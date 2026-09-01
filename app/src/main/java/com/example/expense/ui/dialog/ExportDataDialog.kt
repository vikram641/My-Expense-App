package com.example.expense.ui.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.expense.R
import com.example.expense.core.base.BaseDialogFragment
import com.example.expense.databinding.DialogExportDataBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** What the user picked on ExportDataDialog - either one whole month, or an explicit
 * [fromDate, toDate] range. Both dates are yyyy-MM-dd strings, matching how `date` is
 * stored on ExpenseEntity everywhere else in the app (see CLAUDE.md gotcha about this). */
sealed class ExportRange {
    data class Monthly(val month: String, val label: String) : ExportRange()
    data class Custom(val fromDate: String, val toDate: String) : ExportRange()
}

class ExportDataDialog(
    private val onExport: (ExportRange) -> Unit
) : BaseDialogFragment<DialogExportDataBinding>() {

    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val monthKeyFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    private val monthLabelFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    private var isMonthlyMode = true

    private val monthCal = Calendar.getInstance()
    private var fromCal: Calendar? = null
    private var toCal: Calendar? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DialogExportDataBinding.inflate(inflater, container, false)

    override fun setup() {
        updateMonthRow()

        binding.tabMonthly.setOnClickListener { setMode(monthly = true) }
        binding.tabCustom.setOnClickListener { setMode(monthly = false) }

        binding.rowMonth.setOnClickListener {
            MonthYearPickerDialog(
                selectedMonth = monthCal.get(Calendar.MONTH),
                selectedYear = monthCal.get(Calendar.YEAR)
            ) { month, year ->
                monthCal.set(Calendar.MONTH, month)
                monthCal.set(Calendar.YEAR, year)
                updateMonthRow()
            }.show(childFragmentManager, "export_month_picker")
        }

        binding.rowFromDate.setOnClickListener {
            SelectDateDialog(selectedDate = fromCal ?: Calendar.getInstance()) { year, month, day ->
                val cal = Calendar.getInstance().apply { set(year, month, day) }
                fromCal = cal
                binding.rowFromDate.text = "From: ${displayDateFormat.format(cal.time)}"
            }.show(childFragmentManager, "export_from_date")
        }

        binding.rowToDate.setOnClickListener {
            SelectDateDialog(selectedDate = toCal ?: Calendar.getInstance()) { year, month, day ->
                val cal = Calendar.getInstance().apply { set(year, month, day) }
                toCal = cal
                binding.rowToDate.text = "To: ${displayDateFormat.format(cal.time)}"
            }.show(childFragmentManager, "export_to_date")
        }

        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnExport.setOnClickListener {
            if (isMonthlyMode) {
                onExport(
                    ExportRange.Monthly(
                        month = monthKeyFormat.format(monthCal.time),
                        label = monthLabelFormat.format(monthCal.time)
                    )
                )
                dismiss()
                return@setOnClickListener
            }

            val from = fromCal
            val to = toCal
            if (from == null || to == null) {
                Toast.makeText(requireContext(), "Pick both a from and to date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (from.after(to)) {
                Toast.makeText(requireContext(), "From date must be before to date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            onExport(ExportRange.Custom(apiDateFormat.format(from.time), apiDateFormat.format(to.time)))
            dismiss()
        }
    }

    private fun setMode(monthly: Boolean) {
        isMonthlyMode = monthly
        binding.tabMonthly.setBackgroundResource(if (monthly) R.drawable.bg_month_selected else R.drawable.bg_month_normal)
        binding.tabCustom.setBackgroundResource(if (monthly) R.drawable.bg_month_normal else R.drawable.bg_month_selected)
        binding.rowMonth.visibility = if (monthly) View.VISIBLE else View.GONE
        binding.rowFromDate.visibility = if (monthly) View.GONE else View.VISIBLE
        binding.rowToDate.visibility = if (monthly) View.GONE else View.VISIBLE
    }

    private fun updateMonthRow() {
        binding.rowMonth.text = monthLabelFormat.format(monthCal.time)
    }
}
