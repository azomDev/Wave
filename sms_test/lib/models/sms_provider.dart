import 'package:flutter/material.dart';
import 'package:flutter_sms/flutter_sms.dart';
import 'package:flutter_sms_example/models/conversation.dart';
import 'package:telephony/telephony.dart';

onBackgroundMessage(SmsMessage message) {}

class SmsProvider extends ChangeNotifier {
  SmsProvider() {
    Telephony.instance.requestPhoneAndSmsPermissions.then(
      (result) async {
        if (result == null || !result) {
          print("NO SMS PERMISSION");
          return;
        }

        final threads = (await Telephony.instance.getConversations())
            .map((e) => e.threadId);

        for (var thread in threads) {
          final messages = await Telephony.instance.getInboxSms(
              filter: SmsFilter.where(SmsColumn.THREAD_ID).equals("$thread"));
          if (messages.length > 0)
            _conversations.add(
              Conversation(
                threadId: thread,
                recipients: [messages.first.address ?? "oof"],
                messages: messages,
              ),
            );
        }

        notifyListeners();

        Telephony.instance.listenIncomingSms(
          onNewMessage: newMessage,
          onBackgroundMessage: onBackgroundMessage,
        );
      },
    );
  }

  void newMessage(SmsMessage message) {
    _conversations
        .firstWhere((element) => element.threadId == message.threadId)
        .newMessage(message);
    notifyListeners();
  }

  void createConversation(List<String> recipients) {}

  Future<bool> send(String message, Conversation conversation) async {
    try {
      String _result = await sendSMS(
        message: message,
        recipients: conversation.recipients,
        sendDirect: true,
      );

      print(_result);

      final sms = (await Telephony.instance.getSentSms(
        filter: SmsFilter.where(SmsColumn.THREAD_ID)
            .equals("${conversation.threadId}"),
      ))
          .first;

      print(sms.body);

      conversation.newMessage(sms);

      notifyListeners();
      return true;
    } catch (error) {
      return false;
    }
  }

  get conversations => _conversations;
}

final _conversations = <Conversation>[];
