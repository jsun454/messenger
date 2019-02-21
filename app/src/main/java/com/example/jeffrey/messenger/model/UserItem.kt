package com.example.jeffrey.messenger.model

import com.example.jeffrey.messenger.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.partial_recipient_row.view.*

class UserItem(val user: User): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.partial_recipient_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.partial_recipient_row_txt_username.text = user.username
        Picasso.get().load(user.profileImageUrl).fit().into(viewHolder.itemView.partial_recipient_row_img_user_picture)
    }
}