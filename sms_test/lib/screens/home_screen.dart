import 'package:flutter/material.dart';
import 'package:flutter_sms_example/models/sms_provider.dart';
import 'package:provider/provider.dart';

import '/models/conversation.dart';
import '/widgets/conversation_item.dart';
import 'create_conversation_screen.dart';
import 'message_screen.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  Widget build(BuildContext context) {
    return Consumer<SmsProvider>(
      builder: (context, value, child) => Scaffold(
        appBar: AppBar(
          title: Text('SMS App'),
        ),
        body: ListView.builder(
          itemCount: value.conversations.length,
          itemBuilder: (context, index) {
            return ConversationItem(
              conversation: value.conversations[index],
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) =>
                        MessageScreen(conversation: value.conversations[index]),
                  ),
                );
              },
            );
          },
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () async {
            Conversation? newConversation = await Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => CreateConversationScreen(),
              ),
            );
            if (newConversation != null) {
              setState(() {
                value.conversations.add(newConversation);
              });
            }
          },
          child: Icon(Icons.add),
        ),
      ),
    );
  }
}
