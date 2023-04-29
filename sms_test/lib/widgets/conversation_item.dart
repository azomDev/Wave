import 'package:flutter/material.dart';
import '../models/conversation.dart';

class ConversationItem extends StatelessWidget {
  final Conversation conversation;
  final Function() onTap;

  const ConversationItem({required this.conversation, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(conversation.recipients.join(', ')),
      subtitle: Text(conversation.snippet),
      onTap: onTap,
    );
  }
}
