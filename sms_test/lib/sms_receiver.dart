// sms_receiver.dart
import 'package:telephony/telephony.dart';
import 'package:flutter/services.dart';

class SMSReceiver {
  final Telephony telephony = Telephony.instance;

  SMSReceiver() {
    init();
  }

  void init() {
    telephony.listenIncomingSms(
      onNewMessage: onNewMessage,
    );
  }

  void onNewMessage(SmsMessage message) {
    print('New SMS from: ${message.address}, message: ${message.body}');
    // Process the incoming SMS, e.g., update the UI or create a new conversation.
  }
}
