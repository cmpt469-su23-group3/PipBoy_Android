package com.example.pipboyv1.classes

open class SelectionItem(
    val textLeftPrefix: String? = "",
    val textLeft: String? = "",
    val textLeftPostfix: String? = "",
    val textRightPrefix: String? = "",
    val textRight: String? = "",
    val textRightPostfix: String? = "",
    var selected: Boolean = false,
    var data: SelectionItemData = SelectionItemData()
)