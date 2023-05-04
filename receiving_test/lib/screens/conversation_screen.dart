import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';

class ConversationScreen extends StatefulWidget {
  final int conversationId;

  ConversationScreen({required this.conversationId});

  @override
  _ConversationScreenState createState() => _ConversationScreenState();
}

class _ConversationScreenState extends State<ConversationScreen> {
  final MethodChannel _channel =
      const MethodChannel('com.example.receiving_test');
  final TextEditingController _messageController = TextEditingController();
  static const MethodChannel platform =
      const MethodChannel('com.example.receiving_test');
  late int conversationId;

  Future<void> _sendMessage(String message) async {
    try {
      final Map<String, dynamic> sms = {
        'message': message,
        'conversationId': conversationId,
      };
      await _channel.invokeMethod('sendSms', sms);
      setState(() {}); // Add this line to trigger a rebuild
    } on PlatformException catch (e) {
      print("Failed to add message: '${e.message}'.");
    }
  }

  Future<List<Map>> _getConversation(int conversationId) async {
    try {
      final List conversation =
          await platform.invokeMethod('getConversation', <String, dynamic>{
        'conversationId': conversationId,
      });
      if (conversation.isEmpty) {
        print("There was no message loaded");
      }
      return List<Map>.from(conversation);
    } on PlatformException catch (e) {
      print("Failed to get conversation: '${e.message}'.");
      return [];
    }
  }

  Widget _buildMessageList() {
    return FutureBuilder<List<Map<dynamic, dynamic>>>(
      future: _getConversation(widget.conversationId),
      builder: (BuildContext context,
          AsyncSnapshot<List<Map<dynamic, dynamic>>> snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return CircularProgressIndicator();
        } else if (snapshot.hasError) {
          return Text('Error: ${snapshot.error}');
        } else if (snapshot.hasData) {
          final List<Map<dynamic, dynamic>>? smsList = snapshot.data;
          if (smsList != null && smsList.isNotEmpty) {
            return ListView.builder(
              itemCount: smsList.length,
              itemBuilder: (BuildContext context, int index) {
                final Map<dynamic, dynamic> sms = smsList[index];

                // Convert the timestamp to a DateTime object
                DateTime timestamp = DateTime.fromMillisecondsSinceEpoch(
                    int.parse(sms['timestamp']));
                String formattedTimestamp =
                    DateFormat('MMM d, y h:mm a').format(timestamp);

                return ListTile(
                  title: Text(
                    sms['message'],
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.w400,
                      color: Colors.white,
                    ),
                  ),
                  subtitle: Text(
                    formattedTimestamp,
                    style: TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.w300,
                      color: Colors.white70,
                    ),
                  ),
                );
              },
            );
          } else {
            return Text('No messages');
          }
        } else {
          return Text('No messages');
        }
      },
    );
  }

  @override
  void initState() {
    super.initState();
    conversationId = widget.conversationId; // Add this line
    _getConversation(widget.conversationId).then((conversation) {
      if (conversation.isNotEmpty) {
        setState(() {
          conversationId = conversation[0]['conversationId'];
        });
      }
    });
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
            widget.conversationId.toString(),
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.w500,
            ),
          ),
        ),
        backgroundColor: Color(0xFF2C2C2E), // Set the background color
        body: Column(
          children: <Widget>[
            Expanded(child: _buildMessageList()),
            Padding(
              padding: EdgeInsets.all(8.0),
              child: Row(
                children: <Widget>[
                  Expanded(
                    child: TextField(
                      controller: _messageController,
                      decoration: InputDecoration(
                        hintText: 'Type a message',
                        hintStyle: TextStyle(color: Colors.white),
                      ),
                      style: TextStyle(color: Colors.white),
                      onTap: () {
                        FocusScope.of(context).requestFocus();
                      },
                    ),
                  ),
                  IconButton(
                    icon: Icon(
                      Icons.send,
                      color: Colors.blue,
                    ),
                    onPressed: () {
                      if (_messageController.text.isNotEmpty) {
                        _sendMessage(_messageController.text);
                        _messageController.clear();
                      }
                    },
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
