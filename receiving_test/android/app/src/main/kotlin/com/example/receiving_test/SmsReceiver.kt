package com.example.app_name

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.provider.Telephony

class SmsReceiver : BroadcastReceiver() {
    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "SMSReceiverChannel"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            smsMessages.forEach { message ->
                // Process the message here
                showNotification(context, message.originatingAddress ?: "Unknown", message.messageBody)
            }
        }
    }

    private fun showNotification(context: Context?, sender: String, message: String) {
        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("New SMS from $sender")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SMS Receiver Channel"
            val descriptionText = "Notification channel for SMS Receiver"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.RED
            }
            val notificationManager: NotificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
