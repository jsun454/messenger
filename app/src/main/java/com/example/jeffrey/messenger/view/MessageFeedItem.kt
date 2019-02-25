package com.example.jeffrey.messenger.view

import com.example.jeffrey.messenger.R
import com.example.jeffrey.messenger.model.DirectMessage
import com.example.jeffrey.messenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.partial_latest_message_row.view.*

class MessageFeedItem(private val message: DirectMessage): Item<ViewHolder>() {
    var otherUser: User? = null

    override fun getLayout(): Int {
        return R.layout.partial_latest_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.partial_latest_message_row_txt_message.text = message.message

        val otherId = if(message.fromId == FirebaseAuth.getInstance().uid) {
            message.toId
        } else {
            message.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$otherId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                otherUser = p0.getValue(User::class.java) ?: return
                viewHolder.itemView.partial_latest_message_row_txt_username.text = otherUser?.username

                val uri = otherUser?.profileImageUrl
                val targetView = viewHolder.itemView.partial_latest_message_row_img_user_picture
                Picasso.get().load(uri).fit().into(targetView)
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

}