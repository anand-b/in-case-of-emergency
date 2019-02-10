package edu.nandboolean.incaseofemergency.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.nandboolean.incaseofemergency.R;
import edu.nandboolean.incaseofemergency.beans.EmergencyContact;

public class ICEAlertRaiser {

    public static void raiseAlert(Context context, Location location) {
        EmergencyContact contact = EmergencyContact.getEmergencyContact();
        if (contact != null && contact.getName() != null && contact.getNumber() != null) {
            if (location != null) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    Address address = addresses.get(0);
                    String city = address.getLocality();
                    String adminArea = address.getSubAdminArea();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String addressString = city + ", " + adminArea + ", " + state + ", " + country + ", " + postalCode;
                    String coords = Double.toString(location.getLatitude())+","+Double.toString(location.getLongitude());
                    String sms = String.format(ICEConstants.DEFAULT_EMERGENCY_SMS_TEMPLATE,  addressString, coords);
                    sendSMS(context, contact, sms);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.error_detect_location), Toast.LENGTH_LONG).show();
            }
        }
    }

    private static void sendSMS(Context context, EmergencyContact contact, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contact.getNumber(), null, message, null, null);
            Toast.makeText(context, "Successfully alerted " + contact.getName(), Toast.LENGTH_LONG).show();
            vibrateDevice(context);
        } catch (Exception e) {
            Toast.makeText(context, "ERROR! Could not alert " + contact.getName(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private static void vibrateDevice(Context context) {
        Vibrator emergencyAlertVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            emergencyAlertVibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            emergencyAlertVibrator.vibrate(500);
        }
    }
}
