package com.example.expense.feature.expense.add

import android.os.Build
import android.os.Bundle
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
import com.example.expense.feature.expense.add.AddExpenseViewModel
import com.example.expense.ui.adapter.ExpenseCatAdapter
import com.example.expense.core.util.Utils
import com.example.expense.data.model.AddExpenseRequest
import com.example.expense.data.model.AddExpenseResponse
import com.example.expense.data.model.CatDataResponse
import com.example.expense.core.UiState
import com.example.expense.databinding.FragmentAddExpenseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class AddExpenseFragment : Fragment() {

    private lateinit var  b : FragmentAddExpenseBinding

    private val _catId = MutableLiveData("cat_95910f50-1e1d-4c48-a1b5-9a2308ea1c2d")

    private  val  addExpenseViewModel: AddExpenseViewModel by viewModels()


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
    b.btnSave.setOnClickListener {
        val inputValidation = userInputBinding(_catId.value.toString())
        val result = utils.validateExpenseInput(inputValidation)

        if(result.first){
            addExpenseViewModel.addExpense(inputValidation)
        }
        else{
            Toast.makeText(requireContext(), result.second, Toast.LENGTH_SHORT).show()
        }

    }


    b.datePickerRow.setOnClickListener {
        Utils.showDateTimePicker(requireContext()) { selectedDate ->
            b.tvDate.text = selectedDate
        }
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

        addExpenseViewModel.getExpenseCat()



        observeState()

        b.btnClose.setOnClickListener {
            findNavController().navigateUp()



        }
    }
    fun userInputBinding(catId : String): AddExpenseRequest {
        val amount = b.etAmount.text.toString().trim()
        val note = b.etNote.text.toString().trim()
        val date = b.tvDate.text.toString()


        return AddExpenseRequest(amount = amount.toInt(), note = note, date = date, categoryId = catId)
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

                        Toast.makeText(requireContext(),"Expense Add successfully", Toast.LENGTH_SHORT).show()
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

                    }
                    is UiState.Loading -> {
                        showLoading(true)

                    }
                    is UiState.Success -> {
                        kotlinx.coroutines.delay(2000)
                        showLoading(false)
                        catAdapter.submitList(state.data.data)
                        b.rvCategories.adapter = catAdapter



                    }
                    is UiState.Idle -> {

                    }
                }
            }
        }
    }

}