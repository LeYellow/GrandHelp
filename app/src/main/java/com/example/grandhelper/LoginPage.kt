package com.example.grandhelper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity

class LoginPage: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // Retrieve input fields
        val emailInput: EditText = findViewById(R.id.login_email_box)
        val passwordInput: EditText = findViewById(R.id.login_pass_box)

        // Handle "Log In" button
        val loginButton: Button = findViewById(R.id.login_btn)
        loginButton.setOnClickListener {
            startActivity(Intent(this, CaretakerHomePage::class.java))
        }

        // Handle "Forgot Password" button
        val forgotPasswordButton: Button = findViewById(R.id.forgot_pass_btn)
        forgotPasswordButton.setOnClickListener {
            startActivity(Intent(this, PatientHomePage::class.java))
        }

        //Handle "Register Here" text
        val registerText: TextView = findViewById(R.id.register_text)
        registerText.setOnClickListener {
            startActivity(Intent(this, RegisterPage::class.java))
        }
    }
}