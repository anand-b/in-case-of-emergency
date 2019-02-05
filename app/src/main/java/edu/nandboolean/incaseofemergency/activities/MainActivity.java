package edu.nandboolean.incaseofemergency.activities;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import edu.nandboolean.incaseofemergency.ICEActivity;
import edu.nandboolean.incaseofemergency.R;
import edu.nandboolean.incaseofemergency.services.EmergencyGestureTrackingService;
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
            Uri contactUri = data.getData();
            // TODO : Set the quickContactBadge
            // TODO : Set emergency contact details
            // quickContactBadge.assignContactUri(contactUri);
            updateEmergencyContactDetailsInUI();
            // TODO : Save contact to preference
            saveEmergencyContactInPreference();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateEmergencyContactDetailsInUI() {

    }

    private void saveEmergencyContactInPreference() {

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
