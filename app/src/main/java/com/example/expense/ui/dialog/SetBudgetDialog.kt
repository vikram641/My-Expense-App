package com.example.expense.ui.dialog

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.core.util.Utils
import com.example.expense.data.model.BudgetData
import com.example.expense.data.model.CatDataResponse
import com.example.expense.data.model.Category
import com.example.expense.data.model.SetBudgetRequest
import com.example.expense.data.model.setBudgetResponse
import com.example.expense.ui.adapter.ExpenseCatAdapter
import com.example.expense.core.base.BaseDialogFragment
import com.example.expense.databinding.DialogSetBudgetBinding
import kotlinx.coroutines.launch
import kotlin.getValue

class SetBudgetDialog(
    private val categories: List<CatDataResponse>?,
    private val utils: Utils,
//    private val onSaveClick: (SetBudgetRequest) -> Unit,
    private val callback: (BudgetResult) -> Unit




) : BaseDialogFragment<DialogSetBudgetBinding>() {



    private var selectedCategory: String? = null


    private var selectedMonth: String = ""

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DialogSetBudgetBinding.inflate(inflater, container, false)

    override fun setup() {
        super.onStart()


        val formats = utils.getCurrentMonthFormats()

        val apiMonth = formats["api"]          // 2026-04
        val display = formats["full"]          // April 2026


        binding.etCurrency.setText("INR")
        binding.etMonth.text = display



        setupCategory()



        binding.btnCancel.setOnClickListener {
            callback(BudgetResult.Cancel)
            dismiss()
        }

        binding.btnSave.setOnClickListener {

            val amount = binding.etAmount.text.toString()

            if (selectedCategory == null) {
                Toast.makeText(requireContext(), "Select category", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (amount.isEmpty()) {
                binding.etAmount.error = "Enter amount"
                return@setOnClickListener
            }



            val request = SetBudgetRequest(categoryId = selectedCategory!!,
                limitAmount = amount.toInt(),
                month = apiMonth.toString(),
                currency = "INR")



            callback(BudgetResult.Success(request))
            dismiss()
        }
    }


    private fun setupCategory() {

        val adapter = ExpenseCatAdapter { category ->
            selectedCategory = category.id
        }

        binding.rvCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvCategories.adapter = adapter

        adapter.submitList(categories) // replace with API
    }


}

sealed class BudgetResult {
    data class Success(val data: SetBudgetRequest) : BudgetResult()
    object Cancel : BudgetResult()
}