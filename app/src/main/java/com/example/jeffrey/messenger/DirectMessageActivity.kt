package com.example.jeffrey.messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class DirectMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct_message)

        supportActionBar?.title = "Chat"
    }
}