package com.example.expense.feature.auth

import com.example.expense.R
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.expense.core.util.TokenManager
import com.example.expense.core.NavEvent
import com.example.expense.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
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

        // MainActivity's root (activity_main.xml) paints its own background behind the
        // status/nav bars via fitsSystemWindows - fine for the rest of the app, but this
        // screen's purple gradient needs those bars to match it instead, or the area behind
        // them shows the app's usual dark/light background as a mismatched strip. Restored
        // in onDestroyView() once this fragment leaves.
        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.accent)
        window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.accent)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        splashViewModel.verifyUserToken()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val window = requireActivity().window
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        val isDark = requireContext().getSharedPreferences("settings", 0).getBoolean("dark_mode", true)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = !isDark
            isAppearanceLightNavigationBars = !isDark
        }
    }




    fun observeState(){
        lifecycleScope.launch {
            splashViewModel.splashState.collect {state->
                when(state){
                     NavEvent.HOME->{
                         b.progressBar.isEnabled = false

                         findNavController().navigate(R.id.action_splashFragment2_to_dashboardFragment)

                     }

                    NavEvent.LOGIN->{
                        b.progressBar.isEnabled = false
                        findNavController().navigate(R.id.action_splashFragment2_to_loginFragment)


                    }

                    NavEvent.ONBOARDING->{
                        b.progressBar.isEnabled = false
                        findNavController().navigate(R.id.action_splashFragment2_to_welcomeFragment)
                    }

                    NavEvent.LOADING->{
                        b.progressBar.isEnabled = true

                    }

                }

            }
        }

    }
}