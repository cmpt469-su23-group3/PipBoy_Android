package com.example.pipboyv1.fragments.topnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.pipboyv1.R
import com.example.pipboyv1.adapters.ViewPagerAdapter
import com.example.pipboyv1.fragments.data_subnav.QuestsFragment
import com.example.pipboyv1.fragments.data_subnav.StatsFragment
import com.example.pipboyv1.fragments.data_subnav.WorkshopsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DataFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.dataSubNavTabLayout)
        viewPager2 = view.findViewById(R.id.dataSubNavViewPager2)

        setupSubNav()
    }

    private fun setupSubNav() {
        val subNavTabs = mapOf(
            getString(R.string.data_quests_button) to QuestsFragment(),
//            getString(R.string.data_workshops_button) to WorkshopsFragment(),
//            getString(R.string.data_stats_button) to StatsFragment(),
        )

        for (subNavTab in subNavTabs.entries.iterator()) {
            adapter.addFragment(subNavTab.value, subNavTab.key)
        }

        viewPager2.adapter = adapter
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2) {
                tab, position -> tab.text = adapter.getFragmentTitle(position)
        }
        tabLayoutMediator.attach()
    }
}