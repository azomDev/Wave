class Message {
  final String id;
  final String content;
  final DateTime timestamp;

  Message({required this.id, required this.content, required this.timestamp});

  // Method to create a Message object from a JSON map
  factory Message.fromJson(Map<String, dynamic> json) {
    return Message(
      id: json['id'],
      content: json['content'],
      timestamp: DateTime.parse(json['timestamp']),
    );
  }

  // Method to convert a Message object to a JSON map
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'content': content,
      'timestamp': timestamp.toIso8601String(),
    };
  }
}
