package com.example.pipboyv1.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    
    private val fragmentList: MutableList<Fragment> = mutableListOf()
    private val fragmentTitleList: MutableList<String> = mutableListOf()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (position <= itemCount - 1) fragmentList[position] else fragmentList[0]
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    fun getFragmentTitle(position: Int): String {
        return if (position <= itemCount - 1) fragmentTitleList[position] else fragmentTitleList[0]
    }
    
    fun getFragmentList(): List<Fragment> = fragmentList
    
    inline fun <reified F : Fragment> getFragmentByClass(): F = 
        getFragmentList().first { f -> f is F } as F
}