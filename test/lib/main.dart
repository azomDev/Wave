import 'package:flutter/material.dart';
import 'package:test/screens/main_screen.dart';

void main() {
  runApp(SmsApp());
}

class SmsApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Basic SMS App',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MainScreen(),
    );
  }
}
