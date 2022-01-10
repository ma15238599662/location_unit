// ignore_for_file: avoid_print

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:location/location.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  int _count = 0;
  List _satellites = [];
  Map locationData = {};
  @override
  void initState() {
    super.initState();
    Location.onListenData(_onLocationEvent, _onLocationError);
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await Location.platformVersion ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    await Location.eventMessage();

    await Location.open();

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  void _onLocationEvent(dynamic event) {
    // print(event['satellites']);
    var data = Map<String, dynamic>.from(event);
    setState(() {
      _count = data["satellite_count"] ?? 0;
      _satellites = data["satellites"] ?? [];
      locationData = data;
    });
  }

  void _onLocationError(dynamic error) {
    print(error);
  }

  // telephony: ${locationData['telephony']} \n
  // sensor: ${locationData['sensor']} \n

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: ListView(children: [
          Text("""
            Running on: $_platformVersion\n 
            GPS: $_count\n 
            theta: ${locationData['sensor']?['theta']} \n
            theta: ${locationData['sensor']?['theta']} \n

          """),
          ..._list()
        ]),
      ),
    );
  }

  List<Widget> _list() {
    final List temp = _satellites;
    return temp.map((item) => _item(item)).toList();
  }

  Widget _item(item) {
    return SizedBox(
        child: Row(children: [
      Expanded(child: Text("""
              GPS_satellite_id: ${item['GPS_satellite_id']}; \n 
              signal_SNR: ${item['signal_SNR']}; \n 
              satellite_elevation_angle: ${item['satellite_elevation_angle']};\n  

              BS_carrier_frequency: ${item['BS_carrier_frequency']}; \n 
              BS_signal_strength: ${item['BS_signal_strength']}; \n 
              elevation_angle_of_phone: ${item['elevation_angle_of_phone']}; \n 
              
              item : $item \n \n
              
              """))
    ]));
  }
}
