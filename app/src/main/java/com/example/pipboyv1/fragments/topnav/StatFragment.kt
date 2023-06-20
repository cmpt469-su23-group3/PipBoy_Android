package com.example.pipboyv1.fragments.topnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.pipboyv1.R
import com.example.pipboyv1.adapters.ViewPagerAdapter
import com.example.pipboyv1.fragments.stat_subnav.PerksFragment
import com.example.pipboyv1.fragments.stat_subnav.SpecialFragment
import com.example.pipboyv1.fragments.stat_subnav.StatusFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class StatFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = requireView().findViewById(R.id.statSubNavTabLayout)
        viewPager2 = requireView().findViewById(R.id.statSubNavViewPager2)
        adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        setupSubNav()
    }

    private fun setupSubNav() {
        val subNavTabs = mapOf(
            getString(R.string.stat_status_button) to StatusFragment(),
            getString(R.string.stat_special_button) to SpecialFragment(),
            getString(R.string.stat_perks_button) to PerksFragment(),
        )

        for (subNavTab in subNavTabs.entries.iterator()) {
            adapter.addFragment(subNavTab.value, subNavTab.key)
        }

        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) {
                tab, position -> tab.text = adapter.getFragmentTitle(position)
        }.attach()
    }
}