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

class SelectionItemAdapter(private val selectionItemList: MutableList<SelectionItem>): RecyclerView.Adapter<SelectionItemAdapter.ViewHolder>() {
    private var selectionItemLayoutList: MutableList<LinearLayout> = mutableListOf()
    private var selectionPosition: Int = 0
    private lateinit var valueChangeListener: ValueChangeListener

    interface ValueChangeListener {
        fun onValueChange(newPosition: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val selectionItem: LinearLayout
        val textLeft: TextView
        val textCenter: TextView
        val textRight: TextView

        init {
            selectionItem = view.findViewById(R.id.selectionItem)
            textLeft = view.findViewById(R.id.selectionItemTextLeft)
            textCenter = view.findViewById(R.id.selectionItemTextCenter)
            textRight = view.findViewById(R.id.selectionItemTextRight)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_selection_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Add selection item layout to list
        selectionItemLayoutList.add(viewHolder.selectionItem)

        // Select first item by default
        selectionItemList[selectionPosition].selected = true
        updateSelectionItemStyling(selectionItemLayoutList[selectionPosition], viewHolder, true)

        // Add an click listener to the selection item itself
        viewHolder.selectionItem.setOnClickListener {
            handleSelectionItemClick(viewHolder, position)
        }

        viewHolder.textLeft.text = selectionItemList[position].textLeft
        viewHolder.textCenter.text = selectionItemList[position].textCenter
        viewHolder.textRight.text = selectionItemList[position].textRight
    }

    override fun getItemCount(): Int {
        return selectionItemList.size
    }

    // Method must be called by the parent to listen for position change
    fun setValueChangeListener(valueChangeListener: ValueChangeListener?) {
        this.valueChangeListener = valueChangeListener!!
    }

    private fun handleSelectionItemClick(viewHolder: ViewHolder, position: Int) {
        // Deselect previous selection and reset styling
        selectionItemList[selectionPosition].selected = false
        updateSelectionItemStyling(selectionItemLayoutList[selectionPosition], viewHolder, false)

        // Update selection in current view
        selectionItemList[position].selected = true
        selectionPosition = position
        updateSelectionItemStyling(viewHolder.selectionItem, viewHolder, true)

        // Update selection in parent
        if (valueChangeListener != null) {
            valueChangeListener.onValueChange(position)
        }
    }

    private fun updateSelectionItemStyling(selectionItem: LinearLayout, viewHolder: ViewHolder, selected: Boolean) {
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
        selectionItem.setBackgroundColor(bgColorId)

        // Update the text colour
        selectionItem.children.filterIsInstance<TextView>().forEach { it.setTextColor(txtColorId) }
    }
}