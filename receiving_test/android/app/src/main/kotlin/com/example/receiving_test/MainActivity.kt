package com.example.receiving_test

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import android.telephony.SmsManager
import android.widget.Toast

class MainActivity: FlutterActivity() {
    private val CHANNEL = "com.example.receiving_test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            if (call.method == "sendSms") {
                val num = call.argument<String>("phoneNumber")
                val msg = call.argument<String>("message")
                if (num != null && msg != null) {
                    sendSms(num, msg)
                    result.success("SMS Sent")
                } else {
                    result.error("UNAVAILABLE", "SMS not sent", null)
                }
            } else {
                result.notImplemented()
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
