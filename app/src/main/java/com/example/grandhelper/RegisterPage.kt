package com.example.grandhelper

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity

class RegisterPage: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.register_page)

        // Retrieve input fields
        val emailInput: EditText = findViewById(R.id.register_email_box)
        val phoneInput: EditText = findViewById(R.id.register_phone_box)
        val passwordInput: EditText = findViewById(R.id.register_pass_box)
        val passwordConfirmInput: EditText = findViewById(R.id.register_confirm_pass_box)

        // Handle "Sign Here" text
        val registerText: TextView = findViewById(R.id.register_login_text)
        registerText.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
        }

        // Handle "Log In" button
        val signCaretaker: Button = findViewById(R.id.sign_caretaker)
        signCaretaker.setOnClickListener {
            startActivity(Intent(this, CaretakerHomePage::class.java))
        }

        // Handle "Forgot Password" button
        val signPatient: Button = findViewById(R.id.sign_patient)
        signPatient.setOnClickListener {
            startActivity(Intent(this, PatientHomePage::class.java))
        }

        val back_btn: ImageButton = findViewById(R.id.back_btn)
        back_btn.setOnClickListener {
            finish()
        }
    }
}