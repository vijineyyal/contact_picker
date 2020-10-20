import 'dart:async';

import 'package:flutter/services.dart';

/// Entry point for the ContactPicker plugin.
///
/// Call [selectContact] to bring up a dialog where the user can pick a contact
/// from his/her address book.
class ContactPicker {
  static const MethodChannel _channel = const MethodChannel('contact_picker');

  /// Brings up a dialog where the user can select a contact from his/her
  /// address book.
  ///
  /// Returns the [Contact] selected by the user, or `null` if the user canceled
  /// out of the dialog.
  Future<Contact> selectContact() async {
    final Map<dynamic, dynamic> result =
        await _channel.invokeMethod('selectContact');
    if (result == null) {
      return null;
    }
    return new Contact.fromMap(result);
  }
}

/// Represents a contact selected by the user.
class Contact {
  Contact({this.email, this.phoneNumber});

  factory Contact.fromMap(Map<dynamic, dynamic> map) =>
      new Contact(email: map['emailId'], phoneNumber: map['phoneNumber']);

  /// The full name of the contact, e.g. "Dr. Daniel Higgens Jr.".
  final String email;

  /// The phone number of the contact.
  final String phoneNumber;
}
