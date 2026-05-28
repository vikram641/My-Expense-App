package com.example.expense

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Utlity.TokenManager
import com.example.data.NavEvent
import com.example.data.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val tokenManager: TokenManager) : ViewModel() {


    val splashState = MutableStateFlow(NavEvent.LOADING)

    fun verifyUserToken(){
        if (tokenManager.getToken()!=null){
            splashState.value = NavEvent.HOME
        }
        else{
            splashState.value = NavEvent.LOGIN
        }

    }





}



