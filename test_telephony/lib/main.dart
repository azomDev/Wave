import 'package:flutter/material.dart';
import 'dart:async';
import 'package:telephony/telephony.dart';

onBackgroundMessage(SmsMessage message) {
  debugPrint("onBackgroundMessage called");
}

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _message = "";
  String _phoneNumber = "";
  String _smsMessage = "";
  final telephony = Telephony.instance;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  onMessage(SmsMessage message) async {
    setState(() {
      _message = message.body ?? "Error reading message body.";
    });
  }

  onSendStatus(SendStatus status) {
    setState(() {
      _message = status == SendStatus.SENT ? "sent" : "delivered";
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    final bool? result = await telephony.requestPhoneAndSmsPermissions;

    if (result != null && result) {
      telephony.listenIncomingSms(
          onNewMessage: onMessage, onBackgroundMessage: onBackgroundMessage);
    }

    if (!mounted) return;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TextFormField(
              decoration: InputDecoration(labelText: 'Phone Number'),
              onChanged: (String value) {
                setState(() {
                  _phoneNumber = value;
                });
              },
            ),
            TextFormField(
              decoration: InputDecoration(labelText: 'SMS Message'),
              onChanged: (String value) {
                setState(() {
                  _smsMessage = value;
                });
              },
            ),
            TextButton(
              onPressed: () async {
                await telephony.sendSms(
                    to: _phoneNumber, message: _smsMessage);
              },
              child: Text('Send SMS'),
            ),
            SizedBox(height: 20),
            Text("Latest received SMS: $_message"),
            TextButton(
                onPressed: () async {
                  await telephony.openDialer("123413453");
                },
                child: Text('Open Dialer'))
          ],
        ),
      ),
    ));
  }
}
