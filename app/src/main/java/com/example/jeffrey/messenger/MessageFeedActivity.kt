package com.example.jeffrey.messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.jeffrey.messenger.model.DirectMessage
import com.example.jeffrey.messenger.view.MessageFeedItem
import com.example.jeffrey.messenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_message_feed.*

class MessageFeedActivity : AppCompatActivity() {

    companion object {
        private val TAG = MessageFeedActivity::class.java.simpleName
        var user: User? = null
    }

    private val adapter = GroupAdapter<ViewHolder>()
    private val messagesMap = HashMap<String, DirectMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_feed)

        supportActionBar?.title = "Messages"

        verifyUserLoggedIn()
        fetchUser()

        activity_message_feed_rv_message_list.adapter = adapter
        listenForMessages()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.new_message -> {
                startActivity(Intent(this, NewMessageActivity::class.java))
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun verifyUserLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun fetchUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                user = p0.getValue(User::class.java) ?: return
                Log.i(TAG, "Currently logged in as: ${user!!.username}")
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener (object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(DirectMessage::class.java) ?: return
                messagesMap[p0.key!!] = message
                refreshFeed()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val message = p0.getValue(DirectMessage::class.java) ?: return
                messagesMap[p0.key!!] = message
                refreshFeed()
            }

            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}

        })
    }

    private fun refreshFeed() {
        adapter.clear()
        messagesMap.forEach {
            adapter.add(MessageFeedItem(it.value))
        }
    }
}
