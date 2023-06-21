package com.example.pipboyv1.fragments.inv_subnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pipboyv1.R
import com.example.pipboyv1.adapters.SelectionItemAdapter
import com.example.pipboyv1.classes.SelectionItem
import com.example.pipboyv1.classes.SelectionItemData
import com.example.pipboyv1.helpers.populateDisplayItem
import com.example.pipboyv1.input.PositionChangeListener

class WeaponsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectionItemAdapter
    private var position: Int = 0
    private var DAMAGE: String = "Damage"
    private var FIRERATE: String = "Fire Rate"
    private var RANGE: String = "Range"
    private var ACCURACY: String = "Accuracy"
    private var WEIGHT: String = "Weight"
    private var VALUE: String = "Value"

    private val imgDimension: Int = 250
    private val selectionItems: List<SelectionItem> = listOf(
        SelectionItem(textLeft="10mm Pistol", data= SelectionItemData(imageId=R.drawable.weapon_10mm_pistol, attributes= mapOf(
            DAMAGE to "18",
            "10mm" to "158",
            FIRERATE to "46",
            RANGE to "83",
            ACCURACY to "60",
            WEIGHT to "3.5",
            VALUE to "50",
        ))),
        SelectionItem(textLeft="Gatling Laser", data= SelectionItemData(imageId=R.drawable.weapon_gatling_laser, attributes= mapOf(
            DAMAGE to "14",
            "Core" to "45",
            FIRERATE to "272",
            RANGE to "203",
            ACCURACY to "48",
            WEIGHT to "19.3",
            VALUE to "804",
        ))),
        SelectionItem(textLeft="Pipe Bolt-Action Pistol", data= SelectionItemData(imageId=R.drawable.weapon_pipe_bolt_action, attributes= mapOf(
            DAMAGE to "34",
            ".38" to "734",
            FIRERATE to "2",
            RANGE to "95",
            ACCURACY to "63",
            WEIGHT to "3.2",
            VALUE to "30",
        ))),
        SelectionItem(textLeft="Combat Shotgun", data= SelectionItemData(imageId=R.drawable.weapon_combat_shotgun, attributes= mapOf(
            DAMAGE to "50",
            "Shell" to "253",
            FIRERATE to "20",
            RANGE to "47",
            ACCURACY to "23",
            WEIGHT to "11.2",
            VALUE to "87",
        ))),
        SelectionItem(textLeft="Fat Man", data= SelectionItemData(imageId=R.drawable.weapon_fatman, attributes= mapOf(
            DAMAGE to "468",
            "Mini Nuke" to "16",
            FIRERATE to "1",
            RANGE to "117",
            ACCURACY to "63",
            WEIGHT to "30.7",
            VALUE to "512",
        ))),
        SelectionItem(textLeft="Assault Rifle", data= SelectionItemData(imageId=R.drawable.weapon_assault_rifle, attributes= mapOf(
            DAMAGE to "30",
            "5.56mm" to "1235",
            FIRERATE to "40",
            RANGE to "119",
            ACCURACY to "72",
            WEIGHT to "13.1",
            VALUE to "144",
        )))
    )

    inner class PositionListener : PositionChangeListener {
        override fun onValueChange(newPosition: Int) {
            position = newPosition
            populateDisplayItem(selectionItems[position].data, view, context, imgDimension)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SelectionItemAdapter(selectionItems)
        adapter.setHasStableIds(true)
        adapter.setValueChangeListener(PositionListener())

        return inflater.inflate(R.layout.fragment_weapons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.invWeaponsSelectorRecyclerView) as RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)


        // Populate display panel
        populateDisplayItem(selectionItems[position].data, view, context, imgDimension)
    }
}