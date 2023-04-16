import 'dart:convert';
import 'package:flutter/cupertino.dart';
import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/web_socket_channel.dart';
import '../models/message.dart';

class MessagingService extends ChangeNotifier {
  final String _wsUrl = 'ws://127.0.0.1:8080/ws';
  WebSocketChannel? _channel;
  List<Message> messages = [];

  void connect() {
    _channel = IOWebSocketChannel.connect(_wsUrl);
    _channel!.stream.listen((event) {
      final decoded = jsonDecode(event);
      final message = Message.fromJson(decoded);
      messages.add(message);
      notifyListeners();
    });
  }

  void sendMessage(String messageText) {
    if (_channel == null) {
      return;
    }

    final message = Message(
      content: messageText,
      timestamp: DateTime.now(),
      id: '',
    );

    _channel!.sink.add(jsonEncode(message.toJson()));
  }

  void disposeChannel() {
    _channel?.sink.close();
    _channel = null;
  }

  @override
  void dispose() {
    super.dispose();
    disposeChannel();
  }
}
