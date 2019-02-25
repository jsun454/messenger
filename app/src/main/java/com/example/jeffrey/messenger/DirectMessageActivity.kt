package com.example.jeffrey.messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.jeffrey.messenger.model.DirectMessage
import com.example.jeffrey.messenger.view.ReceivedMessageItem
import com.example.jeffrey.messenger.view.SentMessageItem
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
    private var otherUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct_message)

        otherUser = intent.getParcelableExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title = otherUser?.username ?: "Chat"

        activity_direct_message_rv_message_list.adapter = adapter
        listenForMessages()

        activity_direct_message_btn_send.setOnClickListener {
            sendMessage()
        }
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid ?: return
        val toId = otherUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(DirectMessage::class.java) ?: return
                if(message.fromId == FirebaseAuth.getInstance().uid) {
                    val user = MessageFeedActivity.user ?: return
                    adapter.add(SentMessageItem(message.message, user))
                } else {
                    adapter.add(
                        ReceivedMessageItem(
                            message.message,
                            otherUser ?: return
                        )
                    )
                }
                activity_direct_message_rv_message_list.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

    private fun sendMessage() {
        val text = activity_direct_message_et_user_message.text.toString()
        if(text.isEmpty()) {
            return
        }

        val fromId = FirebaseAuth.getInstance().uid ?: return
        val toId = otherUser?.uid ?: return

        val ref = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId").push()
        val otherRef = FirebaseDatabase.getInstance().getReference("/messages/$toId/$fromId").push()
        val latestRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        val otherLatestRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")

        val message = DirectMessage(ref.key!!, fromId, toId, text, System.currentTimeMillis())

        ref.setValue(message)
            .addOnSuccessListener {
                Log.i(TAG, "Successfully saved message to database")
                activity_direct_message_et_user_message.text.clear()
                activity_direct_message_rv_message_list.scrollToPosition(adapter.itemCount - 1)
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to save message to database: ${it.message}")
            }

        otherRef.setValue(message)

        latestRef.setValue(message)

        otherLatestRef.setValue(message)
    }
}