package com.example.receiving_test

import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.content.Intent
import android.provider.Telephony

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.receiving_test"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "setDefaultSms") {
                try {
                    var intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    intent.putExtra(
                            Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                            "com.example.receiving_test" // replace this with your app package name
                    );
                    startActivity(intent);
                    result.success("Success")
                } catch (ex: Exception) {
                    result.error("UNAVAILABLE", "Setting default sms.", null)
                }
            } else {
                result.notImplemented()
            }
        }
    }
}
