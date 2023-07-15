package com.example.pipboyv1.fragments.topnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.adapters.SelectionItemAdapter
import com.example.pipboyv1.classes.Holotape
import com.example.pipboyv1.classes.HolotapeContainer
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.classes.SelectionItemData
import com.example.pipboyv1.input.SelectionItemInputListener
import java.util.concurrent.atomic.AtomicInteger

class TapeFragment : Fragment() {
    private lateinit var holotape: Holotape
    private lateinit var description: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter
    private var descriptionText: String = ""
    private val selectionItems: MutableList<SelectionItem> = mutableListOf()
    private var position: AtomicInteger = AtomicInteger()

    inner class HolotapeSelectionItemInputResponder : SelectionItemInputListener {
        override fun onValueChange(newPosition: Int) {
            position.set(newPosition)

            val content = holotape.attributes.values.toList()[newPosition]

            // TODO: Replace current view with holotape content

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems)
        adapter.setHasStableIds(true)

        return inflater.inflate(R.layout.fragment_tape, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        description = view.findViewById(R.id.holotapeDescription)
        description.text = descriptionText

        adapter.setValueChangeListener(HolotapeSelectionItemInputResponder())

        recyclerView = view.findViewById(R.id.holotapeSelectorRecyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    fun onHolotapeLoaded(holotapeID: Int) {
        selectionItems.clear()
        holotape = HolotapeContainer.holotapes.first { it.id == holotapeID }

        holotape.attributes.forEach{(k, v) ->
            val selectionItem = SelectionItem(textLeft=k, data=SelectionItemData(description=v))
            selectionItems.add(selectionItem)
        }

        descriptionText = holotape.description
    }
}