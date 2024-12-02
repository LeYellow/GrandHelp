package com.example.grandhelper

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

class CaretakerPatientPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.caretaker_patient_page)

        val userProfile: ImageView = findViewById(R.id.profile_pic)
        userProfile.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        val btnLiveStatus: ImageButton = findViewById(R.id.live_status)
        btnLiveStatus.setOnClickListener {
            startActivity(Intent(this, PatientLiveStatus::class.java))
        }

        val btnOverallStatus: ImageButton = findViewById(R.id.overall_status)
        btnOverallStatus.setOnClickListener {
            startActivity(Intent(this, PatientOverallStatus::class.java))
        }

        val btnMap: ImageButton = findViewById(R.id.location)
        btnMap.setOnClickListener{
            startActivity(Intent(this, LocationPage::class.java))
        }

        val btnPatientSettings: ImageButton = findViewById(R.id.patient_settings)
        btnPatientSettings.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        val btnContact: ImageButton = findViewById(R.id.contact_caretaker)
        btnContact.setOnClickListener{
            showContactPopup()
        }

        val back_btn: ImageButton = findViewById(R.id.back_btn)
        back_btn.setOnClickListener {
            finish()
        }

    }

    private fun showContactPopup() {
        val dialogView = layoutInflater.inflate(R.layout.contact_popup, null)

        val patientNumber = "+1234567890"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val btnMessage = dialogView.findViewById<Button>(R.id.message_btn)
        btnMessage.setOnClickListener {
            val smsIntent = Intent(Intent.ACTION_SENDTO)
            smsIntent.data = Uri.parse("smsto:"+ patientNumber)
            startActivity(smsIntent)
            dialog.dismiss()
        }

        val btnCall = dialogView.findViewById<Button>(R.id.call_btn)
        btnCall.setOnClickListener {
            val phoneIntent = Intent(Intent.ACTION_DIAL)
            phoneIntent.data = Uri.parse("tel:"+ patientNumber)
            startActivity(phoneIntent)
            dialog.dismiss()
        }

        val closePopup = dialogView.findViewById<ImageButton>(R.id.close_popup)
        closePopup.setOnClickListener {
            dialog.dismiss()
        }

        val popupText: TextView = dialogView.findViewById(R.id.popup_text)
        popupText.text = "Do you want to call or message the patient?"

        dialog.show()
        val window = dialog.window
        if (window != null) {
            val widthInPx = (300 * resources.displayMetrics.density).toInt()
            val heightInPx = (200 * resources.displayMetrics.density).toInt()
            window.setLayout(widthInPx, heightInPx)
        }
    }

}
