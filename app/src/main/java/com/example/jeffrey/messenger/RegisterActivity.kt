package com.example.jeffrey.messenger

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

    @SuppressLint("SetTextI18n")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            photoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)

            selectPhotoImageView.setImageBitmap(bitmap)
            selectPhotoButton.background.alpha = 0
            selectPhotoButton.text = "Change Photo"
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
                Log.d("Jeffrey", "Successfully created user with ID: ${it.user.uid}")
                storeImage()
            }
            .addOnFailureListener {
                Log.d("Jeffrey", "Failed to create user: ${it.message}")
            }
    }

    private fun storeImage() {
        if(photoUri == null) {
            return
        }

        val image = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$image")
        ref.putFile(photoUri!!)
            .addOnSuccessListener {
                Log.d("Jeffrey", "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("Jeffrey", "Image file location: $uri")
                    saveUser(uri.toString())
                }
            }
            .addOnFailureListener {
                Log.d("Jeffrey", "Failed to upload image: ${it.message}")
            }
    }

    private fun saveUser(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val username = usernameRegistrationText.text.toString()
        val user = User(uid, username, profileImageUrl)

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Jeffrey", "Successfully saved user to database")
            }
            .addOnFailureListener {
                Log.d("Jeffrey", "Failed to save user to database: ${it.message}")
            }
    }
}

class User(val uid: String, val username: String, val profileImageUrl: String)