package com.example.pipboyv1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HolotapeContentActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var content: String
    private lateinit var backButton: TextView // TextView used for simplicity / styling

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holotape_content)

        content = intent.extras?.get("content") as String
    }

    override fun onStart() {
        super.onStart()

        textView = findViewById(R.id.holotape_content)
        textView.text = content

        backButton = findViewById(R.id.holotape_content_back)
        backButton.text = "<"
        backButton.setOnClickListener{
            finish()
        }
    }
}