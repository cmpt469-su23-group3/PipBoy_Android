package com.example.pipboyv1.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.classes.SelectionItem

class SelectionItemAdapter(private val selectionItemList: ArrayList<SelectionItem>): RecyclerView.Adapter<SelectionItemAdapter.ViewHolder>() {
    private var previousSelection: LinearLayout? = null
    private var previousSelectionPosition: Int = -1

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val selectionItem: LinearLayout
        val textLeftPrefix: TextView
        val textLeft: TextView
        val textLeftPostfix: TextView
        val textRightPrefix: TextView
        val textRight: TextView
        val textRightPostfix: TextView

        init {
            selectionItem = view.findViewById(R.id.selectionItem)
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
        // Add an click listener to the selection item itself
        viewHolder.selectionItem.setOnClickListener {
            handleSelectionItemClick(viewHolder, position)
        }

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

    private fun handleSelectionItemClick(viewHolder: ViewHolder, position: Int) {
         if (previousSelection != null){
            // Deselect previous selection
            selectionItemList[previousSelectionPosition].selected = false

            // Reset styling on (now) deselected item
            updateSelectionItemStyling(previousSelection, viewHolder, false)
        }

        // Assign new selection value
        // TODO: 5-Create-DisplayInfo-Pane -> handle rendering selection item data
        selectionItemList[position].selected = true

        // Store selection
        previousSelection = viewHolder.selectionItem
        previousSelectionPosition = position

        updateSelectionItemStyling(viewHolder.selectionItem, viewHolder, true)
    }

    private fun updateSelectionItemStyling(selectionItem: LinearLayout?, viewHolder: ViewHolder, selected: Boolean) {
        // Note: Here, we pass `selectionItem` separately from `viewHolder` in order to update items *not currently selected*
        val primaryColorId: Int = viewHolder.itemView.context.resources.getColor(R.color.text_1, null)
        val backgroundColorId: Int = viewHolder.itemView.context.resources.getColor(R.color.background_1, null)

        var txtColorId: Int = primaryColorId
        var bgColorId: Int = backgroundColorId

        // When an item is selected, the primary and background colours switch
        if (selected) {
            txtColorId = backgroundColorId
            bgColorId = primaryColorId
        }

        // Update the background colour
        selectionItem?.setBackgroundColor(bgColorId)

        // Update the text colour
        // TODO: FIND A BETTER WAY TO DO THIS
        for (child in selectionItem?.children!!) {
            if (child is LinearLayout) {
                for (grandchild in child.children) {
                    if (grandchild is TextView) {
                        grandchild.setTextColor(txtColorId)
                    }
                }
            }
        }
    }
}