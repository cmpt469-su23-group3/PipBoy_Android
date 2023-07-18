package com.example.pipboyv1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.input.SelectionItemInputListener

class SelectionItemAdapter(private val selectionItemList: List<SelectionItem>, private val mContext: Context, private val initialSelection: Int = 0): RecyclerView.Adapter<SelectionItemAdapter.ViewHolder>() {
    private var selectionItemLayoutList: MutableList<LinearLayout> = mutableListOf()
    private var selectionPosition: Int = initialSelection
    private lateinit var selectionItemInputListener: SelectionItemInputListener

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
        if (!selectionItemLayoutList.contains(viewHolder.selectionItem)) {
            // Add selection item layout to list
            selectionItemLayoutList.add(viewHolder.selectionItem)
        }

        // Select item if applicable
        if (selectionPosition != -1) {
            updateSelectionItemStyling(selectionItemLayoutList[selectionPosition], true)
        }

        // Add an click listener to the selection item itself
        viewHolder.selectionItem.setOnClickListener {
            handleSelectionItemClick(position)
        }

        viewHolder.textLeft.text = selectionItemList[position].textLeft
        viewHolder.textCenter.text = selectionItemList[position].textCenter
        viewHolder.textRight.text = selectionItemList[position].textRight
    }

    override fun getItemId(position: Int): Long {
        return position.hashCode().toLong()
    }

    override fun getItemCount(): Int {
        return selectionItemList.size
    }

    // Method must be called by the parent to listen for position change
    fun setValueChangeListener(selectionItemInputListener: SelectionItemInputListener) {
        this.selectionItemInputListener = selectionItemInputListener
    }

    fun handleSelectionItemClick(position: Int) {
        // Deselect previous selection and reset styling if there was something selected previously
        if (selectionPosition != -1) {
            updateSelectionItemStyling(selectionItemLayoutList[selectionPosition], false)
        }

        // Update selection in current view
        selectionPosition = position
        updateSelectionItemStyling(selectionItemLayoutList[selectionPosition], true)

        // Update selection in parent
        selectionItemInputListener.onValueChange(position)
    }

    fun deselectAll() {
        // Deselect previous selection if applicable
        if (selectionPosition != -1) {
            updateSelectionItemStyling(selectionItemLayoutList[selectionPosition], false)
        }

        // Update selectionPosition to -1 -> indicates no selection before
        selectionPosition = -1
        selectionItemInputListener.onValueChange(-1)
    }

    private fun updateSelectionItemStyling(selectionItem: LinearLayout, selected: Boolean) {
        // Note: Here, we pass `selectionItem` separately from `viewHolder` in order to update items *not currently selected*
        val primaryColorId: Int = mContext.getColor(R.color.text_1)
        val backgroundColorId: Int = mContext.getColor(R.color.background_1)

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