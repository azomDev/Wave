import 'package:telephony/telephony.dart';

class Conversation {
  int? threadId;
  List<String> recipients;
  List<SmsMessage> messages;
  String snippet = "";

  void newMessage(SmsMessage message) {
    messages.add(message);
    snippet = message.body ?? snippet;
  }

  Conversation({
    this.snippet = "",
    this.threadId,
    required this.recipients,
    required this.messages,
  });
}
