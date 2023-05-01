package com.example.receiving_test

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import android.telephony.SmsManager
import android.widget.Toast
import com.example.receiving_test.SmsDatabaseHandler
import com.example.receiving_test.SmsModel
import android.util.Log

class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.example.receiving_test"
    private val smsReceiver = SmsReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val smsDatabaseHandler = SmsDatabaseHandler(this)

        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            when (call.method) {
                "getConversation" -> {
                    val sender = call.argument<String>("sender")
                    val recipient = call.argument<String>("recipient")
                    if (sender != null && recipient != null) {
                        Log.d("MyApp", "Fetching conversation for $sender and $recipient")
                        val conversation = smsDatabaseHandler.getConversation(sender, recipient)
                        if (conversation != null) {
                        Log.d("MyApp", "Successfully fetched conversation")
                        // Convert each SmsModel to a Map
                        val conversationMapList = conversation.map { smsModel ->
                            mapOf(
                            "id" to smsModel.id,
                            "sender" to smsModel.sender,
                            "recipient" to smsModel.recipient,
                            "message" to smsModel.message,
                            "timestamp" to smsModel.timestamp
                            )
                        }
                        result.success(conversationMapList)
                        } else {
                        Log.e("MyApp", "Failed to fetch conversation from database")
                        result.error("UNAVAILABLE", "Could not fetch conversation", null)
                        }
                    } else {
                        Log.e("MyApp", "Invalid sender or recipient")
                        result.error("UNAVAILABLE", "Could not fetch conversation", null)
                    }
                }


                "sendSms" -> {
                    val phoneNumber = call.argument<String>("recipient")
                    val message = call.argument<String>("message")
                    if (phoneNumber != null && message != null) {
                        sendSms(phoneNumber, message)

                        // Create SmsModel and insert into database
                        val sms = SmsModel(
                            id = 0, // You can replace this with proper ID generation logic
                            sender = "me", // Adjust this as needed
                            recipient = phoneNumber,
                            message = message,
                            timestamp = System.currentTimeMillis().toString()
                        )
                        smsDatabaseHandler.addSms(sms)
                        result.success(null)
                    } else {
                        result.error("UNAVAILABLE", "Could not send SMS", null)
                    }
                }

                else -> result.notImplemented()
            }
        }
    }

    private fun sendSms(phoneNumber: String, message: String) {
        try {
            Log.d("MyApp", "Message: $message")
            Log.d("MyApp", "number: $phoneNumber")
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "SMS sent.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "SMS failed, please try again.", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}
