package com.example.grandhelper

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterPage : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.register_page)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Retrieve input fields
        val emailInput: EditText = findViewById(R.id.register_email_box)
        val phoneInput: EditText = findViewById(R.id.register_phone_box)
        val passwordInput: EditText = findViewById(R.id.register_pass_box)
        val passwordConfirmInput: EditText = findViewById(R.id.register_confirm_pass_box)

        // Handle "Sign Here" text (navigate back to LoginPage)
        val registerText: TextView = findViewById(R.id.register_login_text)
        registerText.setOnClickListener {
            startActivity(Intent(this, LoginPage::class.java))
        }

        // Handle "Sign as Caretaker" button
        val signCaretaker: Button = findViewById(R.id.sign_caretaker)
        signCaretaker.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = passwordConfirmInput.text.toString().trim()

            if (validateFields(email, password, confirmPassword)) {
                // Register the caretaker
                registerUser(email, password) {
                    val intent = Intent(this, CaretakerHomePage::class.java)
                    startActivity(intent)
                }
            }
        }

        // Handle "Sign as Patient" button
        val signPatient: Button = findViewById(R.id.sign_patient)
        signPatient.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = passwordConfirmInput.text.toString().trim()

            if (validateFields(email, password, confirmPassword)) {
                // Register the patient
                registerUser(email, password) {
                    val intent = Intent(this, PatientHomePage::class.java)
                    startActivity(intent)
                }
            }
        }

        // Handle "Back" button
        val backBtn: ImageButton = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }
    }

    /**
     * Validates the email, password, and confirm password fields.
     */
    private fun validateFields(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    /**
     * Registers a user with Firebase Authentication.
     */
    private fun registerUser(email: String, password: String, onSuccess: () -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    onSuccess()
                } else {
                    Toast.makeText(
                        this,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
