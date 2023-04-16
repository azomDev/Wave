import 'package:flutter/material.dart';
import 'package:wave_frontend/models/message.dart';

class MessageList extends StatefulWidget {
  final List<Message> messages;

  MessageList({required this.messages});

  @override
  _MessageListState createState() => _MessageListState();
}


class _MessageListState extends State<MessageList> {
  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: widget.messages.length,
      itemBuilder: (context, index) {
        final message = widget.messages[index];
        return Container(
          margin: EdgeInsets.symmetric(vertical: 8.0, horizontal: 16.0),
          padding: EdgeInsets.all(12.0),
          decoration: BoxDecoration(
            color: Colors.grey[200],
            borderRadius: BorderRadius.circular(8.0),
          ),
          child: Text(
            message.content,
            style: TextStyle(fontSize: 16.0),
          ),
        );
      },
    );
  }
}
