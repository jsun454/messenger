package com.example.jeffrey.messenger

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            Toast.makeText(this@LoginActivity, "Username: ${usernameRegistrationText.text}\n" +
                    "Password: ${passwordRegistrationText.text}", Toast.LENGTH_SHORT).show()
        }

        changeToRegisterActivityButton.setOnClickListener {
            finish()
        }
    }
}