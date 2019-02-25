package com.example.jeffrey.messenger.view

import com.example.jeffrey.messenger.R
import com.example.jeffrey.messenger.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.partial_received_message_row.view.*
import kotlinx.android.synthetic.main.partial_sent_message_row.view.*

class ReceivedMessageItem(private val message: String, private val user: User): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.partial_received_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.partial_received_message_row_other_user_message.text = message
        val uri = user.profileImageUrl
        val targetView = viewHolder.itemView.partial_received_message_row_other_user_image
        Picasso.get().load(uri).fit().into(targetView)
    }
}

class SentMessageItem(private val message: String, private val user: User): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.partial_sent_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.partial_sent_message_row_user_message.text = message
        val uri = user.profileImageUrl
        val targetView = viewHolder.itemView.partial_sent_message_row_user_image
        Picasso.get().load(uri).fit().into(targetView)
    }
}