package com.example.jeffrey.messenger.model

class DirectMessage(val id: String, val fromId: String, val toId: String, val message: String, val timeStamp: Long) {
    constructor(): this("", "", "", "", -1)
}