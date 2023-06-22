package com.example.pipboyv1

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.pipboyv1.fragments.topnav.DataFragment
import com.example.pipboyv1.fragments.topnav.InvFragment
import com.example.pipboyv1.fragments.topnav.MapFragment
import com.example.pipboyv1.fragments.topnav.RadioFragment
import com.example.pipboyv1.fragments.topnav.StatFragment
import com.example.pipboyv1.adapters.ViewPagerAdapter
import com.example.pipboyv1.classes.Holotape
import com.example.pipboyv1.input.IPotInputContainer
import com.example.pipboyv1.input.MockPotInputContainer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONObject

class FullscreenActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var potInputContainer: IPotInputContainer
    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        potInputContainer = MockPotInputContainer() // FIXME use BlePotInputContainer eventually

        tabLayout = findViewById(R.id.topNavTabLayout)
        viewPager2 = findViewById(R.id.topNavViewPager2)
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        setupTopNav()
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
        var holotape = Holotape(
            payload.get(getString(R.string.type)) as String,
            payload.get(getString(R.string.name)) as String
        )

        // TODO: Do something with holotape (ex. complete quest, add item to inv, etc.)
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
}