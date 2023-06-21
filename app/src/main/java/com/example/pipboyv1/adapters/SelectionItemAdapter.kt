package com.example.pipboyv1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.input.PositionChangeListener

class SelectionItemAdapter(private val selectionItemList: List<SelectionItem>): RecyclerView.Adapter<SelectionItemAdapter.ViewHolder>() {
    private var selectionItemLayoutList: MutableList<LinearLayout> = mutableListOf()
    private var selectionPosition: Int = 0
    private lateinit var positionChangeListener: PositionChangeListener

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

        // Select first item by default
        updateSelectionItemStyling(selectionItemLayoutList[selectionPosition], viewHolder, true)

        // Add an click listener to the selection item itself
        viewHolder.selectionItem.setOnClickListener {
            handleSelectionItemClick(viewHolder, position)
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
    fun setValueChangeListener(positionChangeListener: PositionChangeListener?) {
        this.positionChangeListener = positionChangeListener!!
    }

    private fun handleSelectionItemClick(viewHolder: ViewHolder, position: Int) {
        // Deselect previous selection and reset styling
        updateSelectionItemStyling(selectionItemLayoutList[selectionPosition], viewHolder, false)

        // Update selection in current view
        selectionPosition = position
        updateSelectionItemStyling(viewHolder.selectionItem, viewHolder, true)

        // Update selection in parent
        positionChangeListener.onValueChange(position)
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