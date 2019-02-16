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
import com.example.jeffrey.messenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        private val TAG = RegisterActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        activity_register_btn_select_photo.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK).setType("image/*"), 0)
        }

        activity_register_btn_register.setOnClickListener {
            registerUser()
        }

        activity_register_txt_go_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private var photoUri: Uri? = null

    @SuppressLint("SetTextI18n")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            photoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)

            activity_register_img_user_picture.setImageBitmap(bitmap)
            activity_register_btn_select_photo.background.alpha = 0
            activity_register_btn_select_photo.text = "Change Photo"
        }
    }

    private fun registerUser() {
        val email = activity_register_et_email.text.toString()
        val password = activity_register_et_password.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email/password", Toast.LENGTH_SHORT).show()
            return
        } else if(photoUri == null) {
            Toast.makeText(this, "Please add a photo", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.i(TAG, "Successfully created user with ID: ${it.user.uid}")
                Toast.makeText(this, "Creating user...", Toast.LENGTH_SHORT).show()
                storeImage()
            }
            .addOnFailureListener {
                Log.w(TAG, "Failed to create user: ${it.message}")
                Toast.makeText(this, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeImage() {
        val image = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$image")
        ref.putFile(photoUri!!)
            .addOnSuccessListener {
                Log.i(TAG, "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener { uri ->
                    Log.i(TAG, "Image file location: $uri")
                    saveUser(uri.toString())
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to upload image: ${it.message}")
            }
    }

    private fun saveUser(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val username = activity_register_et_username.text.toString()
        val user = User(uid, username, profileImageUrl)

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.setValue(user)
            .addOnSuccessListener {
                Log.i(TAG, "Successfully saved user to database")

                val intent = Intent(this, MessageFeedActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to save user to database: ${it.message}")
            }
    }
}