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

class StatsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter

    private var position: AtomicInteger = AtomicInteger()

    private val imgDimension: Int = 250
    private val selectionItems: MutableList<SelectionItem> = mutableListOf(
        SelectionItem(textLeft="General", data= SelectionItemData(imageId=-1, attributes= mapOf(
            "Locations Discovered" to "116",
            "Locaitons Cleared" to "",
            "Days Passed" to "",
            "Hours Slept" to "",
            "Hours Waiting" to "",
            "Caps Found" to "",
            "Most Caps Carried" to "",
            "Junk Collected" to "",
            "Chests Looted" to "",
            "Magazines Found" to "",
        ))
        ),
        SelectionItem(textLeft="Quest", data= SelectionItemData(imageId=-1, attributes= mapOf(
            "Quests Completed" to "0",
            "Misc Obj Completed" to "0",
            "Main Quests Completed" to "0",
            "Side Quests Completed" to "0",
            "BoS Completed" to "0",
            "MM Quests Completed" to "0",
            "RR Quests Completed" to "0",
        ))
        ),
        SelectionItem(textLeft="Combat", data= SelectionItemData(imageId=-1, attributes= mapOf(
            "People Killed" to "51",
            "Animals Killed" to "37",
            "Creatures Killed" to "10",
            "Robots Killed" to "54",
            "Synths Killed" to "0",
            "Turrets Killed" to "24",
            "Legendary Enemies Killed" to "2",
            "Critical Strikes" to "5",
            "Sneak Attacks" to "6",
            "Backstabs" to "0",
            "Weapons Disarmed" to "2",
            "Grand Slams" to "0",
            "Fits of Rage" to "0",
            "Money Shots" to "0",
            "Grim Reaper Sprints" to "0",
            "Four Leaf Clovers" to "0",
            "Ricochets" to "2",
        ))
        ),
        SelectionItem(textLeft="Crafting", data= SelectionItemData(imageId=-1, attributes= mapOf(
            "Weapon Mods Crafted" to "10",
            "Armor Mods Crafted" to "24",
            "Plants Harvested" to "34",
            "Chems Crafted" to "15",
            "Food Cooked" to "16",
            "Workshops Unlocked" to "3",
            "Items Scrapped" to "49",
            "Objects Built" to "50",
            "Supply Lines Created" to "1",
        ))
        ),
        SelectionItem(textLeft="Crime", data= SelectionItemData(imageId=-1, attributes= mapOf(
            "Locks Picked" to "20",
            "Computers Hacked" to "15",
            "Pockets Picked" to "20",
            "Items Stolen" to "20",
            "Assaults" to "3",
            "Murders" to "0",
            "Trespasses" to "0",
        ))
        ),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems)
        adapter.setHasStableIds(true)

        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setValueChangeListener(SelectionItemInputResponder(position, selectionItems, view, context))

//        selectionItems.sortBy { it.textLeft }

        recyclerView = view.findViewById(R.id.dataStatsSelectorRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        // Populate display panel
        populateDisplayItem(selectionItems[position.get()].data, view, context, imgDimension)
    }

}