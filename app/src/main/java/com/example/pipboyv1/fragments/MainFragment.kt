package com.example.pipboyv1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.pipboyv1.MainActivity
import com.example.pipboyv1.MainTabInputListener
import com.example.pipboyv1.R
import com.example.pipboyv1.adapters.ViewPagerAdapter
import com.example.pipboyv1.fragments.topnav.DataFragment
import com.example.pipboyv1.fragments.topnav.DebugFragment
import com.example.pipboyv1.fragments.topnav.InvFragment
import com.example.pipboyv1.fragments.topnav.MapFragment
import com.example.pipboyv1.fragments.topnav.RadioFragment
import com.example.pipboyv1.fragments.topnav.StatFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var activity_main: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity_main = activity as MainActivity
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.topNavTabLayout) ?: return
        viewPager2 = view.findViewById(R.id.topNavViewPager2) ?: return
        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)

        setupTopNav()

        activity_main.potInputContainer.addListener(adapter.getFragmentByClass<RadioFragment>())
        if (activity_main.SHOW_DEBUG_TAB) {
            activity_main.potInputContainer.addListener(adapter.getFragmentByClass<DebugFragment>())
        }

        activity_main.potInputContainer.addListener(MainTabInputListener(viewPager2, adapter))
    }

    private fun setupTopNav() {
        val topNavTabs = mutableMapOf(
            getString(R.string.stat_button) to StatFragment(),
            getString(R.string.inv_button) to InvFragment(),
            getString(R.string.data_button) to DataFragment(),
            getString(R.string.map_button) to MapFragment(),
            getString(R.string.radio_button) to RadioFragment(),
        )

        if (activity_main.SHOW_DEBUG_TAB) {
            topNavTabs += "DEBUG" to DebugFragment()
        }

        topNavTabs.forEach { (title, fragment) ->
            adapter.addFragment(fragment, title)
        }

        viewPager2.adapter = adapter
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = adapter.getFragmentTitle(position)
        }
        tabLayoutMediator.attach()
    }
}