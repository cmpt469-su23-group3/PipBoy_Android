package com.example.pipboyv1.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.widget.Toast
import com.beepiz.bluetooth.gattcoroutines.BGC
import com.beepiz.bluetooth.gattcoroutines.ExperimentalBleGattCoroutinesCoroutinesApi
import com.beepiz.bluetooth.gattcoroutines.GattConnection
import com.beepiz.bluetooth.gattcoroutines.OperationFailedException
import com.beepiz.bluetooth.gattcoroutines.extensions.requireCharacteristic
import com.example.pipboyv1.MainActivity
import com.example.pipboyv1.ble.BluetoothIDs.POT_SERVICE_UUID
import com.example.pipboyv1.ble.BluetoothIDs.POT_1_CHARACTERISTIC_UUID
import com.example.pipboyv1.ble.BluetoothIDs.POT_2_CHARACTERISTIC_UUID
import com.example.pipboyv1.ble.BluetoothIDs.POT_3_CHARACTERISTIC_UUID
import com.example.pipboyv1.input.IPotInputContainer
import com.example.pipboyv1.input.PotIDs
import com.example.pipboyv1.input.PotInputListener
import com.example.pipboyv1.util.toUUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.concurrent.ConcurrentHashMap


@OptIn(ExperimentalBleGattCoroutinesCoroutinesApi::class)
@SuppressLint("MissingPermission")
class BlePotInputContainer(
    private val activity: MainActivity,
    private val bluetoothAdapter: BluetoothAdapter,
    private val coroutineScope: CoroutineScope
) : IPotInputContainer {
    
    companion object {
        private const val LOGGING_TAG: String = "BlePotInputContainer"
        
        private val decimalFormat: DecimalFormat = DecimalFormat("0.0000")
    }

    private val listeners: MutableSet<PotInputListener> = ConcurrentHashMap.newKeySet()
    private val bluetoothScanMgr: BluetoothScanManager by lazy { BluetoothScanManager(bluetoothAdapter) }
    private val potentiometers: MutableMap<Int, Potentiometer> = listOf(
        Potentiometer(PotIDs.POT_0),
        Potentiometer(PotIDs.POT_1),
        Potentiometer(PotIDs.POT_2),
    ).associateBy { it.potId }.toMutableMap()

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
    
    private fun startReconnect() {
        Log.i(LOGGING_TAG, "Reconnecting BLE...")
        startScanning()
    }
    
    private fun updatePot(potentiometer: Potentiometer, rawValue: Float) {
        val oldFilteredValue = potentiometer.filteredValue

        potentiometer.updateRawValue(rawValue)

        val currentFilteredValue = potentiometer.filteredValue
        if (oldFilteredValue != currentFilteredValue) {
            // The filtered value got updated
            listeners.forEach { listener -> 
                listener.onInputChange(potentiometer.potId, currentFilteredValue)
            }
            
            val moveLeft = currentFilteredValue < oldFilteredValue
            listeners.forEach { listener -> 
                if (moveLeft) {
                    listener.onMoveLeft(potentiometer.potId, currentFilteredValue)
                } else {
                    listener.onMoveRight(potentiometer.potId, currentFilteredValue)
                }
            }
        }
    }
    
    private fun onPotValueUpdatedRaw(potID: Int, newValue: String) {
        listeners.forEach { listener ->
            listener.onInputChangeRaw(potID, newValue)
        }
    }
    
    private fun startScanning() {
        bluetoothScanMgr.startScan(this::onDeviceFound)
    }
    
    private fun onDeviceFound(bluetoothDevice: BluetoothDevice) {
        coroutineScope.launch { 
            val gatt = GattConnection(bluetoothDevice, GattConnection.ConnectionSettings(autoConnect = true))
            try {
                Log.i(LOGGING_TAG, "Connecting...")
                gatt.connect()
                Log.i(LOGGING_TAG, "Connected; discovering services")
                gatt.discoverServices()
                Log.i(LOGGING_TAG, "Discovered services")

                val serviceUuid = POT_SERVICE_UUID.toUUID()
                val characteristic1 = gatt.requireCharacteristic(serviceUuid, POT_1_CHARACTERISTIC_UUID.toUUID())
                val characteristic2 = gatt.requireCharacteristic(serviceUuid, POT_2_CHARACTERISTIC_UUID.toUUID())
                val characteristic3 = gatt.requireCharacteristic(serviceUuid, POT_3_CHARACTERISTIC_UUID.toUUID())
                
                val characteristicMap = mapOf(
                    PotIDs.POT_0 to characteristic1,
                    PotIDs.POT_1 to characteristic2,
                    PotIDs.POT_2 to characteristic3,
                )

                Log.i(LOGGING_TAG, "Found service and characteristics")
                Toast.makeText(activity.applicationContext, "Found BLE pots", Toast.LENGTH_LONG).show()
                
                while (gatt.isConnected) { // Note: This will be cancelled when the coroutineScope is killed
                    readCharacteristics(gatt, characteristicMap)
                    delay(50L)
                }
            } finally {
                gatt.close()
                startReconnect()
            }
        }
    }
    
    private suspend fun readCharacteristics(gatt: GattConnection, charMap: Map<Int, BGC>) {
        try {
            charMap.forEach { (potID, characteristic) ->
                gatt.readCharacteristic(characteristic)
                val byteArray = characteristic.value
                val intBits = (byteArray[0].toInt() and 0xFF) or (byteArray[1].toInt() and 0xFF shl 8) or (byteArray[2].toInt() and 0xFF shl 16) or (byteArray[3].toInt() and 0xFF shl 24)
                val rawValue = Float.fromBits(intBits)

                val potentiometer = potentiometers.getOrPut(potID) { Potentiometer(potID) }
                updatePot(potentiometer, rawValue)
                onPotValueUpdatedRaw(potID, "${(0..3).joinToString(separator = " ") { i -> byteArray[i].toUnsignedHex() }} -> ${decimalFormat.format(rawValue)} | ${decimalFormat.format(potentiometer.filteredValue)}")
            }
        } catch (opFailedExc: OperationFailedException) {
            // Sometimes we'll get random cases of error code 133. We'll just reconnect and try again
            gatt.close()
        }
    }
    
    private fun Byte.toUnsignedHex(): String =
        this.toUByte().toString(16).padStart(2, '0')
    
}