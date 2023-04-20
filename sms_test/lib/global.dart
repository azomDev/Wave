import 'models/conversation.dart';

List<Conversation> conversations = [];

void handleIncomingMessage(String address, String messageBody) {
  for (Conversation conversation in conversations) {
    if (conversation.recipients.contains(address)) {
      conversation.messages.add('Received: ' + messageBody);
      break;
    }
  }
}
