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

class WeaponsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter
    private var position: Int = 0
    private val imgDimension: Int = 250
    private val selectionItems: List<SelectionItem> = listOf(
        SelectionItem(textLeft="10mm Pistol", data= SelectionItemData(imageId=R.drawable.weapon_10mm_pistol, attributes= mapOf(
            "Damage" to "18",
            "Fire Rate" to "46",
            "Range" to "83",
            "Accuracy" to "60",
            "Weight" to "3.5",
            "Value" to "50",
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

        return inflater.inflate(R.layout.fragment_weapons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.invWeaponsSelectorRecyclerView) as RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        // Populate display panel
        populateDisplayItem(selectionItems[position].data, view, context, imgDimension)
    }
}