package com.example.pipboyv1.input


/**
 * Holds [PotInputListener]s and calls their functions as inputs come in.
 */
interface IPotInputContainer {
    
    fun addListener(listener: PotInputListener)
    
    fun removeListener(listener: PotInputListener)
    
}