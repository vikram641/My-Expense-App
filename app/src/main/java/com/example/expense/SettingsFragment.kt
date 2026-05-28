package com.example.expense

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.expense.databinding.FragmentSettingsBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.DummyAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.BaseFragment
import com.example.SettingsViewModel
import com.example.data.UiState
import com.example.expense.databinding.ActivityMainBinding.inflate
import com.example.expense.databinding.FragmentBudgetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val settingsViewModel : SettingsViewModel by viewModels ()
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )  = FragmentSettingsBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences(
            "settings",
            Context.MODE_PRIVATE
        )

// Restore saved theme state
        val isDarkMode = prefs.getBoolean("dark_mode", false)

        binding.switchTheme.isChecked = isDarkMode

// Apply saved mode
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        settingsViewModel.getUserProfile()

// Theme change listener
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )

            // Save state
            prefs.edit()
                .putBoolean("dark_mode", isChecked)
                .apply()
        }
        binding.switchAlert.isChecked = requireContext()
            .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getBoolean("sms_notification", true)

        binding.switchAlert.setOnCheckedChangeListener { _, isChecked ->
            requireContext()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("sms_notification", isChecked)
                .apply()
        }



//        setupRecycler()
//        setupScrollAnimation()


    }
    override fun observeState() {

        lifecycleScope.launch {
            settingsViewModel.settingState.collect { state ->
                when(state){
                    is UiState.Error -> {
                        showToast(state.message)

                    }
                    UiState.Idle -> {

                    }
                    UiState.Loading -> {



                    }
                    is UiState.Success -> {
                        binding.tvName.text = state.data.data.name
                        binding.tvEmail.text = state.data.data.email



                    }
                }
            }

        }

    }

//    private fun setupRecycler() {
//        binding.recyclerView.layoutManager =
//            LinearLayoutManager(requireContext())
//
//        binding.recyclerView.adapter =
//            DummyAdapter(50)
//    }

    private fun setupScrollAnimation() {

//        binding.recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {

                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int
                ) {

                    val offset =
                        recyclerView.computeVerticalScrollOffset()

                    val progress =
                        (offset / 600f).coerceIn(0f, 1f)
//
//                    // 💧 animate drop
//                    binding.liquidView.progress = progress
//
//                    // 👤 show profile image
//                    binding.profileImage.alpha = progress
//
//                    // scale effect
//                    val scale = 0.5f + (0.5f * progress)
//
//                    binding.profileImage.scaleX = scale
//                    binding.profileImage.scaleY = scale
                }
            }
//        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


}