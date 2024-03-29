package com.example.pipboyv1.fragments.stat_subnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.classes.SelectionItemData
import com.example.pipboyv1.adapters.SelectionItemAdapter
import com.example.pipboyv1.helpers.populateDisplayItem
import com.example.pipboyv1.input.SelectionItemInputResponder
import java.util.concurrent.atomic.AtomicInteger

class SpecialFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter
    private var position: AtomicInteger = AtomicInteger()
    private val selectionItems: List<SelectionItem> = listOf(
        SelectionItem(textLeft="Strength", textRight="0", data=SelectionItemData(description="Strength is a measure of your raw physical power. It affects how much you can carry, and determines the effectiveness of all melee attacks.", imageId=R.drawable.stat_strength)),
        SelectionItem(textLeft="Perception", textRight="0", data=SelectionItemData(description="Perception is the ability to see, hear, taste and notice unusual things. A high Perception is important for a sharpshooter.", imageId=R.drawable.stat_perception)),
        SelectionItem(textLeft="Endurance", textRight="0", data=SelectionItemData(description="Endurance is the measure of overall physical fitness. It affects your total health and the action point drain from sprinting.", imageId=R.drawable.stat_endurance)),
        SelectionItem(textLeft="Charisma", textRight="0", data=SelectionItemData(description="Charisma is your ability to charm and convince others. It affects your success to persuade others in dialogue and prices when you barter.", imageId=R.drawable.stat_charisma)),
        SelectionItem(textLeft="Intelligence", textRight="0", data=SelectionItemData(description="Intelligence is the measure of your overall mental acuity, and affects the number of experience points earned.", imageId=R.drawable.stat_intelligence)),
        SelectionItem(textLeft="Agility", textRight="0", data=SelectionItemData(description="Agility is a measure of your overall finesse and reflexes. It affects the number of Action Points in V.A.T.S. and your ability to sneak.", imageId=R.drawable.stat_agility)),
        SelectionItem(textLeft="Luck", textRight="0", data=SelectionItemData(description="Luck is a measure of your general good fortune, and affects the recharge rates of critical hits.", imageId=R.drawable.stat_luck))
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems, requireContext())
        adapter.setHasStableIds(true)

        return inflater.inflate(R.layout.fragment_special, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setValueChangeListener(SelectionItemInputResponder(position, selectionItems, view, context))

        recyclerView = view.findViewById(R.id.statSpecialSelectorRecyclerView) as RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        // Populate display panel
        populateDisplayItem(selectionItems[position.get()].data, view, context)
    }
}