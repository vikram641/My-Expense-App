package com.example.expense

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BaseFragment
import com.example.BudgetAdapter
import com.example.BudgetResult
import com.example.BudgetViewModel
import com.example.HomeViewModel
import com.example.SetBudgetDialog
import com.example.Utlity.Utils
import com.example.data.model.CatDataResponse
import com.example.data.model.SetBudgetRequest
import com.example.data.UiState
import com.example.data.model.setBudgetResponse
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

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBudgetBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val formats = utils.getCurrentMonthFormats()
        val apiMonth = formats["api"]          // 2026-04
        val display = formats["full"]          // April 2026
        val short = formats["short"]           // Apr 2026
        val numeric = formats["numeric"]       // 04/2026
        budgetViewModel.getBudgets(apiMonth)




//        binding.tvSetBudget.setOnClickListener { getYearMonth() }
        binding.tvMonth.setOnClickListener {
            val DilogFragment = getYearMonth()
            DilogFragment.show(childFragmentManager, "MonthYearPicker")

        }
        budgetViewModel.getExpenseCat()
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
                    }
                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.blurView)?.visibility = View.GONE
    }

}