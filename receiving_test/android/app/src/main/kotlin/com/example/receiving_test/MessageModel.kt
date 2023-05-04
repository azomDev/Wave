package com.example.receiving_test

data class MessageModel(
    var messageId: Int = 0,
    var conversationId: Int = 0,
    var senderId: Int = 0,
    var message: String = "",
    var timestamp: String = ""
)
