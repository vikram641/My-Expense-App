package com.example.expense.feature.onboarding

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.expense.R
import com.example.expense.databinding.FragmentOnboardingFeatureBinding

/**
 * One reusable screen for the three onboarding feature-highlight slides (Scan Receipts,
 * Auto Debit, Alerts) - the nav graph supplies different content/animation via arguments
 * per destination rather than three near-identical Fragment classes.
 */
class OnboardingFeatureFragment : Fragment() {

    private var _b: FragmentOnboardingFeatureBinding? = null
    private val b get() = _b!!

    private var runningAnimator: Animator? = null
    private var viewAlive = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentOnboardingFeatureBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewAlive = true

        val args = requireArguments()
        b.ivFeatureIcon.setImageResource(args.getInt(ARG_ICON_RES))
        b.tvHeadline.text = args.getString(ARG_TITLE)
        b.tvSubtext.text = args.getString(ARG_SUBTITLE)
        b.btnNext.text = args.getString(ARG_BUTTON_TEXT)
        b.tvStepLabel.text = "${args.getInt(ARG_STEP_INDEX) + 1} of 3"

        setActiveDot(args.getInt(ARG_STEP_INDEX))
        startAnimation(args.getString(ARG_ANIMATION) ?: ANIM_NONE)

        val nextActionId = args.getInt(ARG_NEXT_ACTION_ID)
        b.btnNext.setOnClickListener { findNavController().navigate(nextActionId) }
        // Skip jumps straight to setupNameFragment by destination id, not a declared
        // <action>, so it doesn't pick up the slide-transition anims set on the graph's
        // actions - pass the same ones explicitly via NavOptions to match.
        b.btnSkip.setOnClickListener {
            findNavController().navigate(R.id.setupNameFragment, null, navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            })
        }
    }

    private fun setActiveDot(stepIndex: Int) {
        val dots = listOf(b.dot1, b.dot2, b.dot3)
        dots.forEachIndexed { index, dot ->
            val active = index == stepIndex
            dot.layoutParams = dot.layoutParams.apply {
                width = if (active) dpToPx(22) else dpToPx(8)
            }
            dot.setBackgroundResource(
                if (active) R.drawable.dot_indicator_active else R.drawable.dot_indicator_idle
            )
        }
    }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()

    private fun startAnimation(type: String) {
        when (type) {
            ANIM_SCAN -> {
                b.scanLine.visibility = View.VISIBLE
                runningAnimator = ObjectAnimator.ofFloat(b.scanLine, View.TRANSLATION_Y, 0f, dpToPx(72).toFloat()).apply {
                    duration = 1400
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = ValueAnimator.INFINITE
                    interpolator = LinearInterpolator()
                    start()
                }
            }
            ANIM_PULSE -> {
                b.pulseRingOuter.visibility = View.VISIBLE
                b.pulseRingInner.visibility = View.VISIBLE
                runningAnimator = AnimatorSet().apply {
                    playTogether(
                        pulseRing(b.pulseRingOuter, 0L),
                        pulseRing(b.pulseRingInner, 900L)
                    )
                    start()
                }
            }
            else -> {}
        }
    }

    private fun pulseRing(ring: View, startDelayMs: Long): Animator {
        ring.pivotX = ring.width / 2f
        ring.pivotY = ring.height / 2f
        val scaleX = ObjectAnimator.ofFloat(ring, View.SCALE_X, 0.6f, 1.35f)
        val scaleY = ObjectAnimator.ofFloat(ring, View.SCALE_Y, 0.6f, 1.35f)
        val alpha = ObjectAnimator.ofFloat(ring, View.ALPHA, 0.30f, 0f)
        return AnimatorSet().apply {
            playTogether(scaleX, scaleY, alpha)
            duration = 1800
            startDelay = startDelayMs
            interpolator = android.view.animation.DecelerateInterpolator()
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (viewAlive) start()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewAlive = false
        runningAnimator?.cancel()
        runningAnimator = null
        _b = null
    }

    companion object {
        const val ARG_ICON_RES = "icon_res"
        const val ARG_TITLE = "title"
        const val ARG_SUBTITLE = "subtitle"
        const val ARG_STEP_INDEX = "step_index"
        const val ARG_BUTTON_TEXT = "button_text"
        const val ARG_ANIMATION = "animation"
        const val ARG_NEXT_ACTION_ID = "next_action_id"

        const val ANIM_NONE = "none"
        const val ANIM_SCAN = "scan"
        const val ANIM_PULSE = "pulse"
    }
}
