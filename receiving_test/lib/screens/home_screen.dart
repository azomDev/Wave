import 'package:flutter/material.dart';
import 'package:receiving_test/screens/conversation_screen.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  // Assuming a list of contact numbers for simplicity.
  // In a real app, this data should be fetched from the device's contacts or a database.
  final List<String> _contactNumbers = ['5147422074'];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('SMS Conversations'),
      ),
      body: ListView.builder(
        itemCount: _contactNumbers.length,
        itemBuilder: (BuildContext context, int index) {
          return ListTile(
            title: Text(_contactNumbers[index]),
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) =>
                      ConversationScreen(contactNumber: _contactNumbers[index]),
                ),
              );
            },
          );
        },
      ),
    );
  }
}
