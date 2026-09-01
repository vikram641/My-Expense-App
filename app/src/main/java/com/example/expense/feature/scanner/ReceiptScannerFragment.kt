package com.example.expense.feature.scanner

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.expense.databinding.FragmentReceiptScannerBinding
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ReceiptScannerFragment : Fragment() {

    private lateinit var b: FragmentReceiptScannerBinding

    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var scanLineAnimator: ValueAnimator? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startCamera() else handlePermissionDenied()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentReceiptScannerBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        b.btnCloseScanner.setOnClickListener { findNavController().navigateUp() }
        b.btnCapture.setOnClickListener { onCaptureClicked() }

        if (!requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(requireContext(), "No camera available on this device", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun handlePermissionDenied() {
        Toast.makeText(
            requireContext(),
            "Camera permission is needed to scan receipts",
            Toast.LENGTH_SHORT
        ).show()
        findNavController().navigateUp()
    }

    private fun startCamera() {
        val providerFuture = ProcessCameraProvider.getInstance(requireContext())
        providerFuture.addListener({
            cameraProvider = providerFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(b.previewView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    viewLifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture
                )
                startScanLineAnimation()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Unable to start camera", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun startScanLineAnimation() {
        val overlay = b.scannerOverlay
        overlay.post {
            val bounds = overlay.frameBounds()
            if (bounds.isEmpty) return@post
            scanLineAnimator = ObjectAnimator.ofFloat(b.scanLine, View.TRANSLATION_Y, bounds.top, bounds.bottom).apply {
                duration = 1600
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                start()
            }
        }
    }

    private fun onCaptureClicked() {
        val capture = imageCapture ?: return
        b.btnCapture.isEnabled = false
        setHint("Capturing…", showProgress = true)

        // Read view geometry on the main thread now - onCaptureSuccess below runs on a
        // background executor and must not touch views.
        val viewWidth = b.previewView.width
        val viewHeight = b.previewView.height
        val frame = RectF(b.scannerOverlay.frameBounds())

        capture.takePicture(
            cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val bitmap = try {
                        imageProxyToBitmap(image)?.let { cropToFrame(it, viewWidth, viewHeight, frame) }
                    } finally {
                        image.close()
                    }
                    // CameraX delivers this callback on cameraExecutor's background thread -
                    // hop back to main before touching any views.
                    requireActivity().runOnUiThread {
                        if (bitmap == null) {
                            onExtractionFinished(null)
                        } else {
                            readReceipt(bitmap)
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    requireActivity().runOnUiThread {
                        setHint("Align receipt within frame", showProgress = false)
                        b.btnCapture.isEnabled = true
                        Toast.makeText(requireContext(), "Capture failed, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        val decoded = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return null
        val rotation = image.imageInfo.rotationDegrees
        if (rotation == 0) return decoded
        val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
        return Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true)
    }

    /**
     * PreviewView's default FILL_CENTER scale type center-crops the camera stream to fill
     * the view, so the on-screen scan frame only covers part of what the full-resolution
     * capture actually contains. Sending Gemini the whole photo (receipt plus surrounding
     * table/background) hurts extraction accuracy - this maps the overlay's frame rect (in
     * view pixels) back onto the captured bitmap and crops to it, with a small pad since the
     * frame is a guide, not a hard boundary.
     */
    private fun cropToFrame(bitmap: Bitmap, viewWidth: Int, viewHeight: Int, frame: RectF): Bitmap {
        if (viewWidth <= 0 || viewHeight <= 0 || frame.isEmpty) return bitmap

        val viewAspect = viewWidth.toFloat() / viewHeight
        val bitmapAspect = bitmap.width.toFloat() / bitmap.height

        val visibleLeftFraction: Float
        val visibleTopFraction: Float
        val visibleWidthFraction: Float
        val visibleHeightFraction: Float
        if (bitmapAspect >= viewAspect) {
            visibleWidthFraction = viewAspect / bitmapAspect
            visibleLeftFraction = (1f - visibleWidthFraction) / 2f
            visibleHeightFraction = 1f
            visibleTopFraction = 0f
        } else {
            visibleHeightFraction = bitmapAspect / viewAspect
            visibleTopFraction = (1f - visibleHeightFraction) / 2f
            visibleWidthFraction = 1f
            visibleLeftFraction = 0f
        }

        fun mapX(viewX: Float) = visibleLeftFraction + (viewX / viewWidth) * visibleWidthFraction
        fun mapY(viewY: Float) = visibleTopFraction + (viewY / viewHeight) * visibleHeightFraction

        val padX = frame.width() * 0.08f
        val padY = frame.height() * 0.08f

        val leftFrac = mapX(frame.left - padX).coerceIn(0f, 1f)
        val topFrac = mapY(frame.top - padY).coerceIn(0f, 1f)
        val rightFrac = mapX(frame.right + padX).coerceIn(0f, 1f)
        val bottomFrac = mapY(frame.bottom + padY).coerceIn(0f, 1f)

        val left = (leftFrac * bitmap.width).toInt()
        val top = (topFrac * bitmap.height).toInt()
        val right = (rightFrac * bitmap.width).toInt()
        val bottom = (bottomFrac * bitmap.height).toInt()
        val cropWidth = right - left
        val cropHeight = bottom - top
        if (cropWidth <= 0 || cropHeight <= 0) return bitmap

        return Bitmap.createBitmap(bitmap, left, top, cropWidth, cropHeight)
    }

    private fun readReceipt(bitmap: Bitmap) {
        setHint("Reading receipt…", showProgress = true)
        val categoryNames = arguments?.getStringArray(ARG_CATEGORY_NAMES)?.toList().orEmpty()

        lifecycleScope.launch {
            val result = GeminiReceiptExtractor.extract(bitmap, categoryNames)
            onExtractionFinished(result)
        }
    }

    private fun onExtractionFinished(result: ExtractedReceipt?) {
        val retryMessage = when {
            result == null -> "Couldn't read receipt — check your connection and try again"
            result.status == ReceiptImageStatus.BLURRY ->
                "Image is too blurry to read — hold steady and try again"
            result.status == ReceiptImageStatus.NOT_A_RECEIPT ->
                "That doesn't look like a receipt — try scanning a bill or receipt"
            result.amount == null && result.date == null && result.note == null ->
                "Couldn't find any details on this receipt, try again"
            else -> null
        }

        if (retryMessage != null) {
            setHint(retryMessage, showProgress = false)
            b.btnCapture.isEnabled = true
            return
        }

        findNavController().previousBackStackEntry?.savedStateHandle?.apply {
            set(KEY_AMOUNT, result!!.amount)
            set(KEY_DATE, result.date)
            set(KEY_NOTE, result.note)
            set(KEY_CATEGORY, result.category)
            set(KEY_HAS_RESULT, true)
        }
        findNavController().navigateUp()
    }

    private fun setHint(text: String, showProgress: Boolean) {
        b.tvScanHint.text = text
        b.scanProgress.visibility = if (showProgress) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scanLineAnimator?.cancel()
        scanLineAnimator = null
        cameraProvider?.unbindAll()
        if (::cameraExecutor.isInitialized) cameraExecutor.shutdown()
    }

    companion object {
        const val ARG_CATEGORY_NAMES = "category_names"
        const val KEY_AMOUNT = "scan_amount"
        const val KEY_DATE = "scan_date"
        const val KEY_NOTE = "scan_note"
        const val KEY_CATEGORY = "scan_category"
        const val KEY_HAS_RESULT = "scan_has_result"
    }
}
