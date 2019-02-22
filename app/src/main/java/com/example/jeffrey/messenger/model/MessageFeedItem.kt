package com.example.jeffrey.messenger.model

import com.example.jeffrey.messenger.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class MessageFeedItem: Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.partial_latest_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

}