package edu.nandboolean.incaseofemergency.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import edu.nandboolean.incaseofemergency.ICEActivity;
import edu.nandboolean.incaseofemergency.R;
import edu.nandboolean.incaseofemergency.beans.EmergencyContact;
import edu.nandboolean.incaseofemergency.services.EmergencyGestureTrackingService;
import edu.nandboolean.incaseofemergency.utils.EmergencyContactRetriever;
import edu.nandboolean.incaseofemergency.utils.ICEAlertRaiser;
import edu.nandboolean.incaseofemergency.utils.ICEConstants;
import edu.nandboolean.incaseofemergency.widgets.EmergencyButton;

public class MainActivity extends ICEActivity
        implements View.OnClickListener, EmergencyButton.MultiTapListener {
    Button changeContactBtn;
    EmergencyButton sosBtn;

    QuickContactBadge quickContactBadge;
    TextView emergencyContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sosBtn = findViewById(R.id.btn_sos);
        sosBtn.setOnMultiTapListener(this);


        changeContactBtn = findViewById(R.id.btn_change_contact);
        changeContactBtn.setOnClickListener(this);

        quickContactBadge = findViewById(R.id.emergency_contact_badge);
        emergencyContact = findViewById(R.id.emergency_contact_tv);
        // TODO : Set the quickContactBadge and emergency contact details from preference, if available.
        EmergencyContact contact = getEmergencyContactFromPreference();
        updateEmergencyContactDetailsInUI(contact);

        Intent emergencyService = new Intent(this, EmergencyGestureTrackingService.class);
        stopService(emergencyService); // Better to stop a service even if it isn't started...

        Intent receivedIntent = getIntent();
        if (!receivedIntent.hasExtra(ICEConstants.EXTRA_SKIP_START_SERVICE)) {
            startService(emergencyService);
        }
    }

    @Override
    protected void onFailedToObtainLocationPermission() {
        Toast.makeText(this, "Location Permission denied.", Toast.LENGTH_LONG).show();
        sosBtn.setEnabled(false);
        changeContactBtn.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ICEConstants.REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contactUri = data.getData();
                if (contactUri != null) {
                    // TODO : Set the quickContactBadge
                    String lookupKey = EmergencyContactRetriever.getLookUpKeyFromContactUri(this, contactUri);
                    EmergencyContact contact = EmergencyContactRetriever.getContactBasicDetails(this, lookupKey);
                    if (contact != null) {
                        // quickContactBadge.assignContactUri(contactUri);
                        updateEmergencyContactDetailsInUI(contact);
                        saveEmergencyContactInPreference(contact);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateEmergencyContactDetailsInUI(EmergencyContact contact) {
        if (contact != null) {
            emergencyContact.setText(contact.toString());
        }
    }

    private void saveEmergencyContactInPreference(EmergencyContact contact) {
        SharedPreferences preferences = getSharedPreferences(ICEConstants.PACKAGE_NAME, MODE_PRIVATE);
        preferences
                .edit()
                .putString(ICEConstants.EMERGENCY_CONTACT_NAME_PREF_KEY, contact.getName())
                .putString(ICEConstants.EMERGENCY_CONTACT_NUMBER_PREF_KEY, contact.getNumber())
                .apply();
    }

    private @Nullable EmergencyContact getEmergencyContactFromPreference() {
        SharedPreferences preferences = getSharedPreferences(ICEConstants.PACKAGE_NAME, MODE_PRIVATE);
        String name = preferences.getString(ICEConstants.EMERGENCY_CONTACT_NAME_PREF_KEY, null);
        String number = preferences.getString(ICEConstants.EMERGENCY_CONTACT_NUMBER_PREF_KEY, null);
        if (name != null && number != null) {
            return EmergencyContact.getEmergencyContact().setName(name).setNumber(number);
        }
        return null;
    }

    private void openContactPicker() {
        Intent changeContactIntent = new Intent(Intent.ACTION_PICK);
        changeContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (changeContactIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(changeContactIntent, ICEConstants.REQUEST_SELECT_CONTACT);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_change_contact) {
            openContactPicker();
        }
    }

    @Override
    public void onMultiTap(View view) {
        Location recentLocation = this.locationTracker.getLastKnownLocation();
        ICEAlertRaiser.raiseAlert(this, recentLocation);
    }
}
