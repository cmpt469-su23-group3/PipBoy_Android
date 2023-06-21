package com.example.pipboyv1.fragments.topnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.pipboyv1.R
import com.example.pipboyv1.adapters.ViewPagerAdapter
import com.example.pipboyv1.fragments.inv_subnav.AidFragment
import com.example.pipboyv1.fragments.inv_subnav.AmmoFragment
import com.example.pipboyv1.fragments.inv_subnav.ApparelFragment
import com.example.pipboyv1.fragments.inv_subnav.JunkFragment
import com.example.pipboyv1.fragments.inv_subnav.MiscFragment
import com.example.pipboyv1.fragments.inv_subnav.ModsFragment
import com.example.pipboyv1.fragments.inv_subnav.WeaponsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class InvFragment : Fragment() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = requireView().findViewById(R.id.invSubNavTabLayout)
        viewPager2 = requireView().findViewById(R.id.invSubNavViewPager2)
        adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        setupSubNav()
    }

    private fun setupSubNav() {
        val subNavTabs = mapOf(
            getString(R.string.inv_weapons_button) to WeaponsFragment(),
            getString(R.string.inv_apparel_button) to ApparelFragment(),
            getString(R.string.inv_aid_button) to AidFragment(),
            getString(R.string.inv_misc_button) to MiscFragment(),
            getString(R.string.inv_junk_button) to JunkFragment(),
            getString(R.string.inv_mods_button) to ModsFragment(),
            getString(R.string.inv_ammo_button) to AmmoFragment(),
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