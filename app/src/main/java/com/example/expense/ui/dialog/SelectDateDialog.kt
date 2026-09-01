package com.example.expense.ui.dialog

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expense.R
import com.example.expense.core.base.BaseDialogFragment
import com.example.expense.databinding.DialogSelectDateBinding
import java.util.Calendar

class SelectDateDialog(
    private val selectedDate: Calendar = Calendar.getInstance(),
    private val onDateSelected: (year: Int, month: Int, dayOfMonth: Int) -> Unit
) : BaseDialogFragment<DialogSelectDateBinding>() {

    private var currentYear = selectedDate.get(Calendar.YEAR)
    private var currentMonth = selectedDate.get(Calendar.MONTH)
    private var currentDay = selectedDate.get(Calendar.DAY_OF_MONTH)

    private val months = listOf(
        "Jan", "Feb", "Mar", "Apr",
        "May", "Jun", "Jul", "Aug",
        "Sep", "Oct", "Nov", "Dec"
    )

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DialogSelectDateBinding.inflate(inflater, container, false)

    override fun setup() {
        setupYearSelector()
        setupMonthGrid()
        setupDayGrid()
        setupButtons()
    }

    private fun setupYearSelector() {
        updateYearText()
        binding.btnPrevYear.setOnClickListener {
            currentYear--
            updateYearText()
            clampDayToMonth()
            setupDayGrid()
        }
        binding.btnNextYear.setOnClickListener {
            currentYear++
            updateYearText()
            clampDayToMonth()
            setupDayGrid()
        }
    }

    private fun updateYearText() {
        binding.tvYear.text = currentYear.toString()
    }

    private fun setupMonthGrid() {
        val adapter = MonthAdapter(months, currentMonth) { month ->
            currentMonth = month
            clampDayToMonth()
            setupDayGrid()
        }
        binding.rvMonths.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvMonths.adapter = adapter
    }

    private fun daysInCurrentMonth(): Int {
        val cal = Calendar.getInstance()
        cal.set(currentYear, currentMonth, 1)
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    private fun clampDayToMonth() {
        val maxDay = daysInCurrentMonth()
        if (currentDay > maxDay) currentDay = maxDay
    }

    private fun setupDayGrid() {
        val days = (1..daysInCurrentMonth()).toList()
        val adapter = DayAdapter(days, currentDay) { day ->
            currentDay = day
        }
        binding.rvDays.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.rvDays.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            onDateSelected(currentYear, currentMonth, currentDay)
            dismiss()
        }
    }

    private inner class MonthAdapter(
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
                gravity = Gravity.CENTER
                textSize = 14f
                setPadding(8, 8, 8, 8)
            }
            return MonthVH(tv)
        }

        override fun onBindViewHolder(holder: MonthVH, position: Int) {
            holder.tv.text = months[position]
            if (position == selectedMonth) {
                holder.tv.setBackgroundResource(R.drawable.bg_month_selected)
                holder.tv.setTextColor(ContextCompat.getColor(holder.tv.context, R.color.white))
            } else {
                holder.tv.setBackgroundResource(R.drawable.bg_month_normal)
                holder.tv.setTextColor(ContextCompat.getColor(holder.tv.context, R.color.text_primary))
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

    private inner class DayAdapter(
        private val days: List<Int>,
        private var selectedDay: Int,
        private val onDayClick: (Int) -> Unit
    ) : RecyclerView.Adapter<DayAdapter.DayVH>() {

        inner class DayVH(val tv: TextView) : RecyclerView.ViewHolder(tv)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayVH {
            val tv = TextView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    100
                )
                gravity = Gravity.CENTER
                textSize = 13f
                setPadding(4, 8, 4, 8)
            }
            return DayVH(tv)
        }

        override fun onBindViewHolder(holder: DayVH, position: Int) {
            val day = days[position]
            holder.tv.text = day.toString()
            if (day == selectedDay) {
                holder.tv.setBackgroundResource(R.drawable.bg_month_selected)
                holder.tv.setTextColor(ContextCompat.getColor(holder.tv.context, R.color.white))
            } else {
                holder.tv.setBackgroundResource(R.drawable.bg_month_normal)
                holder.tv.setTextColor(ContextCompat.getColor(holder.tv.context, R.color.text_primary))
            }
            holder.tv.setOnClickListener {
                val prevDay = selectedDay
                val prevPos = days.indexOf(prevDay)
                selectedDay = day
                notifyItemChanged(prevPos)
                notifyItemChanged(position)
                onDayClick(day)
            }
        }

        override fun getItemCount() = days.size
    }
}
