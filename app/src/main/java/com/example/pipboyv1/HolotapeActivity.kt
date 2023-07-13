package com.example.pipboyv1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.adapters.SelectionItemAdapter
import com.example.pipboyv1.classes.Holotape
import com.example.pipboyv1.classes.HolotapeContainer.holotapes
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.classes.SelectionItemData
import com.example.pipboyv1.input.SelectionItemInputListener
import java.util.concurrent.atomic.AtomicInteger

class HolotapeActivity : AppCompatActivity() {
    private lateinit var holotape: Holotape
    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: TextView // TextView used for simplicity / styling
    private lateinit var adapter: SelectionItemAdapter
    private val selectionItems: MutableList<SelectionItem> = mutableListOf()
    private var position: AtomicInteger = AtomicInteger()

    inner class HolotapeSelectionItemInputResponder : SelectionItemInputListener {
        override fun onValueChange(newPosition: Int) {
            position.set(newPosition)

            // TODO: Find better way to get content
            val intent = Intent(window.context, HolotapeContentActivity::class.java)
            intent.putExtra("content", holotape.attributes.values.toList()[newPosition])

            // NOTE: finish() intentionally NOT used here -> doesn't return to the last activity otherwise
            startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holotape)

        val holotapeID: Int = intent.extras?.get("holotapeID") as Int
        holotape = holotapes.find { it.id == holotapeID }!!

        holotape.attributes.forEach{(k, v) ->
            val selectionItem = SelectionItem(textLeft=k, data=SelectionItemData(description=v))
            selectionItems.add(selectionItem)
        }

        adapter = SelectionItemAdapter(selectionItems)
        adapter.setHasStableIds(true)
    }

    override fun onStart() {
        super.onStart()

        adapter.setValueChangeListener(HolotapeSelectionItemInputResponder())

        recyclerView = findViewById(R.id.holotapeSelectorRecyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        backButton = findViewById(R.id.holotape_back)
        backButton.text = "<"
        backButton.setOnClickListener{
            finish()
        }
    }
}