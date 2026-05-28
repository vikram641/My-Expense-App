package com.example.expense.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.BaseDialogFragment
import com.example.HomeViewModel
import com.example.expense.R
import com.example.expense.databinding.DialogMonthYearPickerBinding
import com.example.expense.databinding.DialogSetBudgetBinding
import java.util.Calendar
import kotlin.getValue

class MonthYearPickerDialog(
    private val selectedMonth: Int = Calendar.getInstance().get(Calendar.MONTH),
    private val selectedYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    private val onSelected: (month: Int, year: Int) -> Unit,

) : BaseDialogFragment<DialogMonthYearPickerBinding>() {

//    private var _binding: DialogMonthYearPickerBinding? = null
//    private val binding get() = _binding!!



    private var currentYear = selectedYear
    private var currentMonth = selectedMonth

    private val months = listOf(
        "Jan", "Feb", "Mar", "Apr",
        "May", "Jun", "Jul", "Aug",
        "Sep", "Oct", "Nov", "Dec"
    )

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DialogMonthYearPickerBinding.inflate(inflater, container, false)

    override fun setup() {
        super.onStart()
        setupYearSelector()
        setupMonthGrid()
        setupButtons()
    }



//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//    }

    private fun setupYearSelector() {
        updateYearText()

        // Previous year
        binding.btnPrevYear.setOnClickListener {
            currentYear--
            updateYearText()
        }

        // Next year
        binding.btnNextYear.setOnClickListener {
            currentYear++
            updateYearText()
        }
    }

    private fun updateYearText() {
        binding.tvYear.text = currentYear.toString()
    }

    private fun setupMonthGrid() {
        val adapter = MonthAdapter(months, currentMonth) { month ->
            currentMonth = month
        }
        binding.rvMonths.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvMonths.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            onSelected(currentMonth, currentYear)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        // ✅ Full width dialog
        dialog?.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }



    // ✅ Month Grid Adapter
    inner class MonthAdapter(
        private val months: List<String>,
        private var selectedMonth: Int,
        private val onMonthClick: (Int) -> Unit
    ) : RecyclerView.Adapter<MonthAdapter.MonthVH>() {

        inner class MonthVH(val tv: TextView) : RecyclerView.ViewHolder(tv)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthVH {
            val tv = TextView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    120
                )
                gravity = android.view.Gravity.CENTER
                textSize = 14f
                setPadding(8, 8, 8, 8)
            }
            return MonthVH(tv)
        }

        override fun onBindViewHolder(holder: MonthVH, position: Int) {
            holder.tv.text = months[position]

            if (position == selectedMonth) {
                // ✅ Selected style
                holder.tv.setBackgroundResource(R.drawable.bg_month_selected)
                holder.tv.setTextColor(
                    ContextCompat.getColor(holder.tv.context, R.color.white)
                )
            } else {
                // ✅ Normal style
                holder.tv.setBackgroundResource(R.drawable.bg_month_normal)
                holder.tv.setTextColor(
                    ContextCompat.getColor(holder.tv.context, R.color.text_light)
                )
            }

            holder.tv.setOnClickListener {
                val prev = selectedMonth
                selectedMonth = position
                notifyItemChanged(prev)
                notifyItemChanged(position)
                onMonthClick(position)
            }
        }

        override fun getItemCount() = months.size
    }
}