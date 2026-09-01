package com.example.expense.feature.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.expense.R
import com.example.expense.databinding.FragmentOnboardingSetupNameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetupNameFragment : Fragment() {

    private var _b: FragmentOnboardingSetupNameBinding? = null
    private val b get() = _b!!

    private val viewModel: OnboardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentOnboardingSetupNameBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.etName.setText(viewModel.name)
        b.btnBack.setOnClickListener { findNavController().popBackStack() }
        b.btnContinue.setOnClickListener {
            val name = b.etName.text?.toString()?.trim().orEmpty()
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.name = name
            findNavController().navigate(R.id.action_setupNameFragment_to_setupBudgetFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
