package com.example.jeffrey.messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registrationButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Username: ${usernameRegistrationText.text}\n" +
                "Email: ${emailRegistrationText.text}\n" + "Password: ${passwordRegistrationText.text}", Toast.LENGTH_SHORT).show()
        }

        changeToLoginActivityButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
