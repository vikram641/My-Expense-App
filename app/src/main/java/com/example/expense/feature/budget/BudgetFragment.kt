package com.example.expense.feature.budget

import com.example.expense.R
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.core.base.BaseFragment
import com.example.expense.ui.adapter.BudgetAdapter
import com.example.expense.ui.dialog.BudgetResult
import com.example.expense.feature.budget.BudgetViewModel
import com.example.expense.feature.home.HomeViewModel
import com.example.expense.ui.dialog.SetBudgetDialog
import com.example.expense.core.util.Utils
import com.example.expense.data.model.CatDataResponse
import com.example.expense.data.model.SetBudgetRequest
import com.example.expense.core.UiState
import com.example.expense.databinding.FragmentBudgetBinding
import com.example.expense.ui.dialog.MonthYearPickerDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class BudgetFragment : BaseFragment<FragmentBudgetBinding>() {

    private val budgetViewModel : BudgetViewModel by viewModels ()

    lateinit var adapter: BudgetAdapter

    @Inject lateinit var utils: Utils

    private var categoriesList: List<CatDataResponse> = emptyList()

    lateinit var currentDate: String


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBudgetBinding.inflate(inflater, container, false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val formats = utils.getCurrentMonthFormats()
        val apiMonth = formats["api"]          // 2026-04
//        val display = formats["full"]          // April 2026
//        val short = formats["short"]           // Apr 2026
//        val numeric = formats["numeric"]       // 04/2026

        binding.tvMonth.text = apiMonth

        currentDate = apiMonth?: ""

        fun updateBudgetMonth(action: Utils.Action) {
            currentDate = utils.updateMonth(currentDate, action)
            binding.tvMonth.text = currentDate
            budgetViewModel.getBudgets(currentDate)
            budgetViewModel.getSummary(currentDate)
        }

        // Initial API call
        budgetViewModel.getBudgets(currentDate)
        budgetViewModel.getSummary(currentDate)

        binding.btnNext.setOnClickListener {
            updateBudgetMonth(Utils.Action.INCREASE)
        }

        binding.btnPrev.setOnClickListener {
            updateBudgetMonth(Utils.Action.DECREASE)
        }






//        binding.tvSetBudget.setOnClickListener { getYearMonth() }
        binding.tvMonth.setOnClickListener {
            val DilogFragment = getYearMonth()
            DilogFragment.show(childFragmentManager, "MonthYearPicker")

        }
        budgetViewModel.getExpenseCat(true)
        binding.tvSetBudget.setOnClickListener {
            showBlur()

            SetBudgetDialog(
                categories = categoriesList,
                utils = utils,

            ){
                state ->
                when(state){
                    BudgetResult.Cancel -> {
                        hideBlur()

                    }
                    is BudgetResult.Success -> {
                        hideBlur()

                        budgetViewModel.setBudget(state.data)


                    }
                }
            }.show(parentFragmentManager, "budget_dialog")
        }



        setupRecycler()
        observeState()
    }
    override fun onResume() {
        budgetViewModel.getBudgets(currentDate)
        budgetViewModel.getSummary(currentDate)
        super.onResume()

    }
    fun getYearMonth(): MonthYearPickerDialog{
        val calendar = Calendar.getInstance()

        return MonthYearPickerDialog(
            selectedMonth = calendar.get(Calendar.MONTH),
            selectedYear = calendar.get(Calendar.YEAR),
            onSelected = { month, year ->
                // ✅ Update ViewModel
//                    vm.setSelectedMonth(month, year)

                // ✅ Update TextView
                val monthName = listOf(
                    "-01", "-02", "-03", "-04",
                    "-05", "-06", "-07", "-08",
                    "-09", "-10", "-11", "-12"
                )[month]

                budgetViewModel.getBudgets("$year$monthName")
                budgetViewModel.getSummary("$year$monthName")
                binding.tvMonth.text = "$year$monthName"

            }
        )

    }
    private fun setupRecycler() {

        adapter = BudgetAdapter(utils)   // 🔥 initialize here

        binding.rvBudgets.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvBudgets.adapter = adapter
    }

    override fun observeState() {
        lifecycleScope.launch {
            budgetViewModel.categoryState.collect { state ->
                when(state){
                    is UiState.Error -> {

                    }
                    UiState.Idle -> {}
                    UiState.Loading ->{}
                    is UiState.Success-> {
                        categoriesList = state.data.data

                    }
                }
            }
        }
        lifecycleScope.launch {
            budgetViewModel.setBudgetResult.collect { result ->
                when (result) {
                    is UiState.Success -> {
                        showToast("Budget saved")
                        val month = binding.tvMonth.text.toString()
                        budgetViewModel.getBudgets(month, forceRefresh = true)
                        budgetViewModel.getSummary(month, forceRefresh = true)
                    }
                    is UiState.Error -> showToast("Error: ${result.message}")
                    UiState.Idle -> {}
                    UiState.Loading -> {}
                }
            }
        }
        lifecycleScope.launch {
            budgetViewModel.summaryState.collect { state ->
                when (state) {
                    is UiState.Error -> {}
                    UiState.Idle -> {}
                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val totalBudget = state.data.data.totalBudget
                        val totalSpent = state.data.data.totalSpent

                        if (totalBudget == 0) {
                            binding.tvPercent.visibility = View.GONE
                            binding.progressOverall.visibility = View.GONE
                            binding.tvSpent.visibility = View.GONE

                            binding.tvEmptyOverall.visibility = View.VISIBLE
                        } else {
                            binding.tvEmptyOverall.visibility = View.GONE

                            binding.tvPercent.visibility = View.VISIBLE
                            binding.progressOverall.visibility = View.VISIBLE
                            binding.tvSpent.visibility = View.VISIBLE

                            val percent = utils.percentage(totalSpent, totalBudget)
                            binding.tvPercent.text = "$percent% used"
                            binding.progressOverall.progress = percent
                            binding.tvSpent.text = "₹$totalSpent spent of ₹$totalBudget"
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {

            budgetViewModel.budgetState.collect { state->
                when(state){
                    is UiState.Error -> {
                        showToast(state.message)

                    }
                    UiState.Idle -> {

                    }
                    UiState.Loading -> {

                    }
                    is UiState.Success-> {
                        adapter.submitList(state.data.data)
                        adapter.onItemClick = { item, position ->

                            Toast.makeText(
                                requireContext(),
                                item.categoryName,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        adapter.onDeleteClick = { item, position ->
                            AlertDialog.Builder(requireContext())
                                .setTitle("Delete Budget")
                                .setMessage("Are you sure you want to delete the budget for ${item.categoryName}?")
                                .setPositiveButton("Delete") { _, _ ->
                                    budgetViewModel.deleteBudget(item.categoryId, item.month)
                                }
                                .setNegativeButton("Cancel", null)
                                .show()
                        }

                    }
                }
            }

        }
        lifecycleScope.launch {
            budgetViewModel.deleteBudgetResult.collect { result ->
                when (result) {
                    is UiState.Success -> {
                        showToast("Budget deleted")
                        val month = binding.tvMonth.text.toString()
                        budgetViewModel.getBudgets(month, forceRefresh = true)
                        budgetViewModel.getSummary(month, forceRefresh = true)
                    }
                    is UiState.Error -> showToast("Error: ${result.message}")
                    UiState.Idle -> {}
                    UiState.Loading -> {}
                }
            }
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.blurView)?.visibility = View.GONE
    }

}