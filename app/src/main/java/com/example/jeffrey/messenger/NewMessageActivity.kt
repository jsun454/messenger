package com.example.jeffrey.messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select Recipient"

        newMessageRecyclerView
    }
}
