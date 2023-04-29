import 'package:flutter/material.dart';
import 'package:flutter_sms/flutter_sms.dart';
import 'package:flutter_sms_example/models/sms_provider.dart';
import 'package:provider/provider.dart';
import '../models/conversation.dart';
import 'package:telephony/telephony.dart';

class MessageScreen extends StatefulWidget {
  final Conversation conversation;

  MessageScreen({required this.conversation});

  @override
  _MessageScreenState createState() => _MessageScreenState();
}

class _MessageScreenState extends State<MessageScreen> {
  TextEditingController _controllerMessage = TextEditingController();
  String? _message;

  @override
  Widget build(BuildContext context) {
    return Consumer<SmsProvider>(
      builder: (context, provider, child) => Scaffold(
        appBar: AppBar(
          title: Text(widget.conversation.recipients.join(', ')),
          leading: IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () {
              Navigator.pop(context);
            },
          ),
        ),
        body: Column(
          children: [
            Expanded(
              child: ListView.builder(
                itemCount: widget.conversation.messages.length,
                itemBuilder: (context, index) {
                  return ListTile(
                    title: Text(
                      widget.conversation.messages[index].body ?? "No Message",
                    ),
                  );
                },
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 8.0),
              child: Row(
                children: [
                  Expanded(
                    child: TextField(
                      decoration: InputDecoration(
                        labelText: 'Message',
                        border: OutlineInputBorder(),
                      ),
                      controller: _controllerMessage,
                      onChanged: (String value) => setState(() {}),
                    ),
                  ),
                  IconButton(
                    icon: Icon(Icons.send),
                    onPressed: () async {
                      if (widget.conversation.recipients.isNotEmpty) {
                        await provider.send(
                          _controllerMessage.text,
                          widget.conversation,
                        );
                        _controllerMessage.clear();
                      } else {
                        setState(
                            () => _message = 'At Least 1 Recipient Required');
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
