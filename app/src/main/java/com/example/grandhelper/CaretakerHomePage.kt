package com.example.grandhelper

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.ComponentActivity

class CaretakerHomePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.caretaker_home_page)

        val userProfile: ImageView = findViewById(R.id.profile_pic)
        userProfile.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        val dummy: ImageButton = findViewById(R.id.dummy_patient)
        dummy.setOnClickListener {
            startActivity(Intent(this, CaretakerPatientPage::class.java))
        }

        val btnOverallStatus: ImageButton = findViewById(R.id.add_patient)
        btnOverallStatus.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }
    }
}