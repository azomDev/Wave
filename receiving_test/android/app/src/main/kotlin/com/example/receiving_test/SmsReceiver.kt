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

class SmsReceiver : BroadcastReceiver() {

    private lateinit var methodChannel: MethodChannel

    fun setMethodChannel(methodChannel: MethodChannel) {
        this.methodChannel = methodChannel
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_DELIVER_ACTION) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in smsMessages) {
                //! THis is temporary but I switched sender and recipient
                val sms = SmsModel(
                    id = 0, // id will be set by the database
                    sender = "me",
                    recipient = message.originatingAddress ?: "", //! set recipient as needed AND FORMAT IT
                    message = message.messageBody,
                    timestamp = System.currentTimeMillis().toString()
                )

                val smsDatabaseHandler = SmsDatabaseHandler(context)
                smsDatabaseHandler.addSms(sms)

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
