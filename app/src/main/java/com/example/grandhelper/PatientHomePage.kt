package com.example.grandhelper

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity

class PatientHomePage : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.patient_home_page)

            val btnLiveStatus: ImageButton = findViewById(R.id.live_status)
            btnLiveStatus.setOnClickListener {
                startActivity(Intent(this, PatientLiveStatus::class.java))
            }

            val btnOverallStatus: ImageButton = findViewById(R.id.overall_status)
            btnOverallStatus.setOnClickListener {
                startActivity(Intent(this, PatientOverallStatus::class.java))
            }

            val btnContact: ImageButton = findViewById(R.id.contact_caretaker)
            btnContact.setOnClickListener{
                showContactPopup()
            }

        }

    private fun showContactPopup() {
        val dialogView = layoutInflater.inflate(R.layout.contact_popup, null)

        val caretakerNumber = "+1234567890"

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val btnMessage = dialogView.findViewById<Button>(R.id.message_btn)
        btnMessage.setOnClickListener {
            val smsIntent = Intent(Intent.ACTION_SENDTO)
            smsIntent.data = Uri.parse("smsto:"+ caretakerNumber)
            startActivity(smsIntent)
            dialog.dismiss()
        }

        val btnCall = dialogView.findViewById<Button>(R.id.call_btn)
        btnCall.setOnClickListener {
            val phoneIntent = Intent(Intent.ACTION_DIAL)
            phoneIntent.data = Uri.parse("tel:"+ caretakerNumber)
            startActivity(phoneIntent)
            dialog.dismiss()
        }

        val closePopup = dialogView.findViewById<ImageButton>(R.id.close_popup)
        closePopup.setOnClickListener {
            dialog.dismiss()
        }

        val popupText: TextView = dialogView.findViewById(R.id.popup_text)
        popupText.text = "Do you want to call or message the caretaker?"

        dialog.show()
        val window = dialog.window
        if (window != null) {
            val widthInPx = (300 * resources.displayMetrics.density).toInt()
            val heightInPx = (200 * resources.displayMetrics.density).toInt()
            window.setLayout(widthInPx, heightInPx)
        }
    }

}
