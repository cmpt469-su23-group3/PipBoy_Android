package com.example.pipboyv1.fragments.topnav

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.adapters.SelectionItemAdapter
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.input.PotInputListener
import com.example.pipboyv1.input.SelectionItemInputListener
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

class RadioFragment : Fragment(), PotInputListener {

    companion object {
        private const val RADIO_NAME_CLASSICAL = "Classical Radio"
        private const val RADIO_NAME_DIAMOND = "Diamond City Radio"
//        private const val RADIO_NAME_NUKA = "Nuka-Cola Family Radio"
//        private const val RADIO_NAME_FREEDOM = "Radio Freedom"
//        private const val RADIO_NAME_VAULT = "Vault 88 Radio Beacon"
//        private const val RADIO_NAME_CIVIL_ALERT = "Civil Alert System Broadcast"
    }

    inner class RadioData(_name: String, _startFreq: Float, _endFreq: Float, _currentTrack: Int, _resIdList: MutableList<Int>) {
        val NAME = _name
        val START_FREQ = _startFreq
        val END_FREQ = _endFreq
        val NUM_TRACKS = _resIdList.size
        var currentTrack = _currentTrack
        var resIdList = _resIdList
    }

    private var inRadioView: Boolean = false

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter

    private var position: AtomicInteger = AtomicInteger()

//    private val imgDimension: Int = 250

    // Values are from [0.0f - 1.0f]
    private var currentFrequency: Float = 0.0f
    private var currentVolumeMultiplier: Float = 1.0f
    private var radioClarityPercentage: Float = 0.0f
    private var staticClarityPercentage: Float = 1.0f
    private var radioVolume: Float = 0.0f
    private var staticVolume: Float = 0.0f

    private lateinit var staticMediaPlayer: MediaPlayer
    private lateinit var radioMediaPlayer: MediaPlayer

    private val selectionItems: MutableList<SelectionItem> = mutableListOf(
        SelectionItem(textLeft=RADIO_NAME_CLASSICAL),
        SelectionItem(textLeft=RADIO_NAME_DIAMOND),
//        SelectionItem(textLeft=RADIO_NAME_NUKA),
//        SelectionItem(textLeft=RADIO_NAME_FREEDOM),
//        SelectionItem(textLeft=RADIO_NAME_VAULT),
//        SelectionItem(textLeft=RADIO_NAME_CIVIL_ALERT)
    )

    private val radioDataList: MutableList<RadioData> = mutableListOf(
        RadioData(RADIO_NAME_CLASSICAL, 5.0f, 8.0f, 0, mutableListOf(R.raw.MUS_Institute_Strauss_BlueDanubeWaltz)),
        RadioData(RADIO_NAME_DIAMOND, 18.0f, 21.0f, 0, mutableListOf(R.raw.MUS_Radio_Diamond_TheInkSpots_IDontWantToSet)),
//        RadioData(RADIO_NAME_NUKA, 33.0f, 36.0f),
//        RadioData(RADIO_NAME_FREEDOM, 44.0f, 47.0f),
//        RadioData(RADIO_NAME_VAULT, 61.0f, 64.0f),
//        RadioData(RADIO_NAME_CIVIL_ALERT, 73.0f, 76.0f)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: init the MediaPlayers here for the radios
        staticMediaPlayer = MediaPlayer.create(this.context, R.raw.static_white_noise)
        staticMediaPlayer.isLooping = true
        staticMediaPlayer.setVolume(0.0f, 0.0f)
        staticMediaPlayer.prepare()
        staticMediaPlayer.start()

        radioMediaPlayer = MediaPlayer()

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
//        TODO("Not yet implemented")
    }

    override fun onMoveRight(potIndex: Int, percentageValue: Float) {
//        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        inRadioView = true
        setMediaPlayerVolume(staticVolume, radioVolume)
    }

    override fun onPause() {
        super.onPause()
        inRadioView = false
        setMediaPlayerVolume(0.0f, 0.0f)
    }

    private fun changeFrequency(percentageValue: Float) {
        currentFrequency = percentageValue
        var inRadioFreqRange: Boolean = false
        var selectedStartFreq: Float = -1.0f
        var selectedEndFreq: Float = -1.0f
        var selectedRadioName: String = ""

        for (radioData in radioDataList) {
            if (currentFrequency in radioData.START_FREQ .. radioData.END_FREQ) {
                inRadioFreqRange = true
                selectedRadioName = radioData.NAME
                selectedStartFreq = radioData.START_FREQ
                selectedEndFreq = radioData.END_FREQ

                // TODO: play/change the audio file for the radio station in the frequency
                val currentTrackId: Int = radioData.resIdList[radioData.currentTrack]
                val afd: AssetFileDescriptor =
                    context?.resources?.openRawResourceFd(currentTrackId) ?: return

                radioMediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)

                break
            }
        }

        fun calculateClarity(
            fullClarityFreq: Float,
            selectedStartFreq: Float,
            selectedEndFreq: Float
        ): Float {
            val selectedFreqLeftOfMiddle: Boolean = currentFrequency < fullClarityFreq
            var distanceToMiddle: Float
            var distanceToSelectedFreq: Float

            if (selectedFreqLeftOfMiddle) {
                distanceToMiddle = abs(fullClarityFreq - selectedStartFreq)
                distanceToSelectedFreq = abs(currentFrequency - selectedStartFreq)
            } else {
                distanceToMiddle = abs(fullClarityFreq - selectedEndFreq)
                distanceToSelectedFreq = abs(currentFrequency - selectedEndFreq)
            }

            return distanceToSelectedFreq / distanceToMiddle
        }

        if (!inRadioFreqRange) {
            // TODO: unhighlight everything in the selection menu
            staticVolume = 1.0f * currentVolumeMultiplier
            radioVolume = 0.0f
        }
        else {
            // TODO: highlight the UI element with the same name
            val fullClarityFreq: Float = (selectedStartFreq + selectedEndFreq) / 2
            radioClarityPercentage = calculateClarity(fullClarityFreq, selectedStartFreq, selectedEndFreq)
            staticClarityPercentage = 1.0f - radioClarityPercentage
            radioVolume = 1.0f * radioClarityPercentage * currentVolumeMultiplier
            staticVolume = 1.0f * staticClarityPercentage * currentVolumeMultiplier
        }

        if (inRadioView) {
            setMediaPlayerVolume(staticVolume, radioVolume)
        }
    }

    private fun changeVolume(percentageValue: Float) {
        currentVolumeMultiplier = percentageValue
        radioVolume = 1.0f * radioClarityPercentage * currentVolumeMultiplier
        staticVolume = 1.0f * staticClarityPercentage * currentVolumeMultiplier
        if (inRadioView) {
            setMediaPlayerVolume(staticVolume, radioVolume)
        }
    }

    private fun setMediaPlayerVolume(_staticVolume: Float, _radioVolume: Float) {
        staticMediaPlayer.setVolume(_staticVolume, _staticVolume)
        radioMediaPlayer.setVolume(_radioVolume, _radioVolume)
    }
}