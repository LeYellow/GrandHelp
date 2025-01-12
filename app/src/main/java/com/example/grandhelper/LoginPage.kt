package com.example.grandhelper

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.grandhelper.databinding.LoginPageBinding
import com.google.firebase.auth.FirebaseAuth

class LoginPage: ComponentActivity() {

    private lateinit var binding:LoginPageBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        // Retrieve input fields
        binding.loginBtn.setOnClickListener {
            val email = binding.loginEmailBox.text.toString().trim()
            val password = binding.loginPassBox.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Authenticate with Firebase
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Navigate to the CaretakerHomePage on successful login
                            val intent = Intent(this, CaretakerHomePage::class.java)
                            startActivity(intent)
                        } else {
                            // Show error message
                            Toast.makeText(
                                this,
                                "Login failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                // Show a Toast if fields are empty
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle "Forgot Password" button
        binding.forgotPassBtn.setOnClickListener {
            val email = binding.loginEmailBox.text.toString().trim()

            if (email.isNotEmpty()) {
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Password reset email sent.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Error: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle "Register Here" text
        binding.registerText.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }
    }
}