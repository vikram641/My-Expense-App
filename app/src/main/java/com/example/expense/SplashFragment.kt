package com.example.expense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.Utlity.TokenManager
import com.example.data.NavEvent
import com.example.expense.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private  lateinit var  b : FragmentSplashBinding

    private val splashViewModel: SplashViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        b = FragmentSplashBinding.inflate(inflater,container, false)
        return b.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        splashViewModel.verifyUserToken()
        observeState()
    }




    fun observeState(){
        lifecycleScope.launch {
            splashViewModel.splashState.collect {state->
                when(state){
                     NavEvent.HOME->{
                         delay(2000)
                         b.progressBar.isEnabled = false

                         findNavController().navigate(R.id.action_splashFragment2_to_dashboardFragment)

                     }

                    NavEvent.LOGIN->{
                        delay(2000)
                        b.progressBar.isEnabled = false
                        findNavController().navigate(R.id.action_splashFragment2_to_loginFragment)


                    }

                    NavEvent.LOADING->{
                        b.progressBar.isEnabled = true

                    }

                }

            }
        }

    }


}