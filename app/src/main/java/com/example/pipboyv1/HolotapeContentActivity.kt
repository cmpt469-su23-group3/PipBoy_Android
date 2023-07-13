package com.example.pipboyv1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HolotapeContentActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var content: String
    private lateinit var backButton: TextView // TextView used for simplicity / styling

    override fun onCreate(savedInstanceState: Bundle?) {
        content = intent.extras?.get("content") as String

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holotape_content)
    }

    override fun onStart() {
        textView = findViewById(R.id.holotape_content)
        textView.text = content

        backButton = findViewById(R.id.holotape_content_back)
        backButton.text = "<"
        backButton.setOnClickListener{
            this.finish()
        }

        super.onStart()
    }
}