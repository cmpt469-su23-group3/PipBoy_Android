package com.example.pipboyv1.helpers

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.adapters.AttributeAdapter
import com.example.pipboyv1.classes.SelectionItemData

fun populateDisplayItem(data: SelectionItemData, view: View, context: Context, imgDimen: Int = 450) {
    if (data.imageId >= 0) {
        val imageView: ImageView = view.findViewById(R.id.displayItemImage)
        val imageViewLayoutParams = imageView.layoutParams
        imageViewLayoutParams.width = imgDimen
        imageViewLayoutParams.height = imgDimen
        imageView.layoutParams = imageViewLayoutParams
        imageView.setImageResource(data.imageId)
    }
    if (data.description.isNotEmpty()) {
        val textView: TextView = view.findViewById(R.id.displayItemDescription)
        textView.text = data.description
    }
    if (data.attributes.isNotEmpty()) {
        val attributeRecyclerView: RecyclerView = view.findViewById(R.id.displayItemRecyclerView)
        attributeRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = AttributeAdapter(data.attributes)
        attributeRecyclerView.adapter = adapter
    }
}