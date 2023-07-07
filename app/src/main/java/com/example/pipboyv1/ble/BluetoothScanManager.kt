package com.example.pipboyv1.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean
import android.bluetooth.le.ScanCallback as AndroidBleScanCallback


@SuppressLint("MissingPermission") // Assuming we have BT perms already
class BluetoothScanManager(
    private val btAdapter: BluetoothAdapter,
) {
    
    private val bleScanner: BluetoothLeScanner by lazy { btAdapter.bluetoothLeScanner }
    
    private val scanFilter: ScanFilter = ScanFilter.Builder()
        .setServiceUuid(ParcelUuid.fromString(BluetoothIDs.POT_SERVICE_UUID))
        .build()
    private val scanSettings: ScanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()
    
    
    fun startScan(onDeviceFound: (BluetoothDevice) -> Unit) {
        bleScanner.startScan(listOf(scanFilter), scanSettings, ScanCallback(onDeviceFound))
    }
    
    fun stopScan() {
        bleScanner.stopScan(NoOpScanCallback)
    }
    
    private inner class ScanCallback(
        val onDeviceFound: (BluetoothDevice) -> Unit
    ) : AndroidBleScanCallback() {
        
        private val done: AtomicBoolean = AtomicBoolean(false)

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (done.get())
                return
            
            done.set(true)
            
            val dev = result.device
            Log.i("ScanCallback", "Found BLE device, name: ${dev.name}, address: ${dev.address}")
            onDeviceFound(dev)
            stopScan()
        }
    }
    
    private object NoOpScanCallback : AndroidBleScanCallback()
}