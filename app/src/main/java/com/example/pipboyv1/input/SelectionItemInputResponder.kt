package com.example.pipboyv1.input

import android.content.Context
import android.view.View
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.helpers.populateDisplayItem
import java.util.concurrent.atomic.AtomicInteger

class SelectionItemInputResponder(val position: AtomicInteger, val selectionItems: List<SelectionItem>, val view: View?, val context: Context?): SelectionItemInputListener {

    override fun onValueChange(newPosition: Int) {
        position.set(newPosition)
        populateDisplayItem(selectionItems[position.get()].data, view, context)
    }

}