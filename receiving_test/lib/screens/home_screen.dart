import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:receiving_test/screens/conversation_screen.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final List<int> _conversationIds = [];
  final TextEditingController _textEditingController = TextEditingController();
  static const MethodChannel _channel =
      const MethodChannel('com.example.receiving_test');

  Future<void> _fetchConversationIds() async {
    try {
      final List<dynamic> conversationIdsList =
          await _channel.invokeMethod('getAllConversations');
      final List<int> conversationIds = conversationIdsList.cast<int>();
      setState(() {
        _conversationIds.clear();
        _conversationIds.addAll(conversationIds);
      });
      for (var id in conversationIdsList) {
        print("Conversation with id: " + id.toString() + " has been loaded.");
      }
      if (conversationIdsList.isEmpty) {
        print("No conversations where loaded");
      }
    } on PlatformException catch (e) {
      print("Failed to fetch conversation IDs: '${e.message}'.");
    }
  }

  Future<void> _createConversation(String phoneNumber) async {
    try {
      // Create a new user and get the userId
      final int userId =
          await _channel.invokeMethod('createNewUser', <String, dynamic>{
        'phoneNumber': phoneNumber,
      });

      // Create a new conversation and get the conversationId
      final int conversationId =
          await _channel.invokeMethod('createNewConversation');

      // Create a new participant using the userId and conversationId
      await _channel.invokeMethod('createNewParticipant', <String, dynamic>{
        'userId': userId,
        'conversationId': conversationId,
      });

      // Add the conversationId to the _conversationIds list and trigger a rebuild
      setState(() {
        _conversationIds.add(conversationId);
      });
    } on PlatformException catch (e) {
      print("Failed to create conversation: '${e.message}'.");
    }
  }

  @override
  void initState() {
    super.initState();
    _fetchConversationIds();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        FocusScope.of(context).unfocus();
      },
      child: Scaffold(
        appBar: AppBar(
          title: Text(
            'SMS Conversations',
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.w500,
            ),
          ),
        ),
        backgroundColor: Color(0xFF2C2C2E), // Set the background color
        body: Column(
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: TextField(
                controller: _textEditingController,
                decoration: InputDecoration(
                  labelText: 'Enter phone number',
                  labelStyle: TextStyle(color: Colors.white),
                  enabledBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.white),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.blue),
                  ),
                ),
                style: TextStyle(color: Colors.white),
                keyboardType: TextInputType.phone,
                onTap: () {
                  FocusScope.of(context).requestFocus();
                },
              ),
            ),
            Expanded(
              child: ListView.builder(
                itemCount: _conversationIds.length,
                itemBuilder: (BuildContext context, int index) {
                  return ListTile(
                    title: Text(
                      'Conversation ${_conversationIds[index]}',
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.w400,
                        color: Colors.white,
                      ),
                    ),
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => ConversationScreen(
                              conversationId: _conversationIds[index]),
                        ),
                      );
                    },
                  );
                },
              ),
            ),
          ],
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            if (_textEditingController.text.isNotEmpty) {
              _createConversation(_textEditingController.text);
              _textEditingController.clear();
            }
          },
          child: Icon(Icons.add),
          backgroundColor: Colors.blue,
        ),
      ),
    );
  }
}
