package com.example.receiving_test

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import androidx.core.app.NotificationCompat
import android.telephony.SmsMessage
import android.util.Log
import io.flutter.plugin.common.MethodChannel
import android.telephony.TelephonyManager

class SmsReceiver : BroadcastReceiver() {

    private lateinit var methodChannel: MethodChannel
    val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val userPhoneNumber = telephonyManager.line1Number

    fun setMethodChannel(methodChannel: MethodChannel) {
        this.methodChannel = methodChannel
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_DELIVER_ACTION) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in smsMessages) {
                val smsDatabaseHandler = SmsDatabaseHandler(context)

                // Determine conversation ID based on the sender's phone number
                val senderPhoneNumber = message.originatingAddress?.replace(Regex("[^0-9]"), "")
                val conversationId = smsDatabaseHandler.getConversationId(senderPhoneNumberm, userPhoneNumber)

                // If conversationId is null, this is a new conversation
                if (conversationId == null) {
                    // Create a new conversation
                    val newConversation = ConversationModel(
                        conversationId = 0, // id will be set by the database
                    )
                    smsDatabaseHandler.addConversation(newConversation)

                    // Get the newly created conversation ID
                    val newConversationId = smsDatabaseHandler.getConversationId(senderPhoneNumber)

                    // Create a new message
                    val newMessage = MessageModel(
                        messageId = 0, // id will be set by the database
                        conversationId = newConversationId,
                        senderId = senderPhoneNumber,
                        message = message.messageBody,
                        timestamp = System.currentTimeMillis().toString()
                    )
                    smsDatabaseHandler.addMessage(newMessage)
                } else {
                    // Create a new message in the existing conversation
                    val newMessage = MessageModel(
                        messageId = 0, // id will be set by the database
                        conversationId = conversationId,
                        senderId = senderPhoneNumber,
                        message = message.messageBody,
                        timestamp = System.currentTimeMillis().toString()
                    )
                    smsDatabaseHandler.addMessage(newMessage)
                }

                showNotification(context, message)
            }
        }
    }

    private fun showNotification(context: Context, message: SmsMessage) {
        val channelId = "sms_channel_id"
        val notificationId = 101

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "SMS Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("New SMS received")
            .setContentText("From: ${message.originatingAddress}, Message: ${message.messageBody}")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(notificationId, notification)
    }
}
