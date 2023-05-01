import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ConversationScreen extends StatefulWidget {
  final String contactNumber;

  ConversationScreen({required this.contactNumber});

  @override
  _ConversationScreenState createState() => _ConversationScreenState();
}

class _ConversationScreenState extends State<ConversationScreen> {
  final MethodChannel _channel =
      const MethodChannel('com.example.receiving_test');
  final TextEditingController _messageController = TextEditingController();
  static const MethodChannel platform =
      const MethodChannel('com.example.receiving_test');

  Future<void> _sendMessage(String message) async {
    try {
      final Map<String, dynamic> sms = {
        'recipient': widget.contactNumber,
        'message': message,
        'timestamp': DateTime.now().toString(),
      };
      await _channel.invokeMethod('sendSms', sms);
      //setState(() {});
    } on PlatformException catch (e) {
      print("Failed to add message: '${e.message}'.");
    }
  }

  Future<List<Map>> _getConversation(
      String myNumber, String contactNumber) async {
    try {
      final List conversation =
          await platform.invokeMethod('getConversation', <String, dynamic>{
        'recipient': myNumber,
        'sender': contactNumber,
      });
      return List<Map>.from(conversation);
    } on PlatformException catch (e) {
      print("Failed to get conversation: '${e.message}'.");
      return [];
    }
  }

  Widget _buildMessageList() {
    return FutureBuilder<List<Map<dynamic, dynamic>>>(
      future: _getConversation('me', widget.contactNumber),
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
                return ListTile(
                  title: Text(sms['message']),
                  subtitle: Text(sms['timestamp']),
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
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.contactNumber),
      ),
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
                    decoration: InputDecoration(hintText: 'Type a message'),
                  ),
                ),
                IconButton(
                  icon: Icon(Icons.send),
                  onPressed: () {
                    _sendMessage(_messageController.text);
                    _messageController.clear();
                  },
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}