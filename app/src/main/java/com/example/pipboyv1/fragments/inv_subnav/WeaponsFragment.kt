package com.example.pipboyv1.fragments.inv_subnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.adapters.AttributeAdapter
import com.example.pipboyv1.adapters.SelectionItemAdapter
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.classes.SelectionItemData
import com.example.pipboyv1.input.PositionChangeListener

class WeaponsFragment : Fragment() {
    private val selectionItems: MutableList<SelectionItem> = mutableListOf(
        SelectionItem(textLeft="10mm Pistol", data= SelectionItemData(imageId=R.drawable.weapon_10mm_pistol, attributes= mapOf(
            "Damage" to "18",
            "Fire Rate" to "46",
            "Range" to "83",
            "Accuracy" to "60",
            "Weight" to "3.5",
            "Value" to "50",
        ))),
    )
    private var position: Int = 0

    inner class PositionListener : PositionChangeListener {
        override fun onValueChange(newPosition: Int) {
            position = newPosition
            populateDisplayItem()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weapons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.invWeaponsSelectorRecyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = SelectionItemAdapter(selectionItems)
        adapter.setValueChangeListener(PositionListener())
        recyclerView.adapter = adapter

        // Populate display panel
        populateDisplayItem()
    }

    private fun populateDisplayItem() {
        val selectionItemData = selectionItems[position].data

        if (selectionItemData.imageId >= 0) {
            val imageView: ImageView = this.requireView().findViewById(R.id.displayItemImage)
            val imageViewLayoutParams = imageView.layoutParams
            imageViewLayoutParams.width = 250
            imageViewLayoutParams.height = 250
            imageView.layoutParams = imageViewLayoutParams
            imageView.setImageResource(selectionItemData.imageId)
        }
        if (selectionItemData.description.isNotEmpty()) {
            val textView: TextView = this.requireView().findViewById(R.id.displayItemDescription)
            textView.text = selectionItemData.description
        }
        if (selectionItemData.attributes.isNotEmpty()) {
            val attributeRecyclerView: RecyclerView = this.requireView().findViewById(R.id.displayItemRecyclerView)
            attributeRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            val adapter = AttributeAdapter(selectionItemData.attributes)
            attributeRecyclerView.adapter = adapter
        }
    }
}