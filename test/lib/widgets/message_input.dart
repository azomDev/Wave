import 'package:flutter/material.dart';

class MessageInput extends StatefulWidget {
  final TextEditingController controller;
  final String labelText;

  MessageInput({required this.controller, required this.labelText});

  @override
  _MessageInputState createState() => _MessageInputState();
}

class _MessageInputState extends State<MessageInput> {
  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(vertical: 10.0),
      child: TextFormField(
        controller: widget.controller,
        keyboardType: TextInputType.multiline,
        minLines: 1,
        maxLines: 5,
        decoration: InputDecoration(
          labelText: widget.labelText,
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(5.0),
          ),
        ),
        validator: (String? value) {
          if (value == null || value.isEmpty) {
            return 'Please enter a message';
          }
          return null;
        },
      ),
    );
  }
}
