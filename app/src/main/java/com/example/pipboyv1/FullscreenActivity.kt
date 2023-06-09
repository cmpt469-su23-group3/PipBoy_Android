package com.example.pipboyv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.pipboyv1.fragments.topnav.DataFragment
import com.example.pipboyv1.fragments.topnav.InvFragment
import com.example.pipboyv1.fragments.topnav.MapFragment
import com.example.pipboyv1.fragments.topnav.RadioFragment
import com.example.pipboyv1.fragments.topnav.StatFragment
import com.example.pipboyv1.fragments.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FullscreenActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        tabLayout = findViewById(R.id.topNavTabLayout)
        viewPager2 = findViewById(R.id.topNavViewPager2)
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        setupTopNav()
    }

    private fun setupTopNav() {
        val topNavTabs = mapOf(
            getString(R.string.stat_button) to StatFragment(),
            getString(R.string.inv_button) to InvFragment(),
            getString(R.string.data_button) to DataFragment(),
            getString(R.string.map_button) to MapFragment(),
            getString(R.string.radio_button) to RadioFragment(),
        )

        for (topNavTab in topNavTabs.entries.iterator()) {
            adapter.addFragment(topNavTab.value, topNavTab.key)
        }

        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) {
                tab, position -> tab.text = adapter.getFragmentTitle(position)
        }.attach()
    }
}