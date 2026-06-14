package com.example.expense.feature.auth

import android.R
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense.core.util.Utils
import com.example.expense.data.model.LoginUserRequest
import com.example.expense.data.model.LoginUserResponse
import com.example.expense.data.model.RegisterUserRequest
import com.example.expense.data.model.RegisterUserResponse
import com.example.expense.data.repository.Repository
import com.example.expense.core.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    @Inject
    lateinit var utils: Utils
    val username  = MutableLiveData<String?>()
    val userEmail = MutableLiveData<String?>()
    val userPassword = MutableLiveData<String?>()
    val selectedCurrency = MutableLiveData<String>("INR")

    val validateError = MutableLiveData<String?>()



    private val _registerState = MutableStateFlow<UiState<RegisterUserResponse>>(UiState.Idle)
    val registerState : StateFlow<UiState<RegisterUserResponse>> = _registerState

    private val _validateCadential = MutableStateFlow<Pair<Boolean, String>>(Pair(true,""))
    val validateCadential : StateFlow<Pair<Boolean, String>> =_validateCadential


    fun registerUser(){
        val request = RegisterUserRequest(
            name = username.value.orEmpty(),
            email = userEmail.value.orEmpty(),
            password = userPassword.value.orEmpty(),
            currency = selectedCurrency.value ?: "INR"
        )
        viewModelScope.launch {
            _registerState.value = UiState.Loading
            _registerState.value = repository.registerUser(request)


        }
    }

    fun validateCadential(){
        val request = RegisterUserRequest(
            name = username.value.orEmpty(),
            email = userEmail.value.orEmpty(),
            password = userPassword.value.orEmpty(),
            currency = selectedCurrency.value ?: "INR"
        )
        _validateCadential.value = utils.validateUserRegInput(request)
        if (_validateCadential.value.first){
            registerUser()
        }else{
            validateError.value = validateCadential.value.second
        }

    }

    val userEmailForLogin = MutableLiveData<String?>()
    val userPasswordForLogin = MutableLiveData<String?>()

    val loginValidateError = MutableLiveData<String?>()

    private val _validateLoginData= MutableStateFlow(Pair(true,""))
    val validateLoginData : StateFlow<Pair<Boolean, String>> = _validateLoginData

    private val _loginState = MutableStateFlow<UiState<LoginUserResponse>>(UiState.Idle)
    val loginState = _loginState


    fun validateLoginCadential(){
        val request = LoginUserRequest(
            email = userEmailForLogin.value.orEmpty(),
            password = userPasswordForLogin.value.orEmpty())

        _validateLoginData.value = utils.validateUserLoginInput(request)

        if(_validateLoginData.value.first){
            loginUser()

        }
        else{
            loginValidateError.value = validateLoginData.value.second


        }


    }

    fun loginUser(){
        val request = LoginUserRequest(
            email = userEmailForLogin.value.orEmpty(),
            password = userPasswordForLogin.value.orEmpty())

        viewModelScope.launch {
            _loginState.value = UiState.Loading
            _loginState.value = repository.loginUser(request)
        }
    }



}