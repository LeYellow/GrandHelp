package com.example.grandhelper

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity


class PatientOverallStatus : ComponentActivity() {

    private var pulse = 90
    private var systolicPressure = 110

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_overall_status)

        val tvPulse: TextView = findViewById(R.id.pulse)
        val tvBloodPressure1: TextView = findViewById(R.id.blood_pressure_1)

        tvPulse.text = "$pulse bpm"
        tvBloodPressure1.text = "$systolicPressure systolic mm Hg"

        val back_btn: ImageButton = findViewById(R.id.back_btn)
        back_btn.setOnClickListener {
            finish()
        }
    }
}