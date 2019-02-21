package com.example.jeffrey.messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.jeffrey.messenger.model.DirectMessage
import com.example.jeffrey.messenger.model.ReceivedMessageItem
import com.example.jeffrey.messenger.model.SentMessageItem
import com.example.jeffrey.messenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_direct_message.*

class DirectMessageActivity : AppCompatActivity() {
    companion object {
        private val TAG = DirectMessageActivity::class.java.simpleName
    }

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct_message)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user.username

        activity_direct_message_rv_message_list.adapter = adapter

        listenForMessages()

        activity_direct_message_btn_send.setOnClickListener {
            sendMessage()
        }
    }

    private fun listenForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(DirectMessage::class.java) ?: return
                if(message.fromId == FirebaseAuth.getInstance().uid) {
                    adapter.add(SentMessageItem(message.message))
                } else {
                    adapter.add(ReceivedMessageItem(message.message))
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

    private fun sendMessage() {
        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
        val fromId = FirebaseAuth.getInstance().uid ?: return
        val recipient = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = recipient.uid
        val text = activity_direct_message_et_user_message.text.toString()
        val message = DirectMessage(ref.key!!, fromId, toId, text, System.currentTimeMillis())
        ref.setValue(message)
            .addOnSuccessListener {
                Log.i(TAG, "Successfully saved message to database")
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to save message to database: ${it.message}")
            }
    }
}