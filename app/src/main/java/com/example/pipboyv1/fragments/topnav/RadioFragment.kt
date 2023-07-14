package com.example.pipboyv1.fragments.topnav

import android.media.MediaPlayer
import android.os.Bundle
import android.text.Selection
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.adapters.SelectionItemAdapter
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.classes.SelectionItemData
import com.example.pipboyv1.input.PotInputListener
import com.example.pipboyv1.input.SelectionItemInputListener
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates

class RadioFragment : Fragment(), PotInputListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter

    private var position: AtomicInteger = AtomicInteger()

//    private val imgDimension: Int = 250
    private val START_FREQ_VAL: String = "Start Freq"
    private val END_FREQ_VAL: String = "End Freq"

    private var currentFrequency: Float = 0.0f
    private var currentVolume: Float = 50.0f

    private lateinit var classicalMediaPlayer: MediaPlayer
    private lateinit var diamondMediaPlayer: MediaPlayer
    private lateinit var nukaMediaPlayer: MediaPlayer
    private lateinit var freedomMediaPlayer: MediaPlayer
    private lateinit var vaultMediaPlayer: MediaPlayer

    private val selectionItems: MutableList<SelectionItem> = mutableListOf(
        SelectionItem(textLeft="Classical Radio", data=SelectionItemData(attributes=mapOf(
            START_FREQ_VAL to "5.0",
            END_FREQ_VAL to "8.0"
        ))),
        SelectionItem(textLeft="Diamond City Radio", data=SelectionItemData(attributes=mapOf(
            START_FREQ_VAL to "18.0",
            END_FREQ_VAL to "21.0"
        ))),
        SelectionItem(textLeft="Nuka-Cola Family Radio", data=SelectionItemData(attributes=mapOf(
            START_FREQ_VAL to "33.0",
            END_FREQ_VAL to "36.0"
        ))),
        SelectionItem(textLeft="Radio Freedom", data=SelectionItemData(attributes=mapOf(
            START_FREQ_VAL to "44.0",
            END_FREQ_VAL to "47.0"
        ))),
        SelectionItem(textLeft="Vault 88 Radio Beacon", data=SelectionItemData(attributes=mapOf(
            START_FREQ_VAL to "61.0",
            END_FREQ_VAL to "64.0"
        ))),
        SelectionItem(textLeft="Civil Alert System Broadcast", data=SelectionItemData(attributes=mapOf(
            START_FREQ_VAL to "73.0",
            END_FREQ_VAL to "76.0"
        )))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: init the five MediaPlayers here for the radios
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems)
        adapter.setHasStableIds(true)
        return inflater.inflate(R.layout.fragment_radio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setValueChangeListener(object : SelectionItemInputListener {
            override fun onValueChange(newPosition: Int) {
                position.set(newPosition)
            }
        })

        recyclerView = view.findViewById(R.id.radioSelectorRecyclerView) as RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun onInputChange(potIndex: Int, percentageValue: Float) {
        when(potIndex) {
            1 -> { changeFrequency(percentageValue) }
            2 -> { changeVolume(percentageValue) }
        }
    }

    override fun onMoveLeft(potIndex: Int, percentageValue: Float) {
        TODO("Not yet implemented")
    }

    override fun onMoveRight(potIndex: Int, percentageValue: Float) {
        TODO("Not yet implemented")
    }

    private fun changeFrequency(percentageValue: Float) {
        currentFrequency = percentageValue
    }

    private fun changeVolume(percentageValue: Float) {
        currentVolume = percentageValue
    }
}