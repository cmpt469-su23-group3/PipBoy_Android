package com.example.pipboyv1

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.pipboyv1.fragments.topnav.DataFragment
import com.example.pipboyv1.fragments.topnav.InvFragment
import com.example.pipboyv1.fragments.topnav.MapFragment
import com.example.pipboyv1.fragments.topnav.RadioFragment
import com.example.pipboyv1.fragments.topnav.StatFragment
import com.example.pipboyv1.adapters.ViewPagerAdapter
import com.example.pipboyv1.ble.BlePotInputContainer
import com.example.pipboyv1.ble.BluetoothScanManager
import com.example.pipboyv1.mockBle.MockPotDialog
import com.example.pipboyv1.classes.Holotape
import com.example.pipboyv1.input.IPotInputContainer
import com.example.pipboyv1.input.MockPotInputContainer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONObject

class FullscreenActivity : AppCompatActivity() {

    companion object {
        private const val BLE_REQUEST_CODE: Int = 1
    }

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var mockPotMenuBtn: Button

    private lateinit var potInputContainer: IPotInputContainer
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (getSystemService(Context.BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager).adapter
    }
    private var bluetoothScanManager: BluetoothScanManager? = null
    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        tabLayout = findViewById(R.id.topNavTabLayout)
        viewPager2 = findViewById(R.id.topNavViewPager2)
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        setupTopNav()

        setupPotInputs(forceMock = true) // Keeping this true for Mon Jun 26's demo

        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            handleIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMsgs ->
            (rawMsgs[0] as NdefMessage).apply {
                handleHolotape(JSONObject(String(records[0].payload)))
            }
        }
    }

    private fun handleHolotape(payload: JSONObject) {

        var holotape = Holotape(payload.get(getString(R.string.id)) as String)

        // TODO: Dialog that indicates some action
        // TODO: Update layout attributes of quest to signify it's completed
    }

    private fun setupTopNav() {
        val topNavTabs = mapOf(
            getString(R.string.stat_button) to StatFragment(),
            getString(R.string.inv_button) to InvFragment(),
            getString(R.string.data_button) to DataFragment(),
            getString(R.string.map_button) to MapFragment(),
            getString(R.string.radio_button) to RadioFragment(),
        )

        for (topNavTab in topNavTabs.entries.iterator()) {
            adapter.addFragment(topNavTab.value, topNavTab.key)
        }

        viewPager2.adapter = adapter
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2) {
                tab, position -> tab.text = adapter.getFragmentTitle(position)
        }
        tabLayoutMediator.attach()
    }

    private fun setupMockPot() {
        mockPotMenuBtn = findViewById(R.id.potMenuButton)
        mockPotMenuBtn.setOnClickListener {
            val mockPotMenu: PopupMenu = PopupMenu(this, mockPotMenuBtn)
            mockPotMenu.menuInflater.inflate(R.menu.menu_mockpot, mockPotMenu.menu)
            mockPotMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.addPotValItem -> {
                        MockPotDialog.displayPotIndexDialog(this, MockPotDialog.PotAction.POT_ADD,
                            potInputContainer as MockPotInputContainer)
                    }
                    R.id.subPotValItem -> {
                        MockPotDialog.displayPotIndexDialog(this, MockPotDialog.PotAction.POT_SUB,
                            potInputContainer as MockPotInputContainer)
                    }
                }
                true
            })
            mockPotMenu.show()
        }

    }

    private fun setupPotInputs(forceMock: Boolean = false) {
        val container: IPotInputContainer

        val blAdapter = if (forceMock) null else bluetoothAdapter
        if (blAdapter != null) {
            val mgr = BluetoothScanManager(blAdapter)
            bluetoothScanManager = mgr

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                BLE_REQUEST_CODE
            )

            container = BlePotInputContainer(mgr)
        } else {
            container = MockPotInputContainer()
            setupMockPot()
            runOnUiThread {
                AlertDialog.Builder(this).apply {
                    setMessage("Note: Using mocked pot. inputs")
                }.show()
            }
        }

        potInputContainer = container
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            BLE_REQUEST_CODE -> {
                bluetoothScanManager?.startScan()
            }

            else -> recreate()
        }
    }
}
