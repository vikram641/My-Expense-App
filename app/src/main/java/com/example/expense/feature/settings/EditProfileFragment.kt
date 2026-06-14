package com.example.expense.feature.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.expense.core.UiState
import com.example.expense.core.base.BaseFragment
import com.example.expense.core.util.AvatarManager
import com.example.expense.core.util.CurrencyConstants
import com.example.expense.databinding.FragmentEditProfileBinding
import com.example.expense.ui.dialog.AvatarPickerBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {

    private val viewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var avatarManager: AvatarManager

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentEditProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currencyAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            CurrencyConstants.CURRENCIES
        )
        binding.etCurrency.setAdapter(currencyAdapter)

        viewModel.getUserProfile()
        refreshAvatarDisplay()

        // Avatar tap → open picker
        binding.tvAvatar.setOnClickListener { openAvatarPicker() }

        // Handle avatar picker result
        parentFragmentManager.setFragmentResultListener(
            AvatarPickerBottomSheet.REQUEST_KEY, viewLifecycleOwner
        ) { _, bundle ->
            val emoji = bundle.getString(AvatarPickerBottomSheet.RESULT_EMOJI) ?: return@setFragmentResultListener
            if (emoji.isEmpty()) avatarManager.clearAvatar() else avatarManager.saveAvatar(emoji)
            refreshAvatarDisplay()
        }

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text?.toString()?.trim() ?: ""
            if (name.isEmpty()) { showToast("Name cannot be empty"); return@setOnClickListener }
            val currencyDisplay = binding.etCurrency.text.toString()
            val currency = CurrencyConstants.getCode(currencyDisplay).ifEmpty { "INR" }
            viewModel.updateProfile(name, currency)
        }

        binding.btnChangePassword.setOnClickListener {
            val current = binding.etCurrentPassword.text?.toString()?.trim() ?: ""
            val newPass = binding.etNewPassword.text?.toString()?.trim() ?: ""
            if (current.isEmpty() || newPass.isEmpty()) {
                showToast("Please fill both password fields")
                return@setOnClickListener
            }
            if (newPass.length < 6) {
                showToast("New password must be at least 6 characters")
                return@setOnClickListener
            }
            viewModel.changePassword(current, newPass)
        }
    }

    override fun observeState() {
        // Pre-fill form with current profile
        lifecycleScope.launch {
            viewModel.settingState.collect { state ->
                if (state is UiState.Success) {
                    val user = state.data.data
                    if (binding.etName.text.isNullOrEmpty()) {
                        binding.etName.setText(user.name.orEmpty())
                    }
                    if (binding.etEmail.text.isNullOrEmpty()) {
                        binding.etEmail.setText(user.email.orEmpty())
                    }
                    if (binding.etCurrency.text.isNullOrEmpty()) {
                        binding.etCurrency.setText(
                            CurrencyConstants.getDisplayForCode(user.currency), false
                        )
                    }
                    refreshAvatarDisplay(user.name.orEmpty())
                }
            }
        }

        // Profile update result
        lifecycleScope.launch {
            viewModel.updateProfileState.collect { state ->
                when (state) {
                    is UiState.Loading -> binding.btnSave.isEnabled = false
                    is UiState.Success -> {
                        binding.btnSave.isEnabled = true
                        showToast("Profile updated successfully")
                        findNavController().navigateUp()
                    }
                    is UiState.Error -> {
                        binding.btnSave.isEnabled = true
                        showToast(state.message)
                    }
                    UiState.Idle -> {}
                }
            }
        }

        // Change password result
        lifecycleScope.launch {
            viewModel.changePasswordState.collect { state ->
                when (state) {
                    is UiState.Loading -> binding.btnChangePassword.isEnabled = false
                    is UiState.Success -> {
                        binding.btnChangePassword.isEnabled = true
                        binding.etCurrentPassword.text?.clear()
                        binding.etNewPassword.text?.clear()
                        showToast("Password changed successfully")
                    }
                    is UiState.Error -> {
                        binding.btnChangePassword.isEnabled = true
                        showToast(state.message)
                    }
                    UiState.Idle -> {}
                }
            }
        }
    }

    private fun openAvatarPicker() {
        AvatarPickerBottomSheet.newInstance(avatarManager.getAvatar())
            .show(parentFragmentManager, "avatar_picker")
    }

    private fun refreshAvatarDisplay(name: String = "") {
        val emoji = avatarManager.getAvatar()
        if (emoji != null) {
            binding.tvAvatar.text = emoji
            binding.tvAvatar.textSize = 26f
        } else {
            val initials = name.split(" ").take(2)
                .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                .joinToString("").ifEmpty { "?" }
            binding.tvAvatar.text = initials
            binding.tvAvatar.textSize = 20f
        }
    }
}
