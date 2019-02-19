package com.example.jeffrey.messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.jeffrey.messenger.model.ReceivedMessageItem
import com.example.jeffrey.messenger.model.SentMessageItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_direct_message.*

class DirectMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct_message)

        supportActionBar?.title = "Chat"

        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(ReceivedMessageItem())
        adapter.add(ReceivedMessageItem())
        adapter.add(SentMessageItem())
        adapter.add(SentMessageItem())
        adapter.add(ReceivedMessageItem())
        adapter.add(SentMessageItem())
        adapter.add(ReceivedMessageItem())
        adapter.add(ReceivedMessageItem())
        adapter.add(SentMessageItem())
        adapter.add(SentMessageItem())
        adapter.add(ReceivedMessageItem())
        adapter.add(SentMessageItem())

        activity_direct_message_rv_message_list.adapter = adapter
    }
}