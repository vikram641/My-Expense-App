package com.example.expense.feature.expense.detail

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.expense.R
import com.example.expense.core.UiState
import com.example.expense.core.base.BaseFragment
import com.example.expense.databinding.FragmentExpanseDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpanseDetailFragment : BaseFragment<FragmentExpanseDetailBinding>() {

    private val viewModel: ExpenseDetailViewModel by viewModels()
    private var expenseId: String? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentExpanseDetailBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseId = arguments?.getString("expenseId")
        expenseId?.let { viewModel.getExpenseDetail(it) }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Delete") { _, _ ->
                    expenseId?.let { id -> viewModel.deleteExpense(id) }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun observeState() {
        lifecycleScope.launch {
            viewModel.detailState.collect { state ->
                when (state) {
                    is UiState.Loading -> showLoading(true)
                    is UiState.Success -> {
                        showLoading(false)
                        val expense = state.data.data
                        binding.tvAmount.text = "-₹${expense.amount}"
                        binding.tvDesc.text = expense.note
                        binding.tvTag.text = expense.category.name
                        binding.tvDate.text = expense.date
                        binding.tvCategory.text = expense.category.name
                        binding.tvCurrency.text = expense.currency
                        binding.tvCreatedAt.text = expense.createdAt ?: ""

                        val resId = requireContext().resources.getIdentifier(
                            expense.category.icon, "drawable", requireContext().packageName
                        )
                        binding.imgExpenseIcon.setImageResource(
                            if (resId != 0) resId else R.drawable.ic_transport
                        )

                        val color = try {
                            val hex = expense.category.color.let {
                                if (it.startsWith("#")) it else "#$it"
                            }
                            Color.parseColor(hex)
                        } catch (e: Exception) {
                            Color.GRAY
                        }
                        binding.iconBg.background?.setTint(color)
                    }
                    is UiState.Error -> {
                        showLoading(false)
                        showToast(state.message)
                    }
                    is UiState.Idle -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.deleteState.collect { state ->
                when (state) {
                    is UiState.Loading -> showLoading(true)
                    is UiState.Success -> {
                        showLoading(false)
                        showToast("Expense deleted")
                        findNavController().navigateUp()
                    }
                    is UiState.Error -> {
                        showLoading(false)
                        showToast(state.message)
                    }
                    is UiState.Idle -> {}
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.btnDelete.isEnabled = !show
        binding.btnEditAction.isEnabled = !show
    }
}
