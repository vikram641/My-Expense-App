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
import com.example.expense.feature.auth.AuthViewModel
import com.example.expense.core.util.TokenManager
import com.example.expense.core.UiState
import com.example.expense.databinding.FragmentLoginBinding
import com.example.expense.sync.SyncWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var b : FragmentLoginBinding
    @Inject
    lateinit var tokenManager: TokenManager

    private  val  authViewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentLoginBinding.inflate(inflater,container, false)
        b.vm = authViewModel
        b.lifecycleOwner = this
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.btnLogin.setOnClickListener {
            authViewModel.validateLoginCadential()
        }

        observeState()

        b.btnReg.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


    }

    fun observeState(){

        lifecycleScope.launch {
            authViewModel.loginState.collect{state ->
                when(state){
                    is UiState.Idle->{}
                    is UiState.Loading->{
                        b.btnLogin.text = "Loading..."
                        b.btnLogin.isEnabled = true
                    }
                    is UiState.Success->{
                        val bundle = Bundle().apply {
                            putString("name", state.data.data.user.name)
                            putString("email", state.data.data.user.email)


                        }
                        tokenManager.saveToken(state.data.data.accessToken)
                        tokenManager.saveRefreshToken(state.data.data.refreshToken)
                        // App runs fully offline for now - FCM registration and server sync
                        // are disabled here too (see CLAUDE.md "Offline mode (temporary)").
                        // authViewModel.registerFcmToken()
                        // SyncWorker.enqueueOneTime(requireContext(), SyncWorker.LOGIN_SYNC)
                        findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment , bundle)

//                        authViewModel.syncBudgets()
//                        authViewModel.saveExpensesLocally()
//                        authViewModel.saveExpenseState.collect {state->
//                            when (state) {
//
//                                is UiState.Success -> {
//                                    Toast.makeText(
//                                        requireContext(),
//                                        state.data,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//
//                                is UiState.Error -> {
//                                    Toast.makeText(
//                                        requireContext(),
//                                        state.message,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//
//                                is UiState.Loading -> {
//                                    b.btnLogin.text = "Loading..."
//                                    b.btnLogin.isEnabled = true
//
//                                }
//
//                                else -> {}
//                            }
//                        }
//
                    }
                    is UiState.Error->{
                        b.btnLogin.text = "Submit"
                        Log.d("find",state.message)
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



}
