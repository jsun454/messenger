package com.example.jeffrey.messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.jeffrey.messenger.model.User
import com.example.jeffrey.messenger.view.UserItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {
    companion object {
        const val USER_KEY = "USER_KEY"
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select Recipient"

        activity_new_message_rv_user_list.adapter = adapter

        fetchUsers()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val user = it.getValue(User::class.java) ?: return@forEach
                    adapter.add(UserItem(user))
                }

                adapter.setOnItemClickListener { item, view ->
                    val intent = Intent(view.context, DirectMessageActivity::class.java)
                    intent.putExtra(USER_KEY, (item as UserItem).user)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}
