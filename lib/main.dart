import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MainApp());
}

class MainApp extends StatefulWidget {
  const MainApp({super.key});

  @override
  State<StatefulWidget> createState() => _MainAppState();
}

class _MainAppState extends State<MainApp>{
  static const platform = MethodChannel("com.example.sleep_data_native_approach/health/test1");
  String data = 'no data';

  Future<void> _getHealthData() async {
    String healthData;
    try {
      final String result = await platform.invokeMethod("getHealthData");
      healthData = 'Heath data: $result';
    } on PlatformException catch (e) {
      healthData = "Failed to get health data: '${e.message}'";
    }

    setState(() {
      data = healthData;
    });
  }
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('Health Connect native approach')
        ),
        body: Center(
      child: Text(data),
      ),
        floatingActionButton: FloatingActionButton(
          onPressed: _getHealthData,
          child: Icon(Icons.health_and_safety),
        )
      ,
      ),
    );
  }

}