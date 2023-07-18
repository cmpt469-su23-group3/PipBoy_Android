package com.example.pipboyv1.ble

import kotlin.math.abs


/**
 * This uses an exponential moving average (EMA) to help smooth out the noise from the
 * potentiometer readings.
 * 
 * https://www.norwegiancreations.com/2015/10/tutorial-potentiometers-with-arduino-and-filtering/ 
 */
class Potentiometer(val potId: Int, val useEmaSmoothing: Boolean) {
    
    companion object {
        /**
         * Coefficient in range [[0.0, 1.0]] that determines how much weight previous samples have.
         * A low alpha value has a lot more lag and takes more older samples into account.
         */
        const val EMA_ALPHA: Float = 0.8f

        /**
         * Threshold at which to change the [filteredValue].
         */
        const val FILTERED_VALUE_THRESHOLD: Float = 0.05f

        /**
         * If a value exceeds this delta value once, then it does not get put in the EMA.
         */
        const val OUTLIER_DELTA: Float = 0.6f
    }
    
    var rawValue: Float = 0f
        private set
    
    var filteredValue: Float = 0f
        private set
    
    private var emaValue: Float = 0f
    private var lastOutlier: Float = 0f
    private var isLastValueOutlier: Boolean = false
    
    fun updateRawValue(newRawValue: Float) {
        this.rawValue = newRawValue
        
        if (this.useEmaSmoothing) {
            runExponentialMovingAverage(newRawValue)
        } else {
            this.filteredValue = newRawValue
        }
    }
    
    private fun runExponentialMovingAverage(newRawValue: Float) {
        if (isOutlierValue(newRawValue)) {
            if (!isLastValueOutlier) {
                isLastValueOutlier = true
                lastOutlier = newRawValue
            } else {
                updateExponentialMovingAverage(lastOutlier)
                updateExponentialMovingAverage(newRawValue)
                isLastValueOutlier = false
            }
        } else {
            updateExponentialMovingAverage(newRawValue)
            if (isLastValueOutlier) {
                isLastValueOutlier = false
            }
        }
    }
    
    private fun updateExponentialMovingAverage(newRawValue: Float) {
        /*
        Formula for EMA:
        S_1 = Y_1
        S_t = (alpha * Y_t) + ((1 - alpha) * S_(t-1))
         */
        
        emaValue = (EMA_ALPHA * newRawValue) + ((1f - EMA_ALPHA) * emaValue)
        
        if (abs(emaValue - filteredValue) >= FILTERED_VALUE_THRESHOLD) {
            filteredValue = emaValue
        }
    }
    
    private fun isOutlierValue(newRawValue: Float): Boolean = 
        abs(filteredValue - newRawValue) >= OUTLIER_DELTA
    
}