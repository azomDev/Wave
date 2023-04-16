import 'package:flutter/material.dart';
import 'package:test/models/sms_message.dart';

class SmsListScreen extends StatelessWidget {
  final List<SmsMessage> messages;

  SmsListScreen({required this.messages});

  @override
  Widget build(BuildContext context) {
    return Container(
      child: ListView.builder(
        itemCount: messages.length,
        itemBuilder: (context, index) {
          SmsMessage message = messages[index];
          return ListTile(
            leading: Icon(Icons.sms),
            title: Text(message.sender == message.receiver ? 'Me' : message.sender),
            subtitle: Text(message.message),
            trailing: Text(
              '${message.timestamp.hour}:${message.timestamp.minute}',
            ),
          );
        },
      ),
    );
  }
}
