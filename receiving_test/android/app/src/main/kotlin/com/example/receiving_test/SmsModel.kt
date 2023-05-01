package com.example.receiving_test

data class SmsModel(
    var id: Int = 0,
    var sender: String = "",
    var recipient: String = "",
    var message: String = "",
    var timestamp: String = ""
)
