import 'package:flutter/material.dart';
import '../screens/create_conversation_screen.dart';
import '../screens/message_screen.dart';
import '../widgets/conversation_item.dart';
import '../models/conversation.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  List<Conversation> conversations = []; // List of conversation objects

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('SMS App'),
      ),
      body: ListView.builder(
        itemCount: conversations.length,
        itemBuilder: (context, index) {
          return ConversationItem(
            conversation: conversations[index],
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) => MessageScreen(conversation: conversations[index]),
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
              conversations.add(newConversation);
            });
          }
        },
        child: Icon(Icons.add),
      ),
    );
  }
}
