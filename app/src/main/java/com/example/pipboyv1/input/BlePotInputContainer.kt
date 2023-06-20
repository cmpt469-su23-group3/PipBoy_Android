package com.example.pipboyv1.input

import java.util.concurrent.ConcurrentHashMap


class BlePotInputContainer : IPotInputContainer {
    
    private val listeners: MutableSet<PotInputListener> = ConcurrentHashMap.newKeySet()

    override fun addListener(listener: PotInputListener) {
        listeners += listener
    }

    override fun removeListener(listener: PotInputListener) {
        listeners -= listener
    }
    
    // TODO implement the BLE part of this... -CL
}