package edu.nandboolean.incaseofemergency.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import edu.nandboolean.incaseofemergency.beans.EmergencyContact;

public class EmergencyContactRetriever {

    public static String getLookUpKeyFromContactUri(Context context, Uri contactUri) {
        String[] projections = new String[] {ContactsContract.Data.LOOKUP_KEY};
        Cursor lookupCursor = context.getContentResolver().query(contactUri, projections, null, null, null);
        if (lookupCursor != null && lookupCursor.moveToFirst()) {
            String lookupKey = lookupCursor.getString(lookupCursor.getColumnIndex(ContactsContract.Data.LOOKUP_KEY));
            lookupCursor.close();
            return lookupKey;
        }
        return "";
    }

    public static @Nullable EmergencyContact getContactBasicDetails(Context context, String lookupKey) {
        String whereClause = ContactsContract.Contacts.LOOKUP_KEY + "= ? AND " + ContactsContract.Data.MIMETYPE + "= ?";
        String[] whereClauseArgs = new String[]{lookupKey, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
        Cursor contactCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereClause, whereClauseArgs, null);
        if (contactCursor != null && contactCursor.getCount() > 0) {
            if (contactCursor.moveToFirst()) {
                String displayName = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNo = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactCursor.close();
                return EmergencyContact.getEmergencyContact().setName(displayName).setNumber(phoneNo);
            }
        }
        return null;
    }
}
