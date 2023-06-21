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

class ApparelFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter

    private var position: Int = 0
    private var DAMAGE: String = "DMG Resist"
    private var ENERGY_DAMAGE: String = "Energy SMG Resist"
    private var WEIGHT: String = "Weight"
    private var VALUE: String = "Value"

    private val imgDimension: Int = 250
    private val selectionItems: MutableList<SelectionItem> = mutableListOf(
        SelectionItem(textLeft="Heavy Combat Armor Right Arm", data= SelectionItemData(imageId=R.drawable.armor_image, attributes= mapOf(
            DAMAGE to "17",
            ENERGY_DAMAGE to "17",
            WEIGHT to "7",
            VALUE to "145",
        ))),
        SelectionItem(textLeft="Heavy Combat Armor Right Leg", data= SelectionItemData(imageId=R.drawable.armor_image, attributes= mapOf(
            DAMAGE to "16",
            ENERGY_DAMAGE to "16",
            WEIGHT to "7",
            VALUE to "185",
        ))),
        SelectionItem(textLeft="Heavy Combat Armor Left Arm", data= SelectionItemData(imageId=R.drawable.armor_image, attributes= mapOf(
            DAMAGE to "17",
            ENERGY_DAMAGE to "17",
            WEIGHT to "7",
            VALUE to "145",
        ))),
        SelectionItem(textLeft="Heavy Combat Armor Left Leg", data= SelectionItemData(imageId=R.drawable.armor_image, attributes= mapOf(
            DAMAGE to "16",
            ENERGY_DAMAGE to "16",
            WEIGHT to "7",
            VALUE to "185",
        ))),
        SelectionItem(textLeft="Heavy Combat Armor Chest Piece", data= SelectionItemData(imageId=R.drawable.armor_image, attributes= mapOf(
            DAMAGE to "35",
            ENERGY_DAMAGE to "35",
            WEIGHT to "15.5",
            VALUE to "220",
        ))),
        SelectionItem(textLeft="Combat Armor Helmet", data= SelectionItemData(imageId=R.drawable.armor_image, attributes= mapOf(
            DAMAGE to "10",
            ENERGY_DAMAGE to "10",
            WEIGHT to "4",
            VALUE to "25",
        )))
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

        return inflater.inflate(R.layout.fragment_apparel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectionItems.sortBy { it.textLeft }

        recyclerView = view.findViewById(R.id.invApparelSelectorRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        // Populate display panel
        populateDisplayItem(selectionItems[position].data, view, context, imgDimension)
    }

}