package com.example.expense.feature.auth

import com.example.expense.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import android.widget.ArrayAdapter
import com.example.expense.feature.auth.AuthViewModel
import com.example.expense.core.util.CurrencyConstants
import com.example.expense.core.util.TokenManager
import com.example.expense.core.util.Utils
import com.example.expense.data.model.RegisterUserRequest
import com.example.expense.core.UiState
import com.example.expense.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var b : FragmentRegisterBinding

    private  val  authViewModel: AuthViewModel by viewModels()
    @Inject
    lateinit var tokenManager: TokenManager
    @Inject
    lateinit var utils: Utils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentRegisterBinding.inflate(inflater,container, false)
        b.vm = authViewModel
        b.lifecycleOwner = this
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val currencyAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            CurrencyConstants.CURRENCIES
        )
        b.etCurrency.setAdapter(currencyAdapter)
        b.etCurrency.setOnItemClickListener { _, _, position, _ ->
            authViewModel.selectedCurrency.value =
                CurrencyConstants.getCode(CurrencyConstants.CURRENCIES[position])
        }

        b.btnReg.setOnClickListener {
            authViewModel.validateCadential()
        }

        observeState()



    }

    private fun observeState(){


    lifecycleScope.launch {
            authViewModel.registerState.collect {state->

                when(state){
                    is UiState.Idle->{

                    }
                    is UiState.Success->{
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    is UiState.Loading->{
                        b.btnReg.text = "Loading..."
                        b.btnReg.isEnabled = true
                    }
                    is UiState.Error->{
                        b.btnReg.text = "Create Account"
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()



                    }
                }
            }

        }
    }


//    val currencyList = listOf(
//        "🇮🇳 INR — Indian Rupee",
//        "🇺🇸 USD — US Dollar",
//        "🇪🇺 EUR — Euro"
//    )
//
//    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, currencyList)
//    binding.etCurrency.setAdapter(adapter)
}