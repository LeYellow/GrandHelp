package com.example.grandhelper

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity


class PatientLiveStatus : ComponentActivity() {

    private var pulse = 90
    private var systolicPressure = 110
    private var diastolicPressure = 70

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.patient_live_status)

        val tvPulse: TextView = findViewById(R.id.pulse)
        val tvBloodPressure1: TextView = findViewById(R.id.blood_pressure_1)
        val tvBloodPressure2: TextView = findViewById(R.id.blood_pressure_2)

        tvPulse.text = "$pulse bpm"
        tvBloodPressure1.text = "$systolicPressure systolic mm Hg"
        tvBloodPressure2.text = "$diastolicPressure diastolic mm Hg"

        val back_btn: ImageButton = findViewById(R.id.back_btn)
        back_btn.setOnClickListener {
            finish()
        }
    }
}