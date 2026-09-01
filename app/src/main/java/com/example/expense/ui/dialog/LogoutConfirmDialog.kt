package com.example.expense.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.expense.core.base.BaseDialogFragment
import com.example.expense.databinding.DialogLogoutConfirmBinding
import com.example.expense.sync.SyncWorker

class LogoutConfirmDialog(
    private val onResult: (syncBeforeLogout: Boolean) -> Unit
) : BaseDialogFragment<DialogLogoutConfirmBinding>() {

    override val allowBackPress = true

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DialogLogoutConfirmBinding.inflate(inflater, container, false)

    override fun setup() {
        binding.btnNo.setOnClickListener {
            onResult(false)
            dismiss()
        }
        binding.btnYes.setOnClickListener {
            onResult(true)
            dismiss()
        }
    }
}
