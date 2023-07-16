package com.example.pipboyv1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.pipboyv1.classes.HolotapeContainer
import com.example.pipboyv1.fragments.MainFragment
import com.example.pipboyv1.fragments.TapeFragment
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var nfcAdapter: NfcAdapter

    private val mainFragment = MainFragment()
    private val holotapeFragment = TapeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Use main fragment
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, mainFragment).commit()

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        handleIntent(intent)
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
        Log.i("handleHolotape", "Holotape with ID $holotapeID scanned")

        if (holotapeID == 3) {
            val holotape = HolotapeContainer.holotapes.first { it.id == holotapeID }
            val packageName = holotape.attributes["packageName"]!!

            try {
                val intent = packageManager.getLaunchIntentForPackage(packageName) ?: throw NameNotFoundException()
                window.context.startActivity(intent)
            }
            catch(e: NameNotFoundException) {
                Log.e("handleHolotape", "DOOM is not installed :(")
            }
            catch(e: ActivityNotFoundException) {
                Log.e("handleHolotape", "DOOM could not be started :(")
            }
            return
        }

        if (HolotapeContainer.holotapes.none { it.id == holotapeID }) {
            Log.e("handleHolotape", "Holotape does not exist")
            return
        }

        // Switch to holotape fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, holotapeFragment).addToBackStack(null).commit()
        holotapeFragment.onHolotapeLoaded(holotapeID)
    }
}
