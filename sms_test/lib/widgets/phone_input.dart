import 'package:flutter/material.dart';

class PhoneInput extends StatefulWidget {
  final Function(String) onRecipientAdded;
  final Function(String) onRecipientRemoved;

  PhoneInput(
      {required this.onRecipientAdded, required this.onRecipientRemoved});

  @override
  _PhoneInputState createState() => _PhoneInputState();
}

class _PhoneInputState extends State<PhoneInput> {
  TextEditingController _controllerPhoneNumber = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(Icons.people),
      title: TextField(
        controller: _controllerPhoneNumber,
        decoration: InputDecoration(labelText: 'Add Phone Number'),
        keyboardType: TextInputType.number,
        onChanged: (String value) => setState(() {}),
      ),
      trailing: IconButton(
        icon: Icon(Icons.add),
        onPressed: _controllerPhoneNumber.text.isEmpty
            ? null
            : () => setState(() {
                  widget
                      .onRecipientAdded(_controllerPhoneNumber.text.toString());
                  _controllerPhoneNumber.clear();
                }),
      ),
    );
  }
}
