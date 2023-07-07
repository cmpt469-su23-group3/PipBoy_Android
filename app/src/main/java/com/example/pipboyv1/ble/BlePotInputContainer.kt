package com.example.pipboyv1.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic.FORMAT_FLOAT
import android.util.Log
import com.beepiz.bluetooth.gattcoroutines.ExperimentalBleGattCoroutinesCoroutinesApi
import com.beepiz.bluetooth.gattcoroutines.GattConnection
import com.beepiz.bluetooth.gattcoroutines.extensions.requireCharacteristic
import com.example.pipboyv1.ble.BluetoothIDs.POT_SERVICE_UUID
import com.example.pipboyv1.ble.BluetoothIDs.POT_1_CHARACTERISTIC_UUID
import com.example.pipboyv1.ble.BluetoothIDs.POT_2_CHARACTERISTIC_UUID
import com.example.pipboyv1.ble.BluetoothIDs.POT_3_CHARACTERISTIC_UUID
import com.example.pipboyv1.input.IPotInputContainer
import com.example.pipboyv1.input.PotInputListener
import com.example.pipboyv1.util.toUUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap


@OptIn(ExperimentalBleGattCoroutinesCoroutinesApi::class)
@SuppressLint("MissingPermission")
class BlePotInputContainer(
    private val bluetoothAdapter: BluetoothAdapter,
    private val coroutineScope: CoroutineScope
) : IPotInputContainer {
    
    companion object {
        private const val LOGGING_TAG: String = "BlePotInputContainer"
    }

    private val listeners: MutableSet<PotInputListener> = ConcurrentHashMap.newKeySet()
    private val mgr: BluetoothScanManager by lazy { BluetoothScanManager(bluetoothAdapter) }

    override fun addListener(listener: PotInputListener) {
        listeners += listener
    }

    override fun removeListener(listener: PotInputListener) {
        listeners -= listener
    }

    /**
     * Called when we can finally start scanning.
     */
    fun onPermissionsGranted() {
        startScanning()
    }
    
    private fun startScanning() {
        mgr.startScan(this::onDeviceFound)
    }
    
    private fun onDeviceFound(bluetoothDevice: BluetoothDevice) {
        coroutineScope.launch { 
            val gatt = GattConnection(bluetoothDevice)
            try {
                gatt.connect()
                gatt.discoverServices()

                val serviceUuid = POT_SERVICE_UUID.toUUID()
                val characteristic1 = gatt.requireCharacteristic(serviceUuid, POT_1_CHARACTERISTIC_UUID.toUUID())
                val characteristic2 = gatt.requireCharacteristic(serviceUuid, POT_2_CHARACTERISTIC_UUID.toUUID())
                val characteristic3 = gatt.requireCharacteristic(serviceUuid, POT_3_CHARACTERISTIC_UUID.toUUID())

                Log.i(LOGGING_TAG, "Found service and characteristics")
                
                gatt.readCharacteristic(characteristic1).getFloatValue(FORMAT_FLOAT, 0)
            } finally {
                gatt.close()
            }
        }
    }
    
}