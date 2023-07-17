package com.example.pipboyv1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pipboyv1.R

class TapeContentFragment : Fragment() {
    private lateinit var textView: TextView
    private lateinit var content: String
    private lateinit var backButton: TextView // TextView used for simplicity / styling

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tape_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView = view.findViewById(R.id.holotapeContent)
        textView.text = content

        backButton = view.findViewById(R.id.holotapeContentBack)
        backButton.text = "<"
        backButton.setOnClickListener{
            parentFragmentManager.popBackStackImmediate()
        }
    }

    fun onHolotapeContentSelected(newContent: String) {
        content = newContent
    }
}