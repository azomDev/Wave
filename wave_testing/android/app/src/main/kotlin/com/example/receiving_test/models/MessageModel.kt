package com.example.receiving_test.models

data class MessageModel(
    var sender: String = "",
    var messageContent: String = "",
    var timestampSent: String = "",
    var timestampReceived: String? = null // null if sent by yourself
)
