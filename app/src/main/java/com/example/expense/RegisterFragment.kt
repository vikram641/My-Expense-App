package com.example.expense

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
import com.example.AuthViewModel
import com.example.Utlity.TokenManager
import com.example.Utlity.Utils
import com.example.data.model.RegisterUserRequest
import com.example.data.UiState
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