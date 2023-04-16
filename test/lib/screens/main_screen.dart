import 'package:flutter/material.dart';
import 'package:test/models/sms_message.dart';
import 'package:test/services/sms_service.dart';
import 'package:test/widgets/message_input.dart';
import 'package:test/widgets/phone_number_input.dart';
import 'package:test/widgets/send_button.dart';
import 'package:test/screens/sms_list_screen.dart';

class MainScreen extends StatefulWidget {
  @override
  _MainScreenState createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  final TextEditingController _messageController = TextEditingController();
  final TextEditingController _phoneNumberController = TextEditingController();
  final _formKey = GlobalKey<FormState>();
  final SmsService _smsService = SmsService();
  List<SmsMessage> _messages = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Basic SMS App'),
      ),
      body: Form(
        key: _formKey,
        child: SingleChildScrollView(
          padding: EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              PhoneNumberInput(
                controller: _phoneNumberController,
                labelText: 'Phone Number',
              ),
              MessageInput(
                controller: _messageController,
                labelText: 'Message',
              ),
              SendButton(
                onPressed: () async {
                  if (_formKey.currentState!.validate()) {
                    String phoneNumber = _phoneNumberController.text;
                    String message = _messageController.text;

                    try {
                      await _smsService.sendSms(phoneNumber, message);
                      SmsMessage smsMessage = SmsMessage(
                        sender: phoneNumber,
                        receiver: phoneNumber,
                        message: message,
                        timestamp: DateTime.now(),
                      );
                      setState(() {
                        _messages.add(smsMessage);
                      });
                      _messageController.clear();
                    } catch (error) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text('Error sending SMS: $error'),
                        ),
                      );
                    }
                  }
                },
              ),
              SizedBox(height: 20.0),
              Text(
                'Messages',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),
              Divider(),
              Container(
                height: MediaQuery.of(context).size.height * 0.5,
                child: SmsListScreen(messages: _messages),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
