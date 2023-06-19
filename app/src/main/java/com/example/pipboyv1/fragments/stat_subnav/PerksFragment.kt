package com.example.pipboyv1.fragments.stat_subnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.classes.SelectionItemData
import com.example.pipboyv1.fragments.adapters.SelectionItemAdapter
import com.example.pipboyv1.fragments.items.DisplayItemFragment

class PerksFragment : Fragment() {
    private val selectionItems: MutableList<SelectionItem> = mutableListOf(
        SelectionItem(textLeft="Iron Fist", data=SelectionItemData(description="Channel your chi to unleash devastating fury! Punching attacks do 20% more damage to your opponent.")),
        SelectionItem(textLeft="Pickpocket", data=SelectionItemData(description="Your quick hands and sticky fingers make picking pockets 25% easier.")),
        SelectionItem(textLeft="Toughness", data=SelectionItemData(description="If nothing else, you can take a beating! Instantly gain +10 damage resistance.")),
        SelectionItem(textLeft="Cap Collector", data=SelectionItemData(description="You've mastered the art of the deal! Buying and selling prices at vendors are better.")),
        SelectionItem(textLeft="Hacker", data=SelectionItemData(description="Knowledge of cutting-edge computer encryption allows you to hack Advanced terminals.")),
        SelectionItem(textLeft="Gunslinger", data=SelectionItemData(description="Channel the spirit of the old west! Non-automatic pistols do 20% more damage.")),
        SelectionItem(textLeft="Fortune Finder", data=SelectionItemData(description="You've learned to discover the Wasteland's hidden wealth, and discover more bottle caps in containers.")),
    )
    private var position: Int = 0
    private lateinit var fragmentManager: FragmentManager

    inner class PositionListener : SelectionItemAdapter.ValueChangeListener {
        override fun onValueChange(newPosition: Int) {
            position = newPosition
            handleSelectionItemPositionChange()
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

        val specialRecyclerView: RecyclerView = view.findViewById(R.id.statPerksSelectorRecyclerView) as RecyclerView
        specialRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = SelectionItemAdapter(selectionItems)
        adapter.setValueChangeListener(PositionListener())
        specialRecyclerView.adapter = adapter

        // Create display panel
        fragmentManager = this.requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.statPerkDisplayView, DisplayItemFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        // Populate display panel
        populateDisplayItem()
    }

    private fun handleSelectionItemPositionChange() {
        populateDisplayItem()
    }

    private fun populateDisplayItem() {
        val selectionItemData = selectionItems[position].data
        
        val imageView: ImageView = this.requireView().findViewById(R.id.displayItemImage)
        val textView: TextView = this.requireView().findViewById(R.id.displayItemDescription)

        if (selectionItemData.imageId >= 0) { imageView.setImageResource(selectionItemData.imageId) }
        if (selectionItemData.description.isNotEmpty()) { textView.text = selectionItemData.description }
    }
}