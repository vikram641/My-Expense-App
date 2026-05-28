package com.example.expense

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.example.AuthViewModel
import com.example.ExpenseAdapter
import com.example.ExpensesViewModel
import com.example.LegendAdapter
import com.example.Utlity.Utils
import com.example.data.model.ExpenseQuery
import com.example.data.UiState
import com.example.expense.databinding.FragmentEspenceListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.view.updateLayoutParams
import androidx.constraintlayout.widget.ConstraintLayout

@AndroidEntryPoint
class EspenceListFragment : Fragment() {

    private lateinit var b : FragmentEspenceListBinding
    private var job: Job? = null
    private  val expensesViewModel: ExpensesViewModel by viewModels()
    private lateinit var expenseAdapter: ExpenseAdapter

    @Inject
    lateinit var utils: Utils


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentEspenceListBinding.inflate(inflater,container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseAdapter = ExpenseAdapter()
        setupScrollAnimation()

        b.rvExpenses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = expenseAdapter
            // ← KEY FIX: tell parent DashboardFragment not to steal scroll
            setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }
        }
        b.rvExpenses.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d("SCROLL", "dy = $dy")
            }
        })
//        setupScrollAnimation()

//
//        expensesViewModel.getExpensesSearch(ExpenseQuery(page = 1, limit = 10))
//        b.chipFood.setOnClickListener { expensesViewModel.getExpensesSearch(ExpenseQuery(page = 1, limit = 20, category = "Food")) }
//        b.chipTransport.setOnClickListener { expensesViewModel.getExpensesSearch(ExpenseQuery(page = 1, limit = 20, category = "Transport")) }
//        b.chipBills.setOnClickListener { expensesViewModel.getExpensesSearch(ExpenseQuery(page = 1, limit = 20, category = "Bills")) }
//        b.chipAll.setOnClickListener { expensesViewModel.getExpensesSearch(ExpenseQuery(page = 1, limit = 20)) }
        expensesViewModel.getExpensesSearch(
            ExpenseQuery(page = 1, limit = 10)
        )

// ✅ Handle ALL chip clicks from one place
        b.chipGroup.isSingleSelection = true
        b.chipGroup.check(R.id.chipAll)

        b.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->

            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener

            val selectedId = checkedIds[0]

            val query = when (selectedId) {
                R.id.chipFood -> ExpenseQuery(page = 1, limit = 20, category = "Food")
                R.id.chipTransport -> ExpenseQuery(page = 1, limit = 20, category = "Transport")
                R.id.chipBills -> ExpenseQuery(page = 1, limit = 20, category = "Bills")
                else -> ExpenseQuery(page = 1, limit = 20) // All
            }

            expensesViewModel.getExpensesSearch(query)
        }



        b.searchEt.addTextChangedListener {

            job?.cancel()

            job = lifecycleScope.launch {
                delay(500)

                val text = it.toString()

                if(text.isNotEmpty()){
                    expensesViewModel.getExpensesSearch(
                        ExpenseQuery(

                            search = text
                        )
                    )
                }
            }
        }
        observeState()

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeState(){
        lifecycleScope.launch {
            expensesViewModel.expenseState.collect { state->
                when(state){
                    is UiState.Idle->{

                    }
                    is UiState.Loading->{
                        showLoading(true)


                    }
                    is UiState.Success->{
                        delay(200)
                        showLoading(false)
                        Log.d("bbb",state.data.data.toString())


                        val sectionedList = utils.mapExpenses(state.data.data.expenses?:emptyList())
                        Log.d("nnn",sectionedList.toString())
                        expenseAdapter.submitList(sectionedList) // ✅ never reassign adapter
//                        b.rvExpenses.adapter = expenseAdapter

//                        adapter = ExpenseAdapter(sectionedList)



                    }
                    is UiState.Error->{

                    }
                }
            }
        }
    }



    private var isFilterVisible = true

    private fun setupScrollAnimation() {

        b.rvExpenses.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {

                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int
                ) {

                    super.onScrolled(recyclerView, dx, dy)

                    // Ignore tiny scroll noise
                    if (kotlin.math.abs(dy) < 6) return

                    // =========================
                    // SCROLL UP -> HIDE FILTER
                    // =========================

                    if (dy > 5 && isFilterVisible) {

                        isFilterVisible = false

                        b.filterContainer.animate()
                            .translationY(
                                -b.filterContainer.height.toFloat()
                            )
                            .alpha(0f)
                            .setDuration(220)
//                            .withEndAction {
//
//                                b.filterContainer.visibility =
//                                    View.GONE
//                            }
                            .start()
                        b.rvExpenses.animate()
                            .translationY(
                                -b.filterContainer.height.toFloat()
                            )
                            .setDuration(220)
                            .start()
                    }

                    // =========================
                    // SCROLL DOWN -> SHOW FILTER
                    // ONLY WHEN FIRST ITEM VISIBLE
                    // =========================

                    else if (
                        dy < 0 &&
                        !isFilterVisible &&
                        !recyclerView.canScrollVertically(-1)
                    ) {

                        isFilterVisible = true

                        b.filterContainer.visibility =
                            View.VISIBLE

                        b.filterContainer.translationY =
                            -b.filterContainer.height.toFloat()

                        b.filterContainer.alpha = 0f

                        b.filterContainer.animate()
                            .translationY(0f)
                            .alpha(1f)
                            .setDuration(220)
                            .start()

                        b.rvExpenses.animate()
                            .translationY(0f)
                            .setDuration(220)
                            .start()
                    }
                }
            }
        )
    }
}