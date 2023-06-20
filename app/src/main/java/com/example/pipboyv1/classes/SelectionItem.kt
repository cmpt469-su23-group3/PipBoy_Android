package com.example.pipboyv1.classes

class SelectionItem(
    val textLeft: String = "",
    val textCenter: String = "",
    val textRight: String = "",
    var selected: Boolean = false,
    var data: SelectionItemData = SelectionItemData()
)