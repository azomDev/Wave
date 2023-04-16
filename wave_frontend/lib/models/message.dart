class Message {
  final String id;
  final String content;
  final DateTime timestamp;

  Message({required this.id, required this.content, required this.timestamp});

  // Method to create a Message object from a JSON map
  factory Message.fromJson(Map<String, dynamic> json) {
    return Message(
      id: json['id'].toString(),
      content: json['content'],
      timestamp: json['timestamp'] != null
        ? DateTime.parse(json['timestamp'])
        : DateTime.now(), // Use the current time as a default value
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
