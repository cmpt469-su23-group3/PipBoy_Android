package com.example.pipboyv1.classes

open class SelectionItem(
    val textLeftPrefix: String? = "",
    val textLeft: String? = "",
    val textLeftPostfix: String? = "",
    val textRightPrefix: String? = "",
    val textRight: String? = "",
    val textRightPostfix: String? = "",
    val description: String? = "",
    val image: Int? = 0,
    var selected: Boolean = false
)