package com.example.pipboyv1

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.pipboyv1.ble.BlePotInputContainer
import com.example.pipboyv1.classes.HolotapeContainer
import com.example.pipboyv1.fragments.MainFragment
import com.example.pipboyv1.fragments.TapeFragment
import com.example.pipboyv1.input.IPotInputContainer
import com.example.pipboyv1.mockBle.MockPotDialog
import com.example.pipboyv1.mockBle.MockPotInputContainer
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    companion object {
        const val BLE_REQUEST_CODE: Int = 1
    }

    val SHOW_DEBUG_TAB: Boolean = true

    lateinit var potInputContainer: IPotInputContainer
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (getSystemService(Context.BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager).adapter
    }

    private var nfcAdapter: NfcAdapter? = null
    private var mockPotMenuBtn: Button? = null

    private val mainFragment = MainFragment()
    private val holotapeFragment = TapeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        mockPotMenuBtn = mainFragment.view?.findViewById(R.id.potMenuButton)
        if (mockPotMenuBtn != null) {
            mockPotMenuBtn!!.visibility = View.INVISIBLE
        }

        setupPotInputs()
        handleIntent(intent)

        // Use main fragment
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, mainFragment).commit()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)

        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent) {
        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMsgs ->
            (rawMsgs[0] as NdefMessage).apply {
                handleHolotape(JSONObject(String(records[0].payload)))
            }
        }
    }

    private fun handleHolotape(payload: JSONObject) {
        val holotapeID = payload.get("id") as Int
        Log.e("handleHolotape", "Holotape with ID $holotapeID scanned")

        if (HolotapeContainer.holotapes.none { it.id == holotapeID }) {
            Log.e("handleHolotape", "Holotape does not exist")
            return
        }

        // Switch to holotape fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, holotapeFragment).addToBackStack(null).commit()
        holotapeFragment.onHolotapeLoaded(holotapeID)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            BLE_REQUEST_CODE -> {
                onBlePermissionGranted()
            }

            else -> {}
        }
    }

    private fun setupMockPot() {
        mockPotMenuBtn?.setOnClickListener {
            val mockPotMenu = PopupMenu(this, mockPotMenuBtn)
            mockPotMenu.menuInflater.inflate(R.menu.menu_mockpot, mockPotMenu.menu)
            mockPotMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.addPotValItem -> {
                        MockPotDialog.displayPotIndexDialog(
                            this, MockPotDialog.PotAction.POT_ADD,
                            potInputContainer as MockPotInputContainer
                        )
                    }

                    R.id.subPotValItem -> {
                        MockPotDialog.displayPotIndexDialog(
                            this, MockPotDialog.PotAction.POT_SUB,
                            potInputContainer as MockPotInputContainer
                        )
                    }
                }
                true
            }
            mockPotMenu.show()
        }
        if (mockPotMenuBtn != null) {
            mockPotMenuBtn!!.visibility = View.VISIBLE
        }
    }

    private fun setupPotInputs(forceMock: Boolean = false) {
        val blAdapter = if (forceMock) null else bluetoothAdapter
        if (blAdapter != null) {
            val bluetoothScope: CoroutineScope = lifecycleScope

            potInputContainer = BlePotInputContainer(this, blAdapter, bluetoothScope)

            Log.i("setupPotInputs", "Requesting permissions")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                BLE_REQUEST_CODE
            )
        } else {
            potInputContainer = MockPotInputContainer()
            setupMockPot()
            runOnUiThread {
                Toast.makeText(applicationContext, "Note: Using mocked pot. inputs", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun onBlePermissionGranted() {
        Log.i("setupPotInputs", "Permissions granted")
        (potInputContainer as? BlePotInputContainer)?.onPermissionsGranted()
    }
}
