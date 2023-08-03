package com.example.pipboyv1.fragments

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.pipboyv1.MainActivity
import com.example.pipboyv1.MainTabInputListener
import com.example.pipboyv1.R
import com.example.pipboyv1.adapters.ViewPagerAdapter
import com.example.pipboyv1.ble.BlePotInputContainer
import com.example.pipboyv1.fragments.topnav.DataFragment
import com.example.pipboyv1.fragments.topnav.DebugFragment
import com.example.pipboyv1.fragments.topnav.InvFragment
import com.example.pipboyv1.fragments.topnav.MapFragment
import com.example.pipboyv1.fragments.topnav.RadioFragment
import com.example.pipboyv1.fragments.topnav.StatFragment
import com.example.pipboyv1.input.IPotInputContainer
import com.example.pipboyv1.mockBle.MockPotDialog
import com.example.pipboyv1.mockBle.MockPotInputContainer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope

class MainFragment : Fragment() {
    companion object {
        const val BLE_REQUEST_CODE: Int = 1
        private const val SHOW_DEBUG_TAB: Boolean = true
    }

    private lateinit var mockPotMenuBtn: Button

    private lateinit var potInputContainer: IPotInputContainer
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (requireActivity().getSystemService(Context.BLUETOOTH_SERVICE) as android.bluetooth.BluetoothManager).adapter
    }

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.topNavTabLayout) ?: return
        viewPager2 = view.findViewById(R.id.topNavViewPager2) ?: return
        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)

        mockPotMenuBtn = view.findViewById(R.id.potMenuButton) ?: return
        mockPotMenuBtn.visibility = View.INVISIBLE

        setupTopNav()
        setupPotInputs()

        potInputContainer.addListener(adapter.getFragmentByClass<RadioFragment>())
        if (SHOW_DEBUG_TAB) {
            potInputContainer.addListener(adapter.getFragmentByClass<DebugFragment>())
        }

        potInputContainer.addListener(MainTabInputListener(viewPager2, adapter))
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
            tab.view.isClickable = false
        }
        tabLayoutMediator.attach()
    }

    private fun setupMockPot() {
        mockPotMenuBtn.setOnClickListener {
            val mockPotMenu = PopupMenu(context, mockPotMenuBtn)
            mockPotMenu.menuInflater.inflate(R.menu.menu_mockpot, mockPotMenu.menu)
            mockPotMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.addPotValItem -> {
                        MockPotDialog.displayPotIndexDialog(
                            requireContext(), MockPotDialog.PotAction.POT_ADD,
                            potInputContainer as MockPotInputContainer
                        )
                    }

                    R.id.subPotValItem -> {
                        MockPotDialog.displayPotIndexDialog(
                            requireContext(), MockPotDialog.PotAction.POT_SUB,
                            potInputContainer as MockPotInputContainer
                        )
                    }
                }
                true
            }
            mockPotMenu.show()
        }
        mockPotMenuBtn.visibility = View.VISIBLE
    }

    private fun setupPotInputs(forceMock: Boolean = false) {
        val blAdapter = if (forceMock) null else bluetoothAdapter
        if (blAdapter != null) {
            val bluetoothScope: CoroutineScope = lifecycleScope

            potInputContainer = BlePotInputContainer(activity as MainActivity, blAdapter, bluetoothScope)

            requestBlePermissions()
        } else {
            potInputContainer = MockPotInputContainer()
            setupMockPot()
            requireActivity().runOnUiThread {
                Toast.makeText(requireActivity().applicationContext, "Note: Using mocked pot. inputs", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun requestBlePermissions() {
        Log.i("setupPotInputs", "Requesting permissions")

        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        ActivityCompat.requestPermissions(
            requireActivity(),
            permissions,
            BLE_REQUEST_CODE
        )
    }

    fun onBlePermissionGranted() {
        Log.i("setupPotInputs", "Permissions granted")
        (potInputContainer as? BlePotInputContainer)?.onPermissionsGranted()
    }
}