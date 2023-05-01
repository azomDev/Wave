package com.example.receiving_test

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import android.telephony.SmsManager
import android.widget.Toast
import com.example.receiving_test.SmsDatabaseHandler
import com.example.receiving_test.MessageModel
import android.util.Log
import android.content.Context
import android.telephony.TelephonyManager

class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.example.receiving_test"
    private val smsReceiver = SmsReceiver()
    val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val userPhoneNumber = telephonyManager.line1Number

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val smsDatabaseHandler = SmsDatabaseHandler(this)

        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            when (call.method) {
                "getConversation" -> {
                    val conversationId = call.argument<Int>("conversationId")
                    if (conversationId != null) {
                        Log.d("MyApp", "Fetching conversation for conversation ID $conversationId")
                        val conversation = smsDatabaseHandler.getConversation(conversationId)
                        if (conversation != null) {
                        Log.d("MyApp", "Successfully fetched conversation")
                        // Convert each MessageModel to a Map
                        val conversationMapList = conversation.map { messageModel ->
                            mapOf(
                            "messageId" to messageModel.messageId,
                            "conversationId" to messageModel.conversationId,
                            "senderId" to messageModel.senderId,
                            "message" to messageModel.message,
                            "timestamp" to messageModel.timestamp
                            )
                        }
                        result.success(conversationMapList)
                        } else {
                        Log.e("MyApp", "Failed to fetch conversation from database")
                        result.error("UNAVAILABLE", "Could not fetch conversation", null)
                        }
                    } else {
                        Log.e("MyApp", "Invalid conversation ID")
                        result.error("UNAVAILABLE", "Could not fetch conversation", null)
                    }
                }


                "sendSms" -> {
                    val message = call.argument<String>("message")
                    val conversationId = call.argument<Int>("conversationId")
                    val participants = smsDatabaseHandler.getAllParticipantsInConversation(conversationId)

                    if (participants.isNotEmpty() && message != null) {
                        // for each participant, only send an sms (only add the sms once in the DB)
                        for (participant in participants) {
                            if (participant.participantId != userPhoneNumber) { // Do not send message to yourself
                                sendSms(participant.participantId.toString(), message)
                            }
                        }

                        // Create MessageModel and insert into database
                        val sms = MessageModel(
                            messageId = 0, // You can replace this with proper ID generation logic
                            conversationId = conversationId,
                            senderId = userPhoneNumber.toInt(),
                            message = message,
                            timestamp = System.currentTimeMillis().toString()
                        )
                        smsDatabaseHandler.addMessage(sms)
                        result.success(null)
                    } else {
                        result.error("UNAVAILABLE", "Could not send SMS", null)
                    }
                }

                "getAllConversations" -> {
                
                }

                else -> result.notImplemented()
            }
        }
    }

    private fun sendSms(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "SMS sent.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "SMS failed, please try again.", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}
