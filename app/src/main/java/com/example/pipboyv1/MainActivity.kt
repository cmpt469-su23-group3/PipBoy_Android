package com.example.pipboyv1

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.pipboyv1.fragments.topnav.DataFragment
import com.example.pipboyv1.fragments.topnav.InvFragment
import com.example.pipboyv1.fragments.topnav.MapFragment
import com.example.pipboyv1.fragments.topnav.RadioFragment
import com.example.pipboyv1.fragments.topnav.StatFragment
import com.example.pipboyv1.adapters.ViewPagerAdapter
import com.example.pipboyv1.ble.BlePotInputContainer
import com.example.pipboyv1.classes.HolotapeContainer
import com.example.pipboyv1.fragments.topnav.DebugFragment
import com.example.pipboyv1.mockBle.MockPotDialog
import com.example.pipboyv1.input.IPotInputContainer
import com.example.pipboyv1.mockBle.MockPotInputContainer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        private const val BLE_REQUEST_CODE: Int = 1
        private const val SHOW_DEBUG_TAB: Boolean = true
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
    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        tabLayout = findViewById(R.id.topNavTabLayout)
        viewPager2 = findViewById(R.id.topNavViewPager2)
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        mockPotMenuBtn = findViewById(R.id.potMenuButton)
        mockPotMenuBtn.visibility = View.INVISIBLE

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        setupTopNav()
        setupPotInputs()
        handleIntent(intent)

        if (SHOW_DEBUG_TAB) {
            potInputContainer.addListener(adapter.getFragmentByClass<DebugFragment>())
        }
        
        potInputContainer.addListener(MainTabInputListener(viewPager2, adapter))
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
        val holotapeID = payload.get("id") as Int
        Log.e("handleHolotape", "Holotape with ID $holotapeID scanned")

        if (HolotapeContainer.holotapes.find { it.id == holotapeID } == null) {
            Log.e("handleHolotape", "Holotape does not exist")
            return
        }

        val intent = Intent(window.context, HolotapeActivity::class.java)
        intent.putExtra("holotapeID", holotapeID)

        finish()
        startActivity(intent)
    }

    private fun setupTopNav() {
        val topNavTabs = mutableMapOf(
            getString(R.string.stat_button) to StatFragment(),
            getString(R.string.inv_button) to InvFragment(),
            getString(R.string.data_button) to DataFragment(),
            getString(R.string.map_button) to MapFragment(),
            getString(R.string.radio_button) to RadioFragment(),
        )

        if (SHOW_DEBUG_TAB) {
            topNavTabs += "DEBUG" to DebugFragment()
        }

        topNavTabs.forEach { (title, fragment) ->
            adapter.addFragment(fragment, title)
        }

        viewPager2.adapter = adapter
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = adapter.getFragmentTitle(position)
        }
        tabLayoutMediator.attach()
    }

    private fun setupMockPot() {
        mockPotMenuBtn.setOnClickListener {
            val mockPotMenu: PopupMenu = PopupMenu(this, mockPotMenuBtn)
            mockPotMenu.menuInflater.inflate(R.menu.menu_mockpot, mockPotMenu.menu)
            mockPotMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.addPotValItem -> {
                        MockPotDialog.displayPotIndexDialog(this, MockPotDialog.PotAction.POT_ADD,
                            potInputContainer as MockPotInputContainer
                        )
                    }
                    R.id.subPotValItem -> {
                        MockPotDialog.displayPotIndexDialog(this, MockPotDialog.PotAction.POT_SUB,
                            potInputContainer as MockPotInputContainer
                        )
                    }
                }
                true
            })
            mockPotMenu.show()
        }
        mockPotMenuBtn.visibility = View.VISIBLE
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            BLE_REQUEST_CODE -> {
                Log.i("setupPotInputs", "Permissions granted")
                (potInputContainer as? BlePotInputContainer)?.onPermissionsGranted()
            }

            else -> recreate()
        }
    }
}
