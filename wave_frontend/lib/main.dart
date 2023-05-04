import 'package:flutter/material.dart';
import 'package:wave_frontend/screens/home_screen.dart';
import 'package:wave_frontend/theme/app_theme.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Simple Messaging App',
      theme: AppTheme.lightTheme(),
      home: HomeScreen(),
    );
  }
}
