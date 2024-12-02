package com.example.grandhelper

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity

class LocationPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_page)

        val back_btn: ImageButton = findViewById(R.id.back_btn)
        back_btn.setOnClickListener {
            finish()
        }
    }
}