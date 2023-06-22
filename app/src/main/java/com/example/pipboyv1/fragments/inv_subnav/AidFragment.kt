package com.example.pipboyv1.fragments.inv_subnav

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
import com.example.pipboyv1.helpers.populateDisplayItem
import com.example.pipboyv1.input.PositionChangeListener

class AidFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter

    private var position: Int = 0
    private var HP: String = "HP"
    private var RADS: String = "Rads"
    private var WEIGHT: String = "Weight"
    private var VALUE: String = "Value"

    private val imgDimension: Int = 250
    private val selectionItems: MutableList<SelectionItem> = mutableListOf(
        SelectionItem(textLeft="Blamco Brand Mac and Cheese", data= SelectionItemData(imageId=R.drawable.aid_blamco, attributes= mapOf(
            HP to "+20",
            RADS to "+6",
            WEIGHT to "0.1",
            VALUE to "10",
        ))),
        SelectionItem(textLeft="Carrot", data= SelectionItemData(imageId=R.drawable.aid_carrot, attributes= mapOf(
            HP to "+10",
            RADS to "+3",
            WEIGHT to "0.1",
            VALUE to "3",
        ))),
        SelectionItem(textLeft="Corn", data= SelectionItemData(imageId=R.drawable.aid_corn, attributes= mapOf(
            HP to "+10",
            RADS to "+3",
            WEIGHT to "0.1",
            VALUE to "6",
        ))),
        SelectionItem(textLeft="Cram", data= SelectionItemData(imageId=R.drawable.aid_cram, attributes= mapOf(
            HP to "+25",
            RADS to "+5",
            WEIGHT to "0.5",
            VALUE to "25",
        ))),
        SelectionItem(textLeft="Iguana on a Stick", data= SelectionItemData(imageId=R.drawable.aid_iguana, attributes= mapOf(
            HP to "+40",
            WEIGHT to "0.1",
            VALUE to "33",
        ))),
        SelectionItem(textLeft="InstaMash", data= SelectionItemData(imageId=R.drawable.aid_instamash, attributes= mapOf(
            HP to "+20",
            RADS to "+7",
            WEIGHT to "0.5",
            VALUE to "20",
        ))),
    )

    inner class PositionListener : PositionChangeListener {
        override fun onValueChange(newPosition: Int) {
            position = newPosition
            populateDisplayItem(selectionItems[position].data, view, context, imgDimension)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems)
        adapter.setHasStableIds(true)
        adapter.setValueChangeListener(PositionListener())

        return inflater.inflate(R.layout.fragment_aid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectionItems.sortBy { it.textLeft }

        recyclerView= view.findViewById(R.id.invAidSelectorRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        // Populate display panel
        populateDisplayItem(selectionItems[position].data, view, context, imgDimension)
    }


}