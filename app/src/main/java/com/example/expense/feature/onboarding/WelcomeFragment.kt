package com.example.expense.feature.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.expense.R
import com.example.expense.databinding.FragmentOnboardingWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private var _b: FragmentOnboardingWelcomeBinding? = null
    private val b get() = _b!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentOnboardingWelcomeBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_featureScanFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
