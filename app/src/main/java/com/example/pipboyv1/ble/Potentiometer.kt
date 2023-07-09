package com.example.pipboyv1.ble

import kotlin.math.abs


/**
 * This uses an exponential moving average (EMA) to help smooth out the noise from the
 * potentiometer readings.
 * 
 * https://www.norwegiancreations.com/2015/10/tutorial-potentiometers-with-arduino-and-filtering/ 
 */
class Potentiometer(val potId: Int) {
    
    companion object {
        /**
         * Coefficient in range [[0.0, 1.0]] that determines how much weight previous samples have.
         * A low alpha value has a lot more lag and takes more older samples into account.
         */
        const val EMA_ALPHA: Float = 0.5f

        /**
         * Threshold at which to change the [filteredValue].
         */
        const val THRESHOLD: Float = 0.05f
    }
    
    var rawValue: Float = 0f
        private set
    
    var filteredValue: Float = 0f
        private set
    
    private var emaValue: Float = 0f
    
    fun updateRawValue(newRawValue: Float) {
        this.rawValue = newRawValue
        runExponentialMovingAverage(newRawValue)
    }
    
    private fun runExponentialMovingAverage(newRawValue: Float) {
        /*
        Formula for EMA:
        S_1 = Y_1
        S_t = (alpha * Y_t) + ((1 - alpha) * S_(t-1))
         */
        
        emaValue = (EMA_ALPHA * newRawValue) + ((1f - EMA_ALPHA) * emaValue)
        
        if (abs(emaValue - filteredValue) >= THRESHOLD) {
            filteredValue = emaValue
        }
    }
    
}