package com.example

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.expense.R
import eightbitlab.com.blurview.BlurView

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    private var blurView: BlurView? = null   // 🔥 add this

    abstract fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBlur()   // 🔥 initialize blur
        observeState()
    }

    // -----------------------------
    // 🔥 BLUR SETUP
    // -----------------------------
    private fun setupBlur() {

        blurView = activity?.findViewById(R.id.blurView)

        blurView?.let {

            val decorView = requireActivity().window.decorView
            val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)

            it.setupWith(rootView)
                .setBlurRadius(20f)

        }
    }

    // -----------------------------
    // 🔥 SHOW BLUR
    // -----------------------------
    protected fun showBlur() {
        blurView?.visibility = View.VISIBLE
    }

    // -----------------------------
    // 🔥 HIDE BLUR
    // -----------------------------
    protected fun hideBlur() {
        blurView?.visibility = View.GONE
    }

    // -----------------------------
    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    abstract fun observeState()
}