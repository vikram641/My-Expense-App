package com.example.expense.feature.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.R
import com.example.expense.core.UiState
import com.example.expense.core.base.BaseFragment
import com.example.expense.core.util.AvatarManager
import com.example.expense.core.util.OnboardingPrefs
import com.example.expense.core.util.TokenManager
import com.example.expense.databinding.FragmentSettingsBinding
import com.example.expense.navigation.DashboardFragment
import com.example.expense.sync.SyncWorker
import com.example.expense.ui.dialog.ExportDataDialog
import com.example.expense.ui.dialog.ExportRange
import com.example.expense.ui.dialog.LogoutConfirmDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var avatarManager: AvatarManager

    @Inject
    lateinit var onboardingPrefs: OnboardingPrefs

    // Only actually needed on API 24-28 (see manifest's maxSdkVersion="28" on
    // WRITE_EXTERNAL_STORAGE) - API 29+ exports via MediaStore, no permission required.
    private val requestExportPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) openExportDialog() else showToast("Storage permission is needed to export")
    }

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
        val isDarkMode = prefs.getBoolean("dark_mode", true)

        binding.switchTheme.isChecked = isDarkMode

// Apply saved mode
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        // Show the local onboarding profile immediately (see CLAUDE.md "Offline mode") -
        // getUserProfile() below overwrites this if a real server profile fetch succeeds.
        binding.tvName.text = onboardingPrefs.getUserName().orEmpty()
        binding.tvCurrencyValue.text = onboardingPrefs.getCurrencyCode()
        refreshAvatarDisplay(onboardingPrefs.getUserName().orEmpty())

        settingsViewModel.getUserProfile()

        binding.profileCard.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_editProfileFragment)
        }

        val onExportRowClicked = View.OnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestExportPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                openExportDialog()
            }
        }
        binding.iconExport.setOnClickListener(onExportRowClicked)
        binding.tvExport.setOnClickListener(onExportRowClicked)
        binding.rowExportClickTarget.setOnClickListener(onExportRowClicked)

        binding.btnLogout.setOnClickListener {
            LogoutConfirmDialog { syncBeforeLogout ->
                val refreshToken = tokenManager.getRefreshToken() ?: ""
                if (syncBeforeLogout) {
                    settingsViewModel.logout(refreshToken)
                } else {
                    settingsViewModel.logoutWithoutSync(refreshToken)
                }
            }.show(childFragmentManager, "logout_confirm")
        }

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
                when (state) {
                    is UiState.Error -> showToast(state.message)
                    UiState.Idle -> {}
                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val user = state.data.data
                        binding.tvName.text = user.name.orEmpty()
                        binding.tvEmail.text = user.email.orEmpty()
                        binding.tvCurrencyValue.text = user.currency.ifEmpty { "INR" }
                        refreshAvatarDisplay(user.name.orEmpty())
                    }
                }
            }
        }

        lifecycleScope.launch {
            settingsViewModel.exportState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        showLoading(false)
                        showToast(state.data)
                    }
                    is UiState.Error -> {
                        showLoading(false)
                        showToast(state.message)
                    }
                    is UiState.Loading -> showLoading(true)
                    UiState.Idle -> {}
                }
            }
        }

        lifecycleScope.launch {
            settingsViewModel.logoutState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        showLoading(false)
                        tokenManager.clearSession()
                        findNavController().navigate(
                            R.id.loginFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.nav_graph, true)
                                .build()
                        )
                    }
                    is UiState.Error -> {
                        showLoading(false)
                        showToast(state.message)
                    }
                    is UiState.Loading -> {
                        showLoading(true)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun openExportDialog() {
        ExportDataDialog { range ->
            settingsViewModel.exportData(range)
        }.show(childFragmentManager, "export_data")
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            binding.loaderLayout.visibility = View.VISIBLE
            binding.lottieProgress.playAnimation()
        } else {
            binding.lottieProgress.cancelAnimation()
            binding.loaderLayout.visibility = View.GONE
        }
        (parentFragment as? DashboardFragment)?.setBottomNavInteractionEnabled(!show)
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

    override fun onResume() {
        super.onResume()
        // Refresh avatar in case user changed it in EditProfileFragment
        val currentName = binding.tvName.text.toString()
        refreshAvatarDisplay(currentName)
    }

    private fun refreshAvatarDisplay(name: String) {
        val emoji = avatarManager.getAvatar()
        if (emoji != null) {
            binding.tvAvatar.text = emoji
            binding.tvAvatar.textSize = 22f
        } else {
            val initials = name.split(" ").take(2)
                .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                .joinToString("").ifEmpty { "?" }
            binding.tvAvatar.text = initials
            binding.tvAvatar.textSize = 18f
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}