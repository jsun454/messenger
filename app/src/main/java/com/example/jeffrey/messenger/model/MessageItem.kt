package com.example.jeffrey.messenger.model

import com.example.jeffrey.messenger.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.partial_received_message_row.view.*
import kotlinx.android.synthetic.main.partial_sent_message_row.view.*

class ReceivedMessageItem(private val message: String): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.partial_received_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.partial_received_message_row_other_user_message.text = message
    }
}

class SentMessageItem(private val message: String): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.partial_sent_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.partial_sent_message_row_other_user_message.text = message
    }
}