package com.example.expense.feature.chat

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.R
import com.example.expense.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var b: FragmentChatBinding
    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentChatBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatAdapter()
        b.rvChat.layoutManager = LinearLayoutManager(requireContext())
        b.rvChat.adapter = chatAdapter

        b.btnBack.setOnClickListener { findNavController().navigateUp() }

        b.btnSend.setOnClickListener { sendCurrentInput() }
        b.etInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendCurrentInput()
                true
            } else {
                false
            }
        }

        renderSuggestedChips()

        viewLifecycleOwner.lifecycleScope.launch {
            chatViewModel.messages.collect { messages ->
                chatAdapter.submitList(messages)
                if (messages.isNotEmpty()) {
                    b.rvChat.scrollToPosition(messages.size - 1)
                }
            }
        }
    }

    private fun sendCurrentInput() {
        val text = b.etInput.text.toString()
        if (text.isBlank()) return
        chatViewModel.sendMessage(text)
        b.etInput.setText("")
    }

    private fun renderSuggestedChips() {
        b.chipsContainer.removeAllViews()
        chatViewModel.suggestedQuestions.forEach { question ->
            val chip = TextView(requireContext()).apply {
                text = question
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_accent))
                textSize = 12f
                setTypeface(typeface, Typeface.BOLD)
                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_chip_accent)
                setPadding(dp(14), dp(8), dp(14), dp(8))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginEnd = dp(8) }
                setOnClickListener {
                    b.etInput.setText(question)
                    sendCurrentInput()
                }
            }
            b.chipsContainer.addView(chip)
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
