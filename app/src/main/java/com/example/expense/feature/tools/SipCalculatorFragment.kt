package com.example.expense.feature.tools

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.example.expense.core.base.BaseFragment
import com.example.expense.databinding.FragmentSipCalculatorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.pow

/**
 * Standalone SIP (Systematic Investment Plan) return estimator - a pure local
 * calculation, no network/DB involved, reachable from Settings > SIP Calculator.
 *
 * Uses the standard monthly-compounding SIP future-value formula:
 *   FV = P * (((1 + i)^n - 1) / i) * (1 + i)
 * where P = monthly investment, i = monthly rate (annual% / 12 / 100), n = months.
 */
@AndroidEntryPoint
class SipCalculatorFragment : BaseFragment<FragmentSipCalculatorBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSipCalculatorBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        val recalcWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) = recalculate()
        }

        binding.etMonthlyAmount.addTextChangedListener(recalcWatcher)
        binding.etReturnRate.addTextChangedListener(recalcWatcher)
        binding.etYears.addTextChangedListener(recalcWatcher)

        recalculate()
    }

    override fun observeState() {
        // Pure local calculator - nothing to observe.
    }

    private fun recalculate() {
        val monthly = binding.etMonthlyAmount.text?.toString()?.toDoubleOrNull() ?: 0.0
        val annualRate = binding.etReturnRate.text?.toString()?.toDoubleOrNull() ?: 0.0
        val years = binding.etYears.text?.toString()?.toIntOrNull() ?: 0
        val months = years * 12

        if (monthly <= 0.0 || months <= 0) {
            binding.tvTotalValue.text = "₹0"
            binding.tvInvestedAmount.text = "₹0"
            binding.tvReturnsAmount.text = "₹0"
            setSplit(1f, 0f)
            return
        }

        val invested = monthly * months
        val futureValue = if (annualRate <= 0.0) {
            invested
        } else {
            val monthlyRate = annualRate / 12.0 / 100.0
            monthly * ((((1 + monthlyRate).pow(months)) - 1) / monthlyRate) * (1 + monthlyRate)
        }
        val returns = (futureValue - invested).coerceAtLeast(0.0)

        binding.tvTotalValue.text = "₹${formatAmount(futureValue)}"
        binding.tvInvestedAmount.text = "₹${formatAmount(invested)}"
        binding.tvReturnsAmount.text = "₹${formatAmount(returns)}"

        val investedFraction = (invested / futureValue).toFloat().coerceIn(0.04f, 0.96f)
        setSplit(investedFraction, 1f - investedFraction)
    }

    private fun setSplit(investedFraction: Float, returnsFraction: Float) {
        (binding.fillInvested.layoutParams as LinearLayout.LayoutParams).weight = investedFraction
        (binding.fillReturns.layoutParams as LinearLayout.LayoutParams).weight = returnsFraction
        binding.sipSplitBar.requestLayout()
    }

    private fun formatAmount(value: Double): String {
        val rounded = value.toLong()
        return "%,d".format(rounded)
    }
}
