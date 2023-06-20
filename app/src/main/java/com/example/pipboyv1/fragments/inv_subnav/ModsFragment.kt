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

class ModsFragment : Fragment() {
    private var WEIGHT: String = "Weight"
    private var VALUE: String = "Value"

    private val imgDimension: Int = 250
    private val selectionItems: MutableList<SelectionItem> = mutableListOf(
        SelectionItem(textLeft="10mm Standard Receiver", data= SelectionItemData(imageId=R.drawable.mods_icon, attributes= mapOf(
            WEIGHT to "0.5",
            VALUE to "20",
        ))),
        SelectionItem(textLeft="Pipe Gun Standard Receiver", data= SelectionItemData(imageId=R.drawable.mods_icon, attributes= mapOf(
            WEIGHT to "0.5",
            VALUE to "5",
        ))),

    )
    private var position: Int = 0

    inner class PositionListener : PositionChangeListener {
        override fun onValueChange(newPosition: Int) {
            position = newPosition
            populateDisplayItem(selectionItems[position].data, requireView(), requireContext(), imgDimension)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mods, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectionItems.sortBy { it.textLeft }

        val recyclerView: RecyclerView = view.findViewById(R.id.invModsSelectorRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = SelectionItemAdapter(selectionItems)
        adapter.setValueChangeListener(PositionListener())
        recyclerView.adapter = adapter

        // Populate display panel
        populateDisplayItem(selectionItems[position].data, view, requireContext(), imgDimension)
    }
}