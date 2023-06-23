package com.example.pipboyv1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R

class AttributeAdapterQuests(private val attributeMap: Map<String, String>): RecyclerView.Adapter<AttributeAdapterQuests.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textLeft: TextView
        val textRight: TextView

        init {
            textLeft = view.findViewById(R.id.attributeTextLeftQuests)
            textRight = view.findViewById(R.id.attributeTextRightQuests)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_attribute_quests, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val element = attributeMap.entries.elementAt(position)
        viewHolder.textLeft.text = element.key
        viewHolder.textRight.text = element.value
    }

    override fun getItemCount(): Int {
        return attributeMap.size
    }
}