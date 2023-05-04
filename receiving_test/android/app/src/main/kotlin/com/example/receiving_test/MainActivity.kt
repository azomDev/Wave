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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val smsDatabaseHandler = SmsDatabaseHandler(this)

        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            when (call.method) {
                "getConversation" -> {
                    val conversationId = call.argument<Int>("conversationId")
                    if (conversationId != null) {
                        val messageModels = smsDatabaseHandler.getMessagesFromConversation(conversationId)
                        val messageMaps = messageModels.map {
                            mapOf(
                                "messageId" to it.messageId,
                                "conversationId" to it.conversationId,
                                "senderId" to it.senderId,
                                "message" to it.message,
                                "timestamp" to it.timestamp
                            )
                        }
                        result.success(messageMaps)
                    } else {
                        Log.d("MyApp", "Conversation ID cannot be null")
                        result.error("INVALID_ARGUMENT", "Conversation ID cannot be null", null)
                    }
                }

                "sendSms" -> {
                    val message = call.argument<String>("message")
                    val conversationId = call.argument<Int>("conversationId")
                    val users = smsDatabaseHandler.getAllUsersInConversation(conversationId!!)

                    if (users.isNotEmpty() && message != null) {
                        for (user in users) {
                            val userPhoneNumber = user.phoneNumber
                            if (userPhoneNumber != "me") { // Do not send message to yourself
                                sendSms(userPhoneNumber, message)
                                Log.d("MyApp", "Sending message to: $userPhoneNumber")
                            }
                        }

                        val sms = MessageModel(
                            messageId = 0, // Replace this with proper ID generation logic
                            conversationId = conversationId!!,
                            senderId = smsDatabaseHandler.createOrReturnUser("me"),
                            message = message,
                            timestamp = System.currentTimeMillis().toString()
                        )
                        smsDatabaseHandler.addMessage(sms)
                        result.success(message)
                    } else {
                        Log.d("MyApp", "Could not send SMS, messages or participants is empty")
                        result.error("UNAVAILABLE", "Could not send SMS", null)
                    }
                }

                "getAllConversations" -> {
                    result.success(smsDatabaseHandler.getAllConversations())
                }

                "createNewConversation" -> {
                    result.success(smsDatabaseHandler.createNewConversation())
                }

                "createNewUser" -> {
                    val phoneNumber = call.argument<String>("phoneNumber")
                    result.success(smsDatabaseHandler.createOrReturnUser(phoneNumber!!))
                }

                "createNewParticipant" -> {
                    val userId = call.argument<Int>("userId")
                    val conversationId = call.argument<Int>("conversationId")
                    result.success(smsDatabaseHandler.createNewParticipant(userId!!, conversationId!!))
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
