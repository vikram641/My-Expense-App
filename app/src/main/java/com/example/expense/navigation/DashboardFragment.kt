package com.example.expense.navigation

import com.example.expense.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.expense.databinding.FragmentDashboardBinding
import com.example.expense.feature.analytics.AnalyticsFragment
import com.example.expense.feature.budget.BudgetFragment
import com.example.expense.feature.expense.list.EspenceListFragment
import com.example.expense.feature.home.HomeFragment
import com.example.expense.feature.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var b: FragmentDashboardBinding

   

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentDashboardBinding.inflate(inflater, container, false)
        return b.root
    }

    private val tabOrder = listOf(R.id.home, R.id.expenses, R.id.analytics, R.id.budget, R.id.settings)
    private var currentTabIndex = 0
    private var isBottomNavLocked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            loadChildFragment(HomeFragment(), animate = false)
        }

        b.bottomNav.itemIconTintList = null
        b.bottomNav.itemIconSize = resources.getDimensionPixelSize(R.dimen.nav_icon_size)

        b.bottomNav.setOnItemSelectedListener { item ->
            if (isBottomNavLocked) return@setOnItemSelectedListener false

            val newIndex = tabOrder.indexOf(item.itemId)
            if (newIndex == -1 || newIndex == currentTabIndex) return@setOnItemSelectedListener false

            val goForward = newIndex > currentTabIndex
            currentTabIndex = newIndex

            val fragment = when (item.itemId) {
                R.id.home      -> HomeFragment()
                R.id.expenses  -> EspenceListFragment()
                R.id.analytics -> AnalyticsFragment()
                R.id.budget    -> BudgetFragment()
                R.id.settings  -> SettingsFragment()
                else           -> return@setOnItemSelectedListener false
            }
            loadChildFragment(fragment, goForward)
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(b.root) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            b.bottomNav.visibility = if (imeVisible) View.GONE else View.VISIBLE
            insets
        }
        b.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addExpenseFragment)
        }
    }

    /** Lets a child tab (e.g. Home's "See all") switch to another bottom-nav tab
     * the same way a direct tap on that nav item would. */
    fun goToTab(itemId: Int) {
        b.bottomNav.selectedItemId = itemId
    }

    fun setBottomNavInteractionEnabled(enabled: Boolean) {
        isBottomNavLocked = !enabled
        b.bottomNav.isEnabled = enabled
    }

    private fun loadChildFragment(fragment: Fragment, goForward: Boolean = true, animate: Boolean = true) {
        childFragmentManager.beginTransaction().apply {
            if (animate) {
                if (goForward) {
                    setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                }
            }
            replace(R.id.navHostFragment, fragment)
            commit()
        }
    }
}