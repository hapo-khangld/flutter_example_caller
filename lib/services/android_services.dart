import 'package:flutter/services.dart';

const MethodChannel _channel = MethodChannel('call_plugin');

// Hàm để lấy thông tin số điện thoại gọi đến
Future<String> getIncomingCallNumber() async {
  final String? incomingNumber = await _channel.invokeMethod('getIncomingCallNumber');
  return incomingNumber ?? ''; // Xử lý giá trị null nếu cần thiết
}
