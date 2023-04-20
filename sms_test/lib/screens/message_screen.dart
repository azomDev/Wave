import 'package:flutter/material.dart';
import 'package:flutter_sms/flutter_sms.dart';
import '../models/conversation.dart';
import 'package:telephony/telephony.dart';


onBackgroundMessage(SmsMessage message) {
  debugPrint("onBackgroundMessage called");
}

class MessageScreen extends StatefulWidget {
  final Conversation conversation;

  MessageScreen({required this.conversation});

  @override
  _MessageScreenState createState() => _MessageScreenState();
}

class _MessageScreenState extends State<MessageScreen> {
  TextEditingController _controllerMessage = TextEditingController();
  String? _message;
  final Telephony telephony = Telephony.instance;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  onMessage(SmsMessage message) async {
    setState(() {
      _message = message.body ?? "Error reading message body.";
      widget.conversation.messages
          .add('Received: ' + (_message ?? 'No message body'));
    });
  }

  Future<void> initPlatformState() async {
    // Platform messages may fail, so we use a try/catch PlatformException.
    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.

    final bool? result = await telephony.requestPhoneAndSmsPermissions;

    if (result != null && result) {
      telephony.listenIncomingSms(
          onNewMessage: onMessage, onBackgroundMessage: onBackgroundMessage);
    }

    if (!mounted) return;
  }

  Future<void> _sendSMS(List<String> recipients) async {
    try {
      String _result = await sendSMS(
        message: _controllerMessage.text,
        recipients: recipients,
        sendDirect: true,
      );
      setState(() {
        _message = _result;
        widget.conversation.messages.add('Sent: ' + _controllerMessage.text);
        _controllerMessage.clear();
      });
    } catch (error) {
      setState(() {
        _message = error.toString();
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
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
                  title: Text(widget.conversation.messages[index]),
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
                  onPressed: () {
                    if (widget.conversation.recipients.isNotEmpty) {
                      _sendSMS(widget.conversation.recipients);
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
    );
  }
}
