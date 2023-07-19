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
import com.example.pipboyv1.input.SelectionItemInputResponder
import java.util.concurrent.atomic.AtomicInteger

class ModsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter

    private var position: AtomicInteger = AtomicInteger()
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems, requireContext())
        adapter.setHasStableIds(true)

        return inflater.inflate(R.layout.fragment_mods, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setValueChangeListener(SelectionItemInputResponder(position, selectionItems, view, context))

        selectionItems.sortBy { it.textLeft }

        recyclerView = view.findViewById(R.id.invModsSelectorRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        // Populate display panel
        populateDisplayItem(selectionItems[position.get()].data, view, context, imgDimension)
    }
}