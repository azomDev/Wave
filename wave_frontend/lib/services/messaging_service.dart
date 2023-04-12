import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:wave_frontend/models/message.dart';

class MessagingService {
  // Replace with your own server's base URL
  final String baseUrl = 'http://127.0.0.1:8080';

  // Method to send a message to the backend
  Future<void> sendMessage(String content) async {
    final response = await http.post(
      Uri.parse('$baseUrl/messages/create'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'content': content}),
    );

    if (response.statusCode >= 400) {
      throw Exception('Failed to send message');
    }
  }

  // Method to retrieve messages from the backend
  Future<List<Message>> getMessages() async {
    final response = await http.get(Uri.parse('$baseUrl/messages'));

    if (response.statusCode == 200) {
      List jsonResponse = jsonDecode(response.body);
      return jsonResponse.map((message) => Message.fromJson(message)).toList();
    } else {
      throw Exception('Failed to load messages');
    }
    
  }
}
