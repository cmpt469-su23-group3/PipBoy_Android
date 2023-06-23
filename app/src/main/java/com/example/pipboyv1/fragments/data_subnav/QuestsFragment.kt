package com.example.pipboyv1.fragments.data_subnav

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
import com.example.pipboyv1.classes.SelectionItemData
import com.example.pipboyv1.helpers.populateDisplayItemQuest
import com.example.pipboyv1.input.PositionChangeListener

class QuestsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter

    private var position: Int = 0


    private val imgDimension: Int = 250
    private val selectionItems: MutableList<SelectionItem> = mutableListOf(

        SelectionItem(textLeft="Dangerous Minds", data= SelectionItemData(
            description="There's a woman named Doctor Amari who might be able to extract the secrets of entering the Institute from Kellogg's brain.",
            attributes= mapOf(
                "Talk to Doctor Amari" to "",
            ))),
        SelectionItem(textLeft="Out of Time", data= SelectionItemData(
            description="I haven't found Shaun yet, but I did meet a desperate man in need of help What kind of world have I woken up to?", attributes= mapOf(
                "Investigate Concord" to "",
                "Kill the Insects" to "",
                "Search the neighbourhood with Codsworth" to "",
                "Talk to Codsworth" to "",
                "Go home" to "",
                "Exit Vault 111" to "",
            ))),

    )

    inner class PositionListener : PositionChangeListener {
        override fun onValueChange(newPosition: Int) {
            position = newPosition
            populateDisplayItemQuest(selectionItems[position].data, view, context, imgDimension)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems)
        adapter.setHasStableIds(true)
        adapter.setValueChangeListener(PositionListener())

        return inflater.inflate(R.layout.fragment_quests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectionItems.sortBy { it.textLeft }
        recyclerView = view.findViewById(R.id.dataQuestsSelectorRecyclerView) as RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)


        // Populate display panel
        populateDisplayItemQuest(selectionItems[position].data, view, context, imgDimension)
    }

}