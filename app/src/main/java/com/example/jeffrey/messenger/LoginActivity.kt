package com.example.jeffrey.messenger

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            loginUser()
        }

        changeToRegisterActivityButton.setOnClickListener {
            finish()
        }
    }

    private fun loginUser() {
        val email = emailLoginText.text.toString()
        val password = passwordLoginText.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email/password", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // TODO: implement
            }
            .addOnFailureListener {
                // TODO: implement
            }
    }
}