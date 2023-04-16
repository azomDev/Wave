import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:wave_frontend/components/message_input.dart';
import 'package:wave_frontend/components/send_button.dart';
import 'package:wave_frontend/services/messaging_service.dart';
import 'package:wave_frontend/components/message_list.dart';
import 'package:wave_frontend/models/message.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final TextEditingController _messageController = TextEditingController();

  void _sendMessage(BuildContext context) {
    final messagingService = Provider.of<MessagingService>(context, listen: false);

    if (_messageController.text.isNotEmpty) {
      messagingService.sendMessage(_messageController.text);
      _messageController.clear();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Wave'),
      ),
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 20, vertical: 10),
        child: Column(
          children: [
            Expanded(
              child: Consumer<MessagingService>(
                builder: (context, messagingService, child) {
                  return MessageList(messages: messagingService.messages);
                },
              ),
            ),
            SizedBox(height: 10),
            Row(
              children: [
                Expanded(
                  child: MessageInput(
                    controller: _messageController,
                  ),
                ),
                SizedBox(width: 10),
                SendButton(
                  onPressed: () => _sendMessage(context),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
