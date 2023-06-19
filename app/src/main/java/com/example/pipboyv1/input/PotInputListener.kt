package com.example.pipboyv1.input

/**
 * Note: functions called in this listener may not be on the main thread!
 */
interface PotInputListener {
    
    fun onInputChange(potIndex: Int, percentageValue: Float)
    
    fun onMoveLeft(potIndex: Int, percentageValue: Float)
    
    fun onMoveRight(potIndex: Int, percentageValue: Float)
    
}