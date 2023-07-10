package com.example.pipboyv1.fragments.topnav

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pipboyv1.R
import com.example.pipboyv1.input.PotIDs
import com.example.pipboyv1.input.PotInputListener

class DebugFragment : Fragment(), PotInputListener {

    private lateinit var debugInfo: TextView
    private val potInputs: MutableMap<Int, String> = mutableMapOf(
        PotIDs.POT_0 to "N/A",
        PotIDs.POT_1 to "N/A",
        PotIDs.POT_2 to "N/A",
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_debug, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        debugInfo = view.findViewById(R.id.debugInfo)
        refreshDebugInfo()
    }
    
    @SuppressLint("SetTextI18n")
    private fun refreshDebugInfo() {
        val potText = potInputs.entries.joinToString(separator = "\n") { (index, value) ->
            "Pot $index: $value"
        }
        
        debugInfo.text = """
Debug pot values:
$potText
        """.trimIndent()
        debugInfo.invalidate()
    }  

    override fun onInputChange(potIndex: Int, percentageValue: Float) {
//        potInputs[potIndex] = percentageValue
//        
//        this.activity?.runOnUiThread {
//            refreshDebugInfo()
//        }
    }

    override fun onInputChangeRaw(potIndex: Int, value: String) {
        potInputs[potIndex] = value

        this.activity?.runOnUiThread {
            refreshDebugInfo()
        }
    }

    override fun onMoveLeft(potIndex: Int, percentageValue: Float) {
    }

    override fun onMoveRight(potIndex: Int, percentageValue: Float) {
    }
}