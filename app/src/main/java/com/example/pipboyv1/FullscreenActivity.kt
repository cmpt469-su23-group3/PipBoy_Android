package com.example.pipboyv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.pipboyv1.fragments.DataFragment
import com.example.pipboyv1.fragments.InvFragment
import com.example.pipboyv1.fragments.MapFragment
import com.example.pipboyv1.fragments.RadioFragment
import com.example.pipboyv1.fragments.StatFragment
import com.example.pipboyv1.fragments.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class FullscreenActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager2 = findViewById(R.id.viewPager2)
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
            adapter.addFragment(topNavTab.value)
            tabLayout.addTab(tabLayout.newTab().setText(topNavTab.key))
        }

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}