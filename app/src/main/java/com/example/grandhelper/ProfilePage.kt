package com.example.grandhelper

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity

class ProfilePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)

        val val_first_name = "Johnny"
        val val_last_name = "Dope"
        val val_phone = "123456789"
        val val_gender = "Male"
        val val_age = 70

        val firstName: TextView = findViewById(R.id.first_name)
        val lastName: TextView = findViewById(R.id.last_name)
        val phone: TextView = findViewById(R.id.phone_number)
        val gender: TextView = findViewById(R.id.gender)
        val age: TextView = findViewById(R.id.age)

        firstName.text = "$val_first_name"
        lastName.text = "$val_last_name"
        phone.text = "$val_phone"
        gender.text = "$val_gender"
        age.text = "$val_age"

        val val_full_name = "$val_first_name $val_last_name"
        val fullName: TextView = findViewById(R.id.user_name)
        fullName.text = "$val_full_name"

        val save_btn: Button = findViewById(R.id.save_changes)
        save_btn.setOnClickListener {
            finish()
        }

        val remove_btn: Button = findViewById(R.id.remove_account)
        remove_btn.setOnClickListener {
            finish()
        }

        val back_btn: ImageButton = findViewById(R.id.back_btn)
        back_btn.setOnClickListener {
            finish()
        }
    }
}