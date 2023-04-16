import 'package:telephony/telephony.dart';
import 'package:permission_handler/permission_handler.dart';

class SmsService {
  final Telephony telephony = Telephony.instance;

  Future<void> sendSms(String phoneNumber, String message) async {
    // Request permissions
    PermissionStatus status = await Permission.sms.request();

    if (status.isGranted) {
      SmsSendStatusListener listener = (SendStatus status) {
        print('SMS status: $status');
      };

      try {
        await telephony.sendSms(
          to: phoneNumber,
          message: message,
          statusListener: listener,
        );
      } catch (error) {
        print('Error sending SMS: $error');
        throw error;
      }
    } else {
      print('SMS permission not granted');
      // Handle permission not granted case
    }
  }
}
