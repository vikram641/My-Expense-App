package com.example.expense.ui.dialog

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import com.example.expense.databinding.FragmentAvatarPickerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AvatarPickerBottomSheet : BottomSheetDialogFragment() {

    private var _b: FragmentAvatarPickerBinding? = null
    private val b get() = _b!!

    companion object {
        private const val ARG_CURRENT = "current_avatar"
        const val REQUEST_KEY = "avatar_picked"
        const val RESULT_EMOJI = "emoji"

        fun newInstance(currentAvatar: String?) = AvatarPickerBottomSheet().apply {
            arguments = bundleOf(ARG_CURRENT to currentAvatar)
        }
    }

    private val avatars = listOf(
        "😎", "🦁", "🐼", "🦊",
        "🐯", "🦋", "🎭", "🚀",
        "⚡", "🎮", "🌟", "🔥",
        "💎", "🎨", "🐉", "👑"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentAvatarPickerBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentAvatar = arguments?.getString(ARG_CURRENT)
        buildGrid(currentAvatar)

        b.tvRemoveAvatar.setOnClickListener {
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(RESULT_EMOJI to ""))
            dismiss()
        }
    }

    private fun buildGrid(currentAvatar: String?) {
        val screenWidth = resources.displayMetrics.widthPixels
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, resources.displayMetrics).toInt()
        val cellSize = (screenWidth - margin * 2 * 4 - 24.dpToPx()) / 4

        avatars.forEach { emoji ->
            val cell = TextView(requireContext()).apply {
                text = emoji
                textSize = 28f
                gravity = Gravity.CENTER
                layoutParams = GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED),
                    GridLayout.spec(GridLayout.UNDEFINED)
                ).apply {
                    width = cellSize
                    height = cellSize
                    setMargins(margin, margin, margin, margin)
                }
                if (emoji == currentAvatar) {
                    setBackgroundColor(Color.parseColor("#2A3A6A"))
                }
                setOnClickListener {
                    parentFragmentManager.setFragmentResult(
                        REQUEST_KEY, bundleOf(RESULT_EMOJI to emoji)
                    )
                    dismiss()
                }
            }
            b.avatarGrid.addView(cell)
        }
    }

    private fun Int.dpToPx(): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, toFloat(), resources.displayMetrics).toInt()

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
