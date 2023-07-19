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
import com.example.pipboyv1.input.SelectionItemInputListener
import java.util.concurrent.atomic.AtomicInteger

class QuestsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter

    private var position: AtomicInteger = AtomicInteger()

    private val imgDimension: Int = 250
    private val selectionItems: MutableList<SelectionItem> = mutableListOf(

        SelectionItem(textLeft="Lyon's Pride", data= SelectionItemData(
            description="Elder Maxson says Paladin Lyons's holotape is in the ruins of the Boston Airport. It is crucial to the Brotherhood's future. I need to find it.",
            attributes= mapOf(
                "Find Paladin Lyons's Holotape" to "",
                "Talk to Elder Maxson" to "",
                "Talk to Paladin Danse" to "",
                "Find Paladin Danse" to "",
                "Talk to Proctor Ingram" to "",
                "Talk to Elder Maxson" to "",
                "Visit the Prydwen" to "",
            ))),
        SelectionItem(textLeft="All Aboard", data= SelectionItemData(
            description="I tuned into an old Nuka-Cola family radio stations on my Pip-Boy. It was looping a jingle and advertisement for a place called Nuka-World. The broadcast mentioned a Nuka-World Transit Center with transportation to take me there. I should find it and see if I can still reach the amusement park.",
            attributes= mapOf(
                "Travel to the Nuka-World Transit Center" to "",
                "Listen to the Nuka-Cola Family Radio" to "",
            ))),
        SelectionItem(textLeft="The Watering Hole", data= SelectionItemData(
            description="For the next Vault 88 experiment I need to get chemical formulas Hallucigen, Inc.",
            attributes= mapOf(
                "Get the Chemical Research from Hallucigen, Inc." to "",
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

    inner class QuestSelectionItemInputResponder : SelectionItemInputListener {
        // Note: Quests behave differently from other selection items
        override fun onValueChange(newPosition: Int) {
            position.set(newPosition)
            populateDisplayItemQuest(selectionItems[position.get()].data, view, context, imgDimension)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems, requireContext())
        adapter.setHasStableIds(true)

        return inflater.inflate(R.layout.fragment_quests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setValueChangeListener(QuestSelectionItemInputResponder())

        selectionItems.sortBy { it.textLeft }

        recyclerView = view.findViewById(R.id.dataQuestsSelectorRecyclerView) as RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)


        // Populate display panel
        populateDisplayItemQuest(selectionItems[position.get()].data, view, context, imgDimension)
    }

}