package com.example.expense.feature.expense.list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expense.R
import com.example.expense.core.util.Utils
import com.example.expense.databinding.FragmentEspenceListBinding
import com.example.expense.ui.adapter.ExpenseAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        expenseAdapter.onItemClick = { expense ->
            val bundle = bundleOf("expenseId" to expense.id)
            findNavController().navigate(
                R.id.action_dashboardFragment_to_expanseDetailFragment, bundle
            )
        }

        b.rvExpenses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = expenseAdapter
            // ← KEY FIX: tell parent DashboardFragment not to steal scroll
            setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }
        }

        b.chipGroup.isSingleSelection = true
        b.chipGroup.check(R.id.chipAll)

        b.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            expensesViewModel.setCategory(
                when (checkedIds[0]) {
                    R.id.chipFood      -> "Food"
                    R.id.chipTransport -> "Transport"
                    R.id.chipBills     -> "Bills"
                    else               -> null
                }
            )
        }

        b.searchEt.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(400)
                expensesViewModel.setSearch(it?.toString()?.trim()?.ifEmpty { null })
            }
        }

        observeState()

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeState() {
        lifecycleScope.launch {
            expensesViewModel.displayExpenses.collect { expenses ->
                val sectionedList = utils.mapExpenses(expenses)
                expenseAdapter.submitList(sectionedList)
                b.emptyStateLayout.visibility = if (expenses.isEmpty()) View.VISIBLE else View.GONE
                b.rvExpenses.visibility = if (expenses.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }



}