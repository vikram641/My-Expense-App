package com.example.expense.feature.analytics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.expense.feature.analytics.AnalyticsViewModel
import com.example.expense.core.base.BaseFragment
import com.example.expense.core.UiState
import com.example.expense.databinding.FragmentAnalyticsBinding
import com.example.expense.databinding.FragmentBudgetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.ui.adapter.AnalyticsCategoryAdapter
import com.example.expense.core.base.BaseAdapter
import com.example.expense.ui.adapter.BudgetAdapter
import com.example.expense.ui.adapter.LegendAdapter
import com.example.expense.core.util.Utils
import com.example.expense.data.model.ByMonth
import com.example.expense.ui.dialog.MonthYearPickerDialog
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.Calendar
import javax.inject.Inject


@AndroidEntryPoint
class AnalyticsFragment : BaseFragment<FragmentAnalyticsBinding>() {

    private val analyticsViewModel : AnalyticsViewModel by viewModels()
    private lateinit var analyticsCategoryAdapter: AnalyticsCategoryAdapter
    @Inject lateinit var utils: Utils

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAnalyticsBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()

        val formats = utils.getCurrentMonthFormats()

        val apiMonth = formats["api"]          // 2026-04
        val display = formats["full"]          // April 2026

        analyticsViewModel.getSummary(apiMonth)
        binding.tvMonth.text = display

        binding.tvMonth.setOnClickListener {
            val DilogFragment = getYearMonth()
            DilogFragment.show(childFragmentManager, "MonthYearPicker")


        }


        observeState()

    }
    fun getYearMonth(): MonthYearPickerDialog{
        val calendar = Calendar.getInstance()

        return MonthYearPickerDialog(
            selectedMonth = calendar.get(Calendar.MONTH),
            selectedYear = calendar.get(Calendar.YEAR),
            onSelected = { month, year ->
                // ✅ Update ViewModel
//                    vm.setSelectedMonth(month, year)

                // ✅ Update TextView
                val monthName = listOf(
                    "-01", "-02", "-03", "-04",
                    "-05", "-06", "-07", "-08",
                    "-09", "-10", "-11", "-12"
                )[month]
                binding.tvMonth.text = "$year$monthName"

                analyticsViewModel.getSummary("$year$monthName")

            }
        )

    }

    private fun setupRecycler() {

        analyticsCategoryAdapter = AnalyticsCategoryAdapter()

        binding.rvCategories.layoutManager =
            LinearLayoutManager(requireContext())

        binding.rvCategories.adapter = analyticsCategoryAdapter
    }

    override fun observeState() {
        lifecycleScope.launch {
            analyticsViewModel.analyticsSate.collect { state->
                when(state){
                    is UiState.Error -> {
                        showToast(state.message)


                    }
                    UiState.Idle -> {


                    }
                    UiState.Loading -> {


                    }
                    is UiState.Success -> {

                        bindMonthlyChart(binding.barChart, state.data.data.byMonth)
                        analyticsCategoryAdapter.submitList(state.data.data.byCategory)



                    }
                }
            }

        }

    }

    fun bindMonthlyChart(barChart: BarChart, list: List<ByMonth>) {

        // 👉 Sort data (important for correct order)
        val sortedList = list.sortedBy { it.month }

        val entries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()
        val colors = mutableListOf<Int>()

        sortedList.forEachIndexed { index, item ->

            entries.add(BarEntry(index.toFloat(), item.amount.toFloat()))

            val month = item.month.split("-")[1]

            val label = when (month) {
                "01" -> "Jan"
                "02" -> "Feb"
                "03" -> "Mar"
                "04" -> "Apr"
                "05" -> "May"
                "06" -> "Jun"
                "07" -> "Jul"
                "08" -> "Aug"
                "09" -> "Sep"
                "10" -> "Oct"
                "11" -> "Nov"
                "12" -> "Dec"
                else -> month
            }

            labels.add(label)

            // 👉 Highlight last month (like your UI purple bar)
            if (index == sortedList.lastIndex) {
                colors.add(Color.parseColor("#7C7BFF")) // highlight#7C7BFF
            } else {
                colors.add(Color.parseColor("#3A4A63")) // dull bars
            }
        }

        val dataSet = BarDataSet(entries, "")
        dataSet.colors = colors
        dataSet.setDrawValues(false)

        val barData = BarData(dataSet)
        barData.barWidth = 0.55f

        barChart.data = barData

        // ===== X Axis =====
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            textColor = Color.parseColor("#8FA3BF")
            granularity = 1f

            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return labels.getOrNull(value.toInt()) ?: ""
                }
            }
        }

        // ===== Y Axis =====
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false

        // ===== UI Clean =====
        barChart.description.isEnabled = false
        barChart.isClickable = false
        barChart.legend.isEnabled = false
        barChart.setDrawGridBackground(false)
        barChart.setScaleEnabled(false)

        barChart.animateY(800)
        barChart.invalidate()
    }




}