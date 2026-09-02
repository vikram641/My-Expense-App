package com.example.expense.ui.dialog

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import com.example.expense.R
import com.example.expense.core.util.AvatarPalette
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
        val checkBadgeSize = 18.dpToPx()

        avatars.forEach { emoji ->
            val isSelected = emoji == currentAvatar
            val color = AvatarPalette.colorFor(emoji)

            val badge = TextView(requireContext()).apply {
                text = emoji
                textSize = 26f
                gravity = Gravity.CENTER
                layoutParams = FrameLayout.LayoutParams(cellSize, cellSize)
                background = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(color)
                    if (isSelected) {
                        setStroke(3.dpToPx(), Color.WHITE)
                    }
                }
                elevation = if (isSelected) 6f else 2f
            }

            val cell = FrameLayout(requireContext()).apply {
                layoutParams = GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED),
                    GridLayout.spec(GridLayout.UNDEFINED)
                ).apply {
                    width = cellSize
                    height = cellSize
                    setMargins(margin, margin, margin, margin)
                }
                addView(badge)
                if (isSelected) {
                    addView(ImageView(requireContext()).apply {
                        setImageResource(R.drawable.ic_check)
                        setColorFilter(Color.WHITE)
                        background = GradientDrawable().apply {
                            shape = GradientDrawable.OVAL
                            setColor(color)
                            setStroke(2.dpToPx(), Color.WHITE)
                        }
                        val pad = 3.dpToPx()
                        setPadding(pad, pad, pad, pad)
                        layoutParams = FrameLayout.LayoutParams(checkBadgeSize, checkBadgeSize).apply {
                            gravity = Gravity.BOTTOM or Gravity.END
                        }
                    })
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
