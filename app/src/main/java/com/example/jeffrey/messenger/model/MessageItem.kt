package com.example.jeffrey.messenger.model

import com.example.jeffrey.messenger.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class ReceivedMessageItem: Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.partial_received_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}

class SentMessageItem: Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.partial_sent_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}