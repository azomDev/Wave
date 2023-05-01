import 'package:flutter/material.dart';
import 'package:receiving_test/screens/conversation_screen.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final List<String> _contactNumbers = [];
  final TextEditingController _textEditingController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('SMS Conversations'),
      ),
      body: Column(
        children: <Widget>[
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextField(
              controller: _textEditingController,
              decoration: InputDecoration(
                labelText: 'Enter phone number',
                border: OutlineInputBorder(),
              ),
              keyboardType: TextInputType.phone,
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: _contactNumbers.length,
              itemBuilder: (BuildContext context, int index) {
                return ListTile(
                  title: Text(_contactNumbers[index]),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => ConversationScreen(
                            contactNumber: _contactNumbers[index]),
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
          setState(() {
            _contactNumbers.add(_textEditingController.text);
            _textEditingController.clear();
          });
        },
        child: Icon(Icons.add),
      ),
    );
  }
}
