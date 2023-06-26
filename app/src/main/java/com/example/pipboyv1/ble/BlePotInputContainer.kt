package com.example.pipboyv1.ble

import com.example.pipboyv1.input.IPotInputContainer
import com.example.pipboyv1.input.PotInputListener
import java.util.concurrent.ConcurrentHashMap


class BlePotInputContainer(private val bluetoothScanManager: BluetoothScanManager) : IPotInputContainer {
    
    private val listeners: MutableSet<PotInputListener> = ConcurrentHashMap.newKeySet()

    override fun addListener(listener: PotInputListener) {
        listeners += listener
    }

    override fun removeListener(listener: PotInputListener) {
        listeners -= listener
    }
    
    // TODO implement the BLE part of this... -CL
}