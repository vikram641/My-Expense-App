package com.example.expense.feature.onboarding

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.R
import com.example.expense.core.util.CurrencyConstants
import com.example.expense.databinding.FragmentOnboardingSetupBudgetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val SLIDER_MAX = 100_000

@AndroidEntryPoint
class SetupBudgetFragment : Fragment() {

    private var _b: FragmentOnboardingSetupBudgetBinding? = null
    private val b get() = _b!!

    private val viewModel: OnboardingViewModel by activityViewModels()
    private val chips get() = listOf(10_000 to b.chip10k, 25_000 to b.chip25k, 50_000 to b.chip50k)

    private val categoryAdapter: OnboardingCategoryAdapter = OnboardingCategoryAdapter { category ->
        if (!viewModel.selectedCategoryIds.add(category.id)) {
            viewModel.selectedCategoryIds.remove(category.id)
        }
        categoryAdapter.setSelectedIds(viewModel.selectedCategoryIds)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentOnboardingSetupBudgetBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.tvCurrencySymbol.text = CurrencyConstants.getSymbol(viewModel.currencyCode)
        b.etBudget.setText(viewModel.monthlyBudget.toString())
        b.etBudget.setSelection(b.etBudget.text?.length ?: 0)

        b.etBudget.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val amount = s?.toString()?.toIntOrNull() ?: 0
                updateChipSelection(amount)
                updateSliderFill(amount)
            }
        })

        chips.forEach { (amount, chip) ->
            chip.setOnClickListener {
                b.etBudget.setText(amount.toString())
                b.etBudget.setSelection(b.etBudget.text?.length ?: 0)
            }
        }

        updateChipSelection(viewModel.monthlyBudget)
        b.sliderTrack.post { updateSliderFill(b.etBudget.text?.toString()?.toIntOrNull() ?: 0) }

        b.rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        b.rvCategories.adapter = categoryAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collect { categories ->
                    categoryAdapter.submitList(categories)
                    categoryAdapter.setSelectedIds(viewModel.selectedCategoryIds)
                }
            }
        }

        b.btnBack.setOnClickListener { findNavController().popBackStack() }
        b.btnContinue.setOnClickListener {
            viewModel.monthlyBudget = (b.etBudget.text?.toString()?.toIntOrNull() ?: 0).coerceAtLeast(0)
            findNavController().navigate(R.id.action_setupBudgetFragment_to_setupCurrencyFragment)
        }
    }

    private fun updateChipSelection(amount: Int) {
        chips.forEach { (chipAmount, chip: TextView) ->
            chip.setBackgroundResource(
                if (chipAmount == amount) R.drawable.bg_chip_outline_selected else R.drawable.bg_chip_outline
            )
        }
    }

    private fun updateSliderFill(amount: Int) {
        val trackWidth = b.sliderTrack.width
        if (trackWidth == 0) return
        val fraction = (amount.toFloat() / SLIDER_MAX).coerceIn(0f, 1f)
        b.sliderFill.layoutParams = b.sliderFill.layoutParams.apply {
            width = (trackWidth * fraction).toInt()
        }
        b.sliderFill.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
