package com.example.expense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expense.databinding.FragmentDashboardBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            loadChildFragment(HomeFragment())
        }


        b.bottomNav.itemIconTintList = null
        b.bottomNav.itemIconSize = resources.getDimensionPixelSize(R.dimen.nav_icon_size)

        b.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home      -> { loadChildFragment(HomeFragment(), isForward = false); true }
                R.id.expenses  -> { loadChildFragment(EspenceListFragment(), isForward = true); true }
                R.id.analytics -> { loadChildFragment(AnalyticsFragment(), isForward = true); true }
                R.id.budget    -> { loadChildFragment(BudgetFragment(), isForward = true); true }
                R.id.settings  -> { loadChildFragment(SettingsFragment(), isForward = true); true }
                else -> false
            }
        }
    }

    private fun loadChildFragment(fragment: Fragment, isForward: Boolean = true) {
        childFragmentManager.beginTransaction().apply {
            if (isForward) {
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
            }
            replace(R.id.navHostFragment, fragment)
            commit()
        }
    }
}