import 'package:flutter/material.dart';
import 'package:flutter_phone_direct_caller/flutter_phone_direct_caller.dart';
import 'package:flutter_true_call_example/services/android_services.dart';

class ExamplePhoneDirectCaller extends StatefulWidget {
  const ExamplePhoneDirectCaller({Key? key}) : super(key: key);

  @override
  State<ExamplePhoneDirectCaller> createState() => _ExamplePhoneDirectCallerState();
}

class _ExamplePhoneDirectCallerState extends State<ExamplePhoneDirectCaller> {
  String _incomingCallNumber = '';

  @override
  void initState() {
    super.initState();
    getIncomingCallNumber().then((number) {
      print(number);
      setState(() {
        _incomingCallNumber = number;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Center(
            child: ElevatedButton(
              onPressed: () async {
                const number = '+84961783723';
                await FlutterPhoneDirectCaller.callNumber(number);
              },
              child: Text('Call number'),
            ),
          ),
          Center(
            child: InkWell(
              onTap: () {
                getIncomingCallNumber().then((number) {
                  print(number);
                  setState(() {
                    _incomingCallNumber = number;
                  });
                });
              },
              child: Text(
                'Incoming Call Number: $_incomingCallNumber',
                style: const TextStyle(fontSize: 20),
              ),
            ),
          )
        ],
      ),
    );
  }
}
