  import 'package:flutter/material.dart';
  import 'package:flutter/services.dart';

  void main() {
    runApp(MyApp());
  }

  class MyApp extends StatelessWidget {
    @override
    Widget build(BuildContext context) {
      return MaterialApp(
        title: 'SMS Sender',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: SmsSender(),
      );
    }
  }

  class SmsSender extends StatefulWidget {
    @override
    _SmsSenderState createState() => _SmsSenderState();
  }

  class _SmsSenderState extends State<SmsSender> {
    final _phoneNumberController = TextEditingController();
    final _messageController = TextEditingController();

    Future<void> _sendSms() async {
      try {
        const platform = MethodChannel('com.example.receiving_test');
        final String phoneNumber = _phoneNumberController.text;
        final String message = _messageController.text;
        await platform.invokeMethod('sendSms', <String, dynamic>{
          'phoneNumber': phoneNumber,
          'message': message,
        });
      } on PlatformException catch (e) {
        print('Failed to send SMS: ${e.message}');
      }
    }

    @override
    Widget build(BuildContext context) {
      return Scaffold(
        appBar: AppBar(
          title: Text('SMS Sender'),
        ),
        body: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            children: <Widget>[
              TextField(
                controller: _phoneNumberController,
                decoration: InputDecoration(
                  labelText: 'Phone number',
                ),
                keyboardType: TextInputType.phone,
              ),
              TextField(
                controller: _messageController,
                decoration: InputDecoration(
                  labelText: 'Message',
                ),
                maxLines: null,
              ),
              SizedBox(height: 16.0),
              ElevatedButton(
                child: Text('Send SMS'),
                onPressed: _sendSms,
              ),
            ],
          ),
        ),
      );
    }

    @override
    void dispose() {
      _phoneNumberController.dispose();
      _messageController.dispose();
      super.dispose();
    }
  }
