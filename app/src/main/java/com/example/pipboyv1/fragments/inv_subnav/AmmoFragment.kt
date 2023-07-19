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

class AmmoFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter

    private var position: AtomicInteger = AtomicInteger()
    private var WEIGHT: String = "Weight"
    private var VALUE: String = "Value"

    private val imgDimension: Int = 250
    private val selectionItems: MutableList<SelectionItem> = mutableListOf(
        SelectionItem(textLeft="10mm Round (158)", data= SelectionItemData(imageId=R.drawable.ammo_10mm, attributes= mapOf(
            WEIGHT to "0",
            VALUE to "1",
        ))),
        SelectionItem(textLeft=".38 Round (734)", data= SelectionItemData(imageId=R.drawable.ammo_38, attributes= mapOf(
            WEIGHT to "0",
            VALUE to "1",
        ))),
        SelectionItem(textLeft="Mini Nuke (16)", data= SelectionItemData(imageId=R.drawable.ammo_mini, attributes= mapOf(
            WEIGHT to "0",
            VALUE to "100",
        ))),
        SelectionItem(textLeft="Fusion Core (45)", data= SelectionItemData(imageId=R.drawable.ammo_fusion_core, attributes= mapOf(
            WEIGHT to "0",
            VALUE to "200",
        ))),
        SelectionItem(textLeft="Shotgun Shell (253)", data= SelectionItemData(imageId=R.drawable.ammo_shells, attributes= mapOf(
            WEIGHT to "0",
            VALUE to "3",
        ))),
        SelectionItem(textLeft="5.56mm Round (1235)", data= SelectionItemData(imageId=R.drawable.ammo_556, attributes= mapOf(
            WEIGHT to "0",
            VALUE to "2",
        ))),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems, requireContext())
        adapter.setHasStableIds(true)

        return inflater.inflate(R.layout.fragment_ammo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setValueChangeListener(SelectionItemInputResponder(position, selectionItems, view, context))

        selectionItems.sortBy { it.textLeft }

        recyclerView= view.findViewById(R.id.invAmmoSelectorRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        // Populate display panel
        populateDisplayItem(selectionItems[position.get()].data, view, context, imgDimension)
    }

}