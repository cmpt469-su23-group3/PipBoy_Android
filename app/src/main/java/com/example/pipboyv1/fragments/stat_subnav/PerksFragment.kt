package com.example.pipboyv1.fragments.stat_subnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.classes.SelectionItemData
import com.example.pipboyv1.adapters.SelectionItemAdapter
import com.example.pipboyv1.helpers.populateDisplayItem
import com.example.pipboyv1.input.PositionChangeListener

class PerksFragment : Fragment() {
    private val selectionItems: MutableList<SelectionItem> = mutableListOf(
        SelectionItem(textLeft="Iron Fist", data=SelectionItemData(description="Channel your chi to unleash devastating fury! Punching attacks do 20% more damage to your opponent.", imageId=R.drawable.perk_iron_fist)),
        SelectionItem(textLeft="Pickpocket", data=SelectionItemData(description="Your quick hands and sticky fingers make picking pockets 25% easier.", imageId=R.drawable.perk_pickpocket)),
        SelectionItem(textLeft="Toughness", data=SelectionItemData(description="If nothing else, you can take a beating! Instantly gain +10 damage resistance.", imageId=R.drawable.perk_toughness)),
        SelectionItem(textLeft="Cap Collector", data=SelectionItemData(description="You've mastered the art of the deal! Buying and selling prices at vendors are better.", imageId=R.drawable.perk_cap_collector)),
        SelectionItem(textLeft="Hacker", data=SelectionItemData(description="Knowledge of cutting-edge computer encryption allows you to hack Advanced terminals.", imageId=R.drawable.perk_hacker)),
        SelectionItem(textLeft="Gunslinger", data=SelectionItemData(description="Channel the spirit of the old west! Non-automatic pistols do 20% more damage.", imageId=R.drawable.perk_gunslinger)),
        SelectionItem(textLeft="Fortune Finder", data=SelectionItemData(description="You've learned to discover the Wasteland's hidden wealth, and discover more bottle caps in containers.", imageId=R.drawable.perk_fortune_finder)),
    )
    private var position: Int = 0

    inner class PositionListener : PositionChangeListener {
        override fun onValueChange(newPosition: Int) {
            position = newPosition
            populateDisplayItem(selectionItems[position].data, requireView(), requireContext())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.statPerksSelectorRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = SelectionItemAdapter(selectionItems)
        adapter.setValueChangeListener(PositionListener())
        recyclerView.adapter = adapter

        // Populate display panel
        populateDisplayItem(selectionItems[position].data, view, requireContext())
    }
}