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
        val activeQuestColorId: Int = viewHolder.itemView.context.resources.getColor(R.color.background_active_quest, null)
        val activeQuestTextColorId: Int = viewHolder.itemView.context.resources.getColor(R.color.text_1, null)
        val finishedQuestColorId: Int = viewHolder.itemView.context.resources.getColor(R.color.background_finished_quest, null)
        val finishedQuestTextColorId: Int = viewHolder.itemView.context.resources.getColor(R.color.text_finished_quest, null)

        val element = attributeMap.entries.elementAt(position)
        viewHolder.textLeft.text = element.key
        viewHolder.textRight.text = element.value
        if(position == 0) {
            viewHolder.textLeft.setBackgroundColor(activeQuestColorId)
            viewHolder.textRight.setBackgroundColor(activeQuestColorId)
            viewHolder.textLeft.setTextColor(activeQuestTextColorId)
            viewHolder.textRight.setTextColor(activeQuestTextColorId)
        }
        else {
            viewHolder.textLeft.setBackgroundColor(finishedQuestColorId)
            viewHolder.textRight.setBackgroundColor(finishedQuestColorId)
            viewHolder.textLeft.setTextColor(finishedQuestTextColorId)
            viewHolder.textRight.setTextColor(finishedQuestTextColorId)
        }

    }

    override fun getItemCount(): Int {
        return attributeMap.size
    }
}