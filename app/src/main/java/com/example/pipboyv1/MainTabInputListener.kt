package com.example.pipboyv1

import androidx.viewpager2.widget.ViewPager2
import com.example.pipboyv1.adapters.ViewPagerAdapter
import com.example.pipboyv1.input.PotIDs
import com.example.pipboyv1.input.PotInputListener


class MainTabInputListener(
    private val viewPager: ViewPager2,
    private val adapter: ViewPagerAdapter
) : PotInputListener {
    
    companion object {
        private val TARGET_POT: Int get() = PotIDs.POT_0
    }
    
    override fun onInputChange(potIndex: Int, percentageValue: Float) {
        if (potIndex == TARGET_POT) {
            val numPages = adapter.itemCount
            if (numPages > 1) {
                viewPager.setCurrentItem((percentageValue * numPages).toInt().coerceIn(0, numPages - 1), true)
            }
        }
    }

    override fun onMoveLeft(potIndex: Int, percentageValue: Float) {
    }

    override fun onMoveRight(potIndex: Int, percentageValue: Float) {
    }
}