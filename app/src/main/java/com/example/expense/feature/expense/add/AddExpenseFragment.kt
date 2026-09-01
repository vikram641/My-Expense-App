package com.example.expense.feature.expense.add

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.R
import com.example.expense.feature.expense.add.AddExpenseViewModel
import com.example.expense.feature.scanner.ExtractedReceipt
import com.example.expense.feature.scanner.ReceiptScannerFragment
import com.example.expense.ui.adapter.ExpenseCatAdapter
import com.example.expense.core.util.Utils
import com.example.expense.data.model.AddExpenseRequest
import com.example.expense.data.model.AddExpenseResponse
import com.example.expense.data.model.CatDataResponse
import com.example.expense.core.UiState
import com.example.expense.databinding.FragmentAddExpenseBinding
import com.example.expense.ui.dialog.SelectDateDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class AddExpenseFragment : Fragment() {

    private lateinit var  b : FragmentAddExpenseBinding

    private val _catId = MutableLiveData("")

    private  val  addExpenseViewModel: AddExpenseViewModel by viewModels()

    private var editExpenseId: String? = null
    private var pendingCategoryId: String? = null
    private var lastLoadedCategories: List<CatDataResponse>? = null
    private var pendingScanResult: ExtractedReceipt? = null

    lateinit var catAdapter: ExpenseCatAdapter

    @Inject
    lateinit var utils: Utils





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentAddExpenseBinding.inflate(inflater,container, false)
        return b.root
    }

//    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    catAdapter = ExpenseCatAdapter { item ->
            _catId.value = item.id

    }

    editExpenseId = arguments?.getString("expenseId")
    if (editExpenseId != null) {
        b.tvTitle.text = "Edit Expense"
        b.btnSave.text = "UPDATE"
        addExpenseViewModel.getExpenseDetail(editExpenseId!!)
    }

    b.btnSave.setOnClickListener {
        val inputValidation = userInputBinding(_catId.value.toString())
        val result = utils.validateExpenseInput(inputValidation)
        Log.d("result", result.second + result.first)


        if(result.first){
            val id = editExpenseId
            if (id != null) {
                addExpenseViewModel.updateExpense(id, inputValidation)
            } else {
                addExpenseViewModel.addExpense(inputValidation.apply { this.amount.toInt()})
            }
        }
        else{
            Toast.makeText(requireContext(), result.second, Toast.LENGTH_SHORT).show()
        }

    }


    b.datePickerRow.setOnClickListener {
        SelectDateDialog(selectedDate = Calendar.getInstance()) { year, month, dayOfMonth ->
            b.tvDate.text = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
        }.show(childFragmentManager, "select_date")
    }






        b.rvCategories.apply {
            adapter = catAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)


        }

        b.btnScanReceipt.isEnabled = false
        addExpenseViewModel.getExpenseCat()



        observeState()

        b.btnClose.setOnClickListener {
            findNavController().navigateUp()



        }

        b.btnScanReceipt.setOnClickListener {
            val categoryNames = (lastLoadedCategories.orEmpty())
                .mapNotNull { it.name?.takeIf(String::isNotBlank) }
                .toTypedArray()
            val args = Bundle().apply {
                putStringArray(ReceiptScannerFragment.ARG_CATEGORY_NAMES, categoryNames)
            }
            findNavController().navigate(R.id.action_addExpenseFragment_to_receiptScannerFragment, args)
        }

        observeScanResult()
    }

    private fun observeScanResult() {
        val handle = findNavController().currentBackStackEntry?.savedStateHandle ?: return
        handle.getLiveData<Boolean>(ReceiptScannerFragment.KEY_HAS_RESULT).observe(viewLifecycleOwner) { hasResult ->
            if (hasResult != true) return@observe

            val extracted = ExtractedReceipt(
                amount = handle.get<String>(ReceiptScannerFragment.KEY_AMOUNT),
                date = handle.get<String>(ReceiptScannerFragment.KEY_DATE),
                note = handle.get<String>(ReceiptScannerFragment.KEY_NOTE),
                category = handle.get<String>(ReceiptScannerFragment.KEY_CATEGORY)
            )

            // Consume once so re-observing on the next screen visit doesn't re-apply stale data.
            handle.remove<Boolean>(ReceiptScannerFragment.KEY_HAS_RESULT)
            handle.remove<String>(ReceiptScannerFragment.KEY_AMOUNT)
            handle.remove<String>(ReceiptScannerFragment.KEY_DATE)
            handle.remove<String>(ReceiptScannerFragment.KEY_NOTE)
            handle.remove<String>(ReceiptScannerFragment.KEY_CATEGORY)

            applyExtractedReceipt(extracted)
        }
    }

    private fun applyExtractedReceipt(extracted: ExtractedReceipt) {
        extracted.amount?.let { b.etAmount.setText(it) }
        extracted.note?.let { b.etNote.setText(it) }
        extracted.date?.let { b.tvDate.text = it }

        val categoryName = extracted.category ?: run {
            Log.d("ScanCategory", "no category returned by Gemini for this scan")
            return
        }
        val categories = lastLoadedCategories
        if (categories == null) {
            Log.d("ScanCategory", "categories not loaded yet, queuing category='$categoryName'")
            pendingScanResult = extracted
            return
        }
        val match = categories.firstOrNull { it.name?.equals(categoryName, ignoreCase = true) == true }
            ?: categories.firstOrNull { cat ->
                val name = cat.name ?: return@firstOrNull false
                name.contains(categoryName, ignoreCase = true) || categoryName.contains(name, ignoreCase = true)
            }
        if (match?.id == null) {
            Log.d(
                "ScanCategory",
                "no match for category='$categoryName' among [${categories.mapNotNull { it.name }.joinToString()}]"
            )
            return
        }
        _catId.value = match.id
        catAdapter.preselect(match.id)
        val position = categories.indexOfFirst { it.id == match.id }
        if (position >= 0) b.rvCategories.smoothScrollToPosition(position)
    }
    fun userInputBinding(catId : String): AddExpenseRequest {
        val amount = b.etAmount.text.toString().trim()
        val note = b.etNote.text.toString().trim()
        val date = b.tvDate.text.toString()


        return AddExpenseRequest(amount = amount, note = note, date = date, categoryId = catId)
    }


    private fun showLoading(show: Boolean) {
        if (show) {
            b.loaderLayout.visibility = View.VISIBLE
            b.lottieProgress.playAnimation()
        } else {
            b.lottieProgress.cancelAnimation()
            b.loaderLayout.visibility = View.GONE
        }
    }


    fun observeState(){
        lifecycleScope.launch {
            addExpenseViewModel.addExpenseState.collect {state->
                when(state){
                    is UiState.Error ->{
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()

                    }
                    is UiState.Loading ->{




                    }
                    is UiState.Success ->{

                        Toast.makeText(requireContext(),state.data, Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()


                    }
                    is UiState.Idle ->{

                    }
                }
            }
        }
        lifecycleScope.launch {

            addExpenseViewModel.categoryState.collect {state ->
                when(state){
                    is UiState.Error -> {
                        b.btnScanReceipt.isEnabled = true

                    }
                    is UiState.Loading -> {
                        showLoading(true)

                    }
                    is UiState.Success -> {
//                        kotlinx.coroutines.delay(2000)
                        showLoading(false)
                        catAdapter.submitList(state.data.data)
                        b.rvCategories.adapter = catAdapter
                        lastLoadedCategories = state.data.data
                        b.btnScanReceipt.isEnabled = true
                        pendingCategoryId?.let { catAdapter.preselect(it) }
                        pendingScanResult?.let {
                            pendingScanResult = null
                            applyExtractedReceipt(it)
                        }



                    }
                    is UiState.Idle -> {

                    }
                }
            }
        }
        lifecycleScope.launch {
            addExpenseViewModel.editExpenseState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        val expense = state.data.data
                        b.etAmount.setText(expense.amount)
                        b.etNote.setText(expense.note)
                        b.tvDate.text = expense.date
                        pendingCategoryId = expense.category.id
                        _catId.value = expense.category.id
                        catAdapter.preselect(expense.category.id)
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Loading, is UiState.Idle -> {}
                }
            }
        }
    }

}