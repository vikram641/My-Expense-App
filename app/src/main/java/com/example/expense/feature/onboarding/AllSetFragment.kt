package com.example.expense.feature.onboarding

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.expense.R
import com.example.expense.core.util.CurrencyConstants
import com.example.expense.databinding.FragmentOnboardingAllSetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllSetFragment : Fragment() {

    private var _b: FragmentOnboardingAllSetBinding? = null
    private val b get() = _b!!

    private val viewModel: OnboardingViewModel by activityViewModels()
    private var ringAnimator: Animator? = null
    private var viewAlive = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentOnboardingAllSetBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewAlive = true

        val name = viewModel.name.trim().ifEmpty { "there" }
        b.tvHeadline.text = "You're all set, $name!"
        b.tvRecapBudget.text = CurrencyConstants.getSymbol(viewModel.currencyCode) +
            "%,d".format(viewModel.monthlyBudget)
        b.tvRecapCurrency.text = viewModel.currencyCode

        startRingPulse()

        b.btnStart.setOnClickListener {
            lifecycleScope.launch {
                viewModel.completeOnboarding()
                findNavController().navigate(R.id.action_allSetFragment_to_dashboardFragment)
            }
        }
    }

    private fun startRingPulse() {
        b.ringOuter.visibility = View.VISIBLE
        b.ringInner.visibility = View.VISIBLE
        ringAnimator = AnimatorSet().apply {
            playTogether(pulseRing(b.ringOuter, 0L), pulseRing(b.ringInner, 1100L))
            start()
        }
    }

    private fun pulseRing(ring: View, startDelayMs: Long): Animator {
        ring.pivotX = ring.width / 2f
        ring.pivotY = ring.height / 2f
        val scaleX = ObjectAnimator.ofFloat(ring, View.SCALE_X, 0.7f, 1.5f)
        val scaleY = ObjectAnimator.ofFloat(ring, View.SCALE_Y, 0.7f, 1.5f)
        val alpha = ObjectAnimator.ofFloat(ring, View.ALPHA, 0.3f, 0f)
        return AnimatorSet().apply {
            playTogether(scaleX, scaleY, alpha)
            duration = 2200
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
        ringAnimator?.cancel()
        ringAnimator = null
        _b = null
    }
}
