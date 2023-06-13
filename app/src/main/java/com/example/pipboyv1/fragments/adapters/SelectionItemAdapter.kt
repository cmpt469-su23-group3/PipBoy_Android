package com.example.pipboyv1.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.classes.SelectionItem

class SelectionItemAdapter(private val selectionItemList: ArrayList<SelectionItem>): RecyclerView.Adapter<SelectionItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textLeftPrefix: TextView
        val textLeft: TextView
        val textLeftPostfix: TextView
        val textRightPrefix: TextView
        val textRight: TextView
        val textRightPostfix: TextView

        init {
            textLeftPrefix = view.findViewById(R.id.selectionItemTextLeftPrefix)
            textLeft = view.findViewById(R.id.selectionItemTextLeft)
            textLeftPostfix = view.findViewById(R.id.selectionItemTextLeftPostfix)
            textRightPrefix = view.findViewById(R.id.selectionItemTextRightPrefix)
            textRight = view.findViewById(R.id.selectionItemTextRight)
            textRightPostfix = view.findViewById(R.id.selectionItemTextRightPostfix)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.selection_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textLeftPrefix.text = selectionItemList[position].textLeftPrefix
        viewHolder.textLeft.text = selectionItemList[position].textLeft
        viewHolder.textLeftPostfix.text = selectionItemList[position].textLeftPostfix
        viewHolder.textRightPrefix.text = selectionItemList[position].textRightPrefix
        viewHolder.textRight.text = selectionItemList[position].textRight
        viewHolder.textRightPostfix.text = selectionItemList[position].textRightPostfix
    }

    override fun getItemCount(): Int {
        return selectionItemList.size
    }
}