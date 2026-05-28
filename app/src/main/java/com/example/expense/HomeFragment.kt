package com.example.expense

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.HomeViewModel
import com.example.LegendAdapter
import com.example.Utlity.Utils
import com.example.data.UiState
import com.example.data.model.User
import com.example.expense.databinding.FragmentHomeBinding
import com.example.expense.ui.dialog.MonthYearPickerDialog
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.internal.format
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var b : FragmentHomeBinding

    private val homeViewModel : HomeViewModel by viewModels ()

    @Inject
    lateinit var utils: Utils

    private lateinit var legendAdapter: LegendAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentHomeBinding.inflate(inflater,container, false)
        b.vm = homeViewModel
        b.lifecycleOwner = this
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name  = arguments?.getString("name")
        val email = arguments?.getString("email")

        b.avatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://google.com")
            startActivity(intent)
        }


        homeViewModel.userProfileDetail()




        homeViewModel.getSummary()
        homeViewModel.getWeeklySummary()

        observerState()
        legendAdapter = LegendAdapter(emptyList()) // initially empty

        b.rvLegend.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = legendAdapter
        }


        // ✅ Pull to refresh
        b.swipeRefresh.apply {
            setColorSchemeResources(R.color.purple, R.color.green)
            setProgressBackgroundColorSchemeResource(R.color.card_bg)
            setOnRefreshListener {
//                vm.refreshData()  // call your refresh function
                postDelayed({ isRefreshing = false }, 1500)
            }
        }

        // ✅ FAB hide on scroll down, show on scroll up
        b.nestedScroll.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY + 10) {
                    // Scrolling DOWN — hide FAB
                    b.fabAdd.animate()
                        .scaleX(0f).scaleY(0f)
                        .alpha(0f)
                        .setDuration(200)
                        .start()
                } else if (scrollY < oldScrollY - 10) {
                    // Scrolling UP — show FAB
                    b.fabAdd.animate()
                        .scaleX(1f).scaleY(1f)
                        .alpha(1f)
                        .setDuration(200)
                        .start()
                }
            }
        )

        b.tvDate.setOnClickListener {
            val DilogFragment = homeViewModel.getYearMonth()
            DilogFragment.show(childFragmentManager, "MonthYearPicker")

        }

        b.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addExpenseFragment)
        }


    }
    private fun showLoading(show: Boolean) {
        if (show) {
            b.loaderLayout.visibility = View.VISIBLE
            b.lottieProgress.playAnimation()
        } else {
            b.lottieProgress.cancelAnimation()
            b.loaderLayout.visibility = View.GONE
        }
    }

    fun observerState(){

        homeViewModel.progress.observe(viewLifecycleOwner) { value ->

            val animator = ObjectAnimator.ofInt(
                b.progress,              // actual ProgressBar
                "progress",
                b.progress.progress,
                value
            )

            animator.duration = 800
            animator.start()

        }

        lifecycleScope.launch {
            homeViewModel.homeState.collect {state ->
                when(state){
                    is UiState.Idle ->{

                    }
                    is UiState.Loading ->{
                        showLoading(true)

                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()

                    }
                    is UiState.Success ->{
                        kotlinx.coroutines.delay(2000)
                        showLoading(false)
                        legendAdapter = LegendAdapter(state.data.data.byCategory)
                        b.rvLegend.adapter = legendAdapter
                        val pieEntries = ArrayList<PieEntry>()
                        val colors = ArrayList<Int>()

                        state.data.data.byCategory.forEach { item ->
                            pieEntries.add(PieEntry(item.percentage.toFloat(), ""))


                            colors.add(Color.parseColor(item.categoryColor))
                        }
                        val dataSet = PieDataSet(pieEntries, "")
                        dataSet.setDrawValues(false)

                        dataSet.colors = colors
//                        dataSet.valueTextSize = 12f
//                        dataSet.valueTextColor = Color.WHITE
//                        dataSet.sliceSpace = 3f
//                        dataSet.selectionShift = 5f

                        val pieData = PieData(dataSet)
//                        pieData.setDrawValues(false)

                        b.pieChart.apply {
                            data = pieData

                            description.isEnabled = false
                            isDrawHoleEnabled = true
                            setHoleColor(Color.TRANSPARENT)

                            setUsePercentValues(true)
                            setEntryLabelColor(Color.WHITE)
                            setEntryLabelTextSize(10f)

                            animateY(1000)

                            legend.isEnabled = false
                            legend.verticalAlignment



                            invalidate()
                        }






                    }

                }
            }
        }

    }
//    fun setProgressWithAnimation(progressBar: ProgressBar, value: Int) {
//        val animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.progress, value)
//        animator.duration = 800
//        animator.start()
//    }

    
}