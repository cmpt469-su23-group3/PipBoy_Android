package com.example.pipboyv1.fragments.topnav

import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
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
        private const val LOG_TAG: String = "RadioFragment"

        private const val RADIO_NAME_CLASSICAL = "Classical Radio"
        private const val RADIO_NAME_DIAMOND = "Diamond City Radio"
//        private const val RADIO_NAME_NUKA = "Nuka-Cola Family Radio"
//        private const val RADIO_NAME_FREEDOM = "Radio Freedom"
//        private const val RADIO_NAME_VAULT = "Vault 88 Radio Beacon"
//        private const val RADIO_NAME_CIVIL_ALERT = "Civil Alert System Broadcast"
    }

    inner class RadioStationData(_name: String, _startFreq: Float, _endFreq: Float, _currentTrack: Int, _resIdList: MutableList<Int>) {
        val name = _name
        val startFreq = _startFreq
        val endFreq = _endFreq
        val numTracks = _resIdList.size
        val resIdList = _resIdList
        var currentTrack = _currentTrack
    }

    private var radioInitialized: Boolean = false

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter

    private var position: AtomicInteger = AtomicInteger()

    private var inRadioView: Boolean = false
    private var isPlayingRadioStation: Boolean = false

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
    )

    private val radioStationDataList: MutableList<RadioStationData> = mutableListOf(
        RadioStationData(RADIO_NAME_CLASSICAL, 0.05f, 0.15f, 0, mutableListOf(R.raw.mus_institute_strauss_bluedanubewaltz)),
        RadioStationData(RADIO_NAME_DIAMOND, 0.44f, 0.54f, 0, mutableListOf(R.raw.mus_radio_diamond_theinkspots_idontwanttoset)),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        staticMediaPlayer = MediaPlayer.create(this.context, R.raw.static_white_noise)
        staticMediaPlayer.isLooping = true
        staticMediaPlayer.start()

        radioMediaPlayer = MediaPlayer()

        setMediaPlayerVolume(0.0f, 0.0f)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems, requireContext(), -1)
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
        radioInitialized = true
//        Log.i(LOG_TAG, "radioFragment UI is now initialized")
    }

    override fun onInputChange(potIndex: Int, percentageValue: Float) {
        if (radioInitialized) {
//            Log.i(LOG_TAG, "onInputChange triggered on RadioFragment")
            when (potIndex) {
                1 -> {
                    changeFrequency(percentageValue)
                }

                2 -> {
                    changeVolume(percentageValue)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inRadioView = true
        setMediaPlayerVolume(staticVolume, radioVolume)
        Log.i(LOG_TAG, "onResume | staticVolume: $staticVolume, radioVolume: $radioVolume")
    }

    override fun onPause() {
        super.onPause()
        inRadioView = false
        setMediaPlayerVolume(0.0f, 0.0f)
        Log.i(LOG_TAG, "onPause called.")
    }

    private fun changeVolume(potPercentageValue: Float) {
        currentVolumeMultiplier = potPercentageValue
        radioVolume = calculateMediaPlayerVolume(radioClarityPercentage, currentVolumeMultiplier)
        staticVolume = calculateMediaPlayerVolume(staticClarityPercentage, currentVolumeMultiplier)
        if (inRadioView) {
            setMediaPlayerVolume(staticVolume, radioVolume)
        }
    }

    private fun changeFrequency(potPercentageValue: Float) {
        currentFrequency = potPercentageValue
        var inRadioFreqRange: Boolean = false
        var selectedStartFreq: Float = -1.0f
        var selectedEndFreq: Float = -1.0f
        var selectedRadioName: String = ""
        var selectedRadioPosition: Int = 0

        for (radioStationData in radioStationDataList) {
            if (currentFrequency in radioStationData.startFreq .. radioStationData.endFreq) {
                inRadioFreqRange = true
                selectedRadioName = radioStationData.name
                selectedStartFreq = radioStationData.startFreq
                selectedEndFreq = radioStationData.endFreq
                Log.i(LOG_TAG, "current frequency is $currentFrequency in radio $selectedRadioName")

                if (!isPlayingRadioStation) {
                    startRadioStationAudio(radioStationData.resIdList)
                    isPlayingRadioStation = true
                }
                break
            }
            selectedRadioPosition++
        }

        if (!inRadioFreqRange) {
            staticVolume = 1.0f * currentVolumeMultiplier
            radioVolume = 0.0f

            if (isPlayingRadioStation) {
                radioMediaPlayer.stop()
                radioMediaPlayer.reset()
                isPlayingRadioStation = false
            }

            clearUiRadioHighlight()
        }
        else {
            val fullClarityFreq: Float = (selectedStartFreq + selectedEndFreq) / 2
            radioClarityPercentage = calculateClarity(currentFrequency, fullClarityFreq, selectedStartFreq, selectedEndFreq)
            staticClarityPercentage = 1.0f - radioClarityPercentage
            radioVolume = calculateMediaPlayerVolume(radioClarityPercentage, currentVolumeMultiplier)
            staticVolume = calculateMediaPlayerVolume(staticClarityPercentage, currentVolumeMultiplier)

            highlightRadioUiItem(selectedRadioPosition)
        }

        if (inRadioView) {
            setMediaPlayerVolume(staticVolume, radioVolume)
        }
    }

    /* Private helper functions */

    private fun highlightRadioUiItem(radioSelectionPosition: Int) {
        adapter.handleSelectionItemClick(radioSelectionPosition)
    }

    private fun clearUiRadioHighlight() {
        adapter.deselectAll()
    }

    private fun calculateClarity(selectedFrequency: Float, fullClarityFreq: Float,
                                 selectedStartFreq: Float, selectedEndFreq: Float ): Float {
        val selectedFreqLeftOfMiddle: Boolean = selectedFrequency < fullClarityFreq
        var distanceToMiddle: Float
        var distanceToSelectedFreq: Float

        if (selectedFreqLeftOfMiddle) {
            distanceToMiddle = abs(fullClarityFreq - selectedStartFreq)
            distanceToSelectedFreq = abs(selectedFrequency - selectedStartFreq)
        } else {
            distanceToMiddle = abs(fullClarityFreq - selectedEndFreq)
            distanceToSelectedFreq = abs(selectedFrequency - selectedEndFreq)
        }

        return distanceToSelectedFreq / distanceToMiddle
    }

    private fun startRadioStationAudio(songIdList: MutableList<Int>) {
        val songDurationList: MutableList<Int> = mutableListOf()
        var songListTotalTime: Long = 0
        for (songId in songIdList) {
            val mp: MediaPlayer = MediaPlayer.create(context, songId)
            songListTotalTime += mp.duration
            songDurationList.add(mp.duration)
        }
        var radioTimeOffset: Long = SystemClock.elapsedRealtime() % songListTotalTime
        var songIndex: Int = 0
        for (songDuration in songDurationList) {
            if (radioTimeOffset - songDuration > 0) {
                radioTimeOffset -= songDuration
                songIndex++
            } else {
                break
            }
        }

        val currentTrackId: Int = songIdList[songIndex]
        val songAfd: AssetFileDescriptor =
            context?.resources?.openRawResourceFd(currentTrackId) ?: return
        radioMediaPlayer.setDataSource(songAfd.fileDescriptor, songAfd.startOffset, songAfd.length)
        radioMediaPlayer.prepare()
        radioMediaPlayer.seekTo(radioTimeOffset.toInt())
        radioMediaPlayer.start()
    }

    private inline fun calculateMediaPlayerVolume(clarityPercentage: Float, volumeMultiplier: Float): Float {
        return 1.0f * clarityPercentage * volumeMultiplier
    }

    private fun setMediaPlayerVolume(_staticVolume: Float, _radioVolume: Float) {
        staticMediaPlayer.setVolume(_staticVolume, _staticVolume)
        radioMediaPlayer.setVolume(_radioVolume, _radioVolume)
    }

    /* Unimplemented inherited methods */

    override fun onMoveLeft(potIndex: Int, percentageValue: Float) {

    }

    override fun onMoveRight(potIndex: Int, percentageValue: Float) {

    }
}