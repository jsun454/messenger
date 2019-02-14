package com.example.jeffrey.messenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectPhotoButton.setOnClickListener {
            startActivityForResult(
                Intent(Intent.ACTION_PICK).setType("image/*"),
                0
            )
        }

        registrationButton.setOnClickListener {
            registerUser()
        }

        changeToLoginActivityButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    var photoUri: Uri? = null

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            photoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            selectPhotoButton.setBackgroundDrawable(bitmapDrawable)
            selectPhotoButton.text = ""
        }
    }

    private fun registerUser() {
        val email = emailRegistrationText.text.toString()
        val password = passwordRegistrationText.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email/password", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully created user with ID: " + it.user.uid,
                    Toast.LENGTH_SHORT).show()
                storeImage()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create user: ${it.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeImage() {
        
    }
}
