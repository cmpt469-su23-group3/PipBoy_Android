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
import com.example.pipboyv1.helpers.populateDisplayItem
import com.example.pipboyv1.input.SelectionItemInputResponder
import java.util.concurrent.atomic.AtomicInteger

class WorkshopsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter

    private var position: AtomicInteger = AtomicInteger()
    private var PEOPLE: String = "People"
    private var FOOD: String = "Food"
    private var WATER: String = "Water"
    private var POWER: String = "Power"
    private var DEFENSE: String = "Defense"
    private var BEDS: String = "Beds"
    private var HAPPINESS: String = "Happiness"

    private val imgDimension: Int = 250
    private val selectionItems: MutableList<SelectionItem> = mutableListOf(
        SelectionItem(textLeft="Abernathy Farm", data= SelectionItemData(imageId=-1, attributes= mapOf(
            PEOPLE to "3",
            FOOD to "10",
            WATER to "3",
            POWER to "0",
            DEFENSE to "0",
            BEDS to "4",
            HAPPINESS to "46",
        ))),
        SelectionItem(textLeft="Red Rocket Truck Stop", data= SelectionItemData(imageId=-1, attributes= mapOf(
            PEOPLE to "0",
            FOOD to "0",
            WATER to "0",
            POWER to "3",
            DEFENSE to "0",
            BEDS to "0",
            HAPPINESS to "0",
        ))),
        SelectionItem(textLeft="Sanctuary Hills", data= SelectionItemData(imageId=-1, attributes= mapOf(
            PEOPLE to "10",
            FOOD to "10",
            WATER to "10",
            POWER to "10",
            DEFENSE to "10",
            BEDS to "10",
            HAPPINESS to "100",
        ))),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems, requireContext())
        adapter.setHasStableIds(true)

        return inflater.inflate(R.layout.fragment_workshops, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setValueChangeListener(SelectionItemInputResponder(position, selectionItems, view, context))

        selectionItems.sortBy { it.textLeft }

        recyclerView = view.findViewById(R.id.dataWorkshopsSelectorRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        // Populate display panel
        populateDisplayItem(selectionItems[position.get()].data, view, context, imgDimension)
    }
}