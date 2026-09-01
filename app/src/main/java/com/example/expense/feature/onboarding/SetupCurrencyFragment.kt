package com.example.expense.feature.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.R
import com.example.expense.core.util.CurrencyConstants
import com.example.expense.databinding.FragmentOnboardingSetupCurrencyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetupCurrencyFragment : Fragment() {

    private var _b: FragmentOnboardingSetupCurrencyBinding? = null
    private val b get() = _b!!

    private val viewModel: OnboardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentOnboardingSetupCurrencyBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currencyCodes = CurrencyConstants.CURRENCIES.map { CurrencyConstants.getCode(it) }
        b.rvCurrencies.layoutManager = LinearLayoutManager(requireContext())
        b.rvCurrencies.adapter = CurrencyAdapter(currencyCodes, viewModel.currencyCode) { code ->
            viewModel.currencyCode = code
        }

        b.btnBack.setOnClickListener { findNavController().popBackStack() }
        b.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_setupCurrencyFragment_to_allSetFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
