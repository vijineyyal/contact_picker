package net.goderbauer.flutter.contactpicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.HashMap;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import static android.app.Activity.RESULT_OK;

public class ContactPickerPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener {
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "contact_picker");
    ContactPickerPlugin instance = new ContactPickerPlugin(registrar.activity());
    registrar.addActivityResultListener(instance);
    channel.setMethodCallHandler(instance);
  }

    private ContactPickerPlugin(Activity activity) {
        this.activity = activity;
    }

  private static int PICK_CONTACT = 2015;

  private Activity activity;
  private Result pendingResult;
  String phone="11";
String email="@";
int phonedx=0;
int emailIdx=0;

   @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("selectContact")) {
      if (pendingResult == null) {
        pendingResult = result;
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        activity.startActivityForResult(i, PICK_CONTACT);
      } else {
      }
    } else {
      result.notImplemented();
    }
  }

  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode != PICK_CONTACT) {
      return false;
    }
    if (resultCode != RESULT_OK) {
      pendingResult.success(null);
      pendingResult = null;
      return true;
    }
    			Uri contactUri = data.getData();

                        String id = contactUri.getLastPathSegment();
 			Cursor cursor2 = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[] { id },
                                null);
                        Cursor cursor3 = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[] { id },
                                null);
                        phonedx = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);
 			emailIdx = cursor3.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);


                        if (cursor2.moveToFirst()) {
                           phone = cursor2.getString(phonedx);
                        }
			else{phone="";}
			if (cursor3.moveToFirst()) {
			email = cursor3.getString(emailIdx);  
                        }
			else{email="";}

    



    HashMap<String, Object> contact = new HashMap<>();
    contact.put("phoneNumber", phone+"");
    contact.put("emailId", email+"");

    pendingResult.success(contact);
    pendingResult = null;
    return true;
  }
}
