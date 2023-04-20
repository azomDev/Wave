import 'package:flutter/material.dart';
import '../models/conversation.dart';
import '../widgets/phone_input.dart';

class CreateConversationScreen extends StatefulWidget {
  @override
  _CreateConversationScreenState createState() =>
      _CreateConversationScreenState();
}

class _CreateConversationScreenState extends State<CreateConversationScreen> {
  List<String> recipients = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Create Conversation'),
        actions: [
          IconButton(
            icon: Icon(Icons.check),
            onPressed: recipients.isEmpty
                ? null
                : () {
                    Navigator.pop(
                      context,
                      Conversation(recipients: recipients, messages: []),
                    );
                  },
          ),
        ],
      ),
      body: PhoneInput(
        onRecipientAdded: (String recipient) {
          setState(() {
            recipients.add(recipient);
          });
        },
        onRecipientRemoved: (String recipient) {
          setState(() {
            recipients.remove(recipient);
          });
        },
      ),
    );
  }
}
