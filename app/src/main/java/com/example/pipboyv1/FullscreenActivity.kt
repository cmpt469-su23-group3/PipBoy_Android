package com.example.pipboyv1

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
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
import com.example.pipboyv1.mockBle.PotValueDialogDisplay
import com.example.pipboyv1.mockBle.PotIndexDialogDisplay
import com.example.pipboyv1.input.IPotInputContainer
import com.example.pipboyv1.input.MockPotInputContainer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        tabLayout = findViewById(R.id.topNavTabLayout)
        viewPager2 = findViewById(R.id.topNavViewPager2)
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        setupTopNav()
        setupMockPot()

        setupPotInputs(forceMock = true) // Keeping this true for Mon Jun 26's demo
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
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = adapter.getFragmentTitle(position)
        }
        tabLayoutMediator.attach()
    }

    private fun setupMockPot() {
        mockPotMenuBtn = findViewById(R.id.potMenuButton)
        mockPotMenuBtn.setOnClickListener {
            // TODO: display a pop up menu
            val mockPotMenu: PopupMenu = PopupMenu(this, mockPotMenuBtn)
            mockPotMenu.menuInflater.inflate(R.menu.menu_mockpot, mockPotMenu.menu)
            mockPotMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.addPotValItem -> {
                        Toast.makeText(applicationContext, "Added to POT Value", Toast.LENGTH_SHORT)
                            .show()
//                        PotValueDialogDisplay.displayMockPotValueDialog(PotValueDialogDisplay.MOCK_POT_ADD)
                    }
                    R.id.subPotValItem -> {
                        Toast.makeText(
                            applicationContext,
                            "Subtracted from POT Value",
                            Toast.LENGTH_SHORT
                        ).show()
//                        PotValueDialogDisplay.displayMockPotValueDialog(PotValueDialogDisplay.MOCK_POT_SUB)
                    }
                    R.id.changePotValItem -> {
                        Toast.makeText(applicationContext, "Changing POT value", Toast.LENGTH_SHORT)
                            .show()
//                        PotValueDialogDisplay.displayMockPotValueDialog(PotValueDialogDisplay.MOCK_POT_CHANGE)
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
