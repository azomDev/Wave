import 'package:flutter/material.dart';
import 'package:wave_frontend/components/message_input.dart';
import 'package:wave_frontend/components/send_button.dart';
import 'package:wave_frontend/services/messaging_service.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final TextEditingController _messageController = TextEditingController();
  final MessagingService _messagingService = MessagingService();

  void _sendMessage() async {
    if (_messageController.text.isNotEmpty) {
      // Send the message to the database using the messaging service
      await _messagingService.sendMessage(_messageController.text);
      // Clear the text box
      _messageController.clear();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Simple Messaging App'),
      ),
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 20, vertical: 10),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            Row(
              children: [
                Expanded(
                  child: MessageInput(
                    controller: _messageController,
                  ),
                ),
                SizedBox(width: 10),
                SendButton(
                  onPressed: _sendMessage,
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
