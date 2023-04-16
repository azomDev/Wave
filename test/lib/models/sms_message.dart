class SmsMessage {
  final String sender;
  final String receiver;
  final String message;
  final DateTime timestamp;

  SmsMessage({
    required this.sender,
    required this.receiver,
    required this.message,
    required this.timestamp,
  });
}
