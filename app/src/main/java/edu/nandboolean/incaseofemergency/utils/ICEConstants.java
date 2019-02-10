package edu.nandboolean.incaseofemergency.utils;

public class ICEConstants {
    public static final int REQUEST_SELECT_CONTACT = 1;
    public static final String  EXTRA_SKIP_START_SERVICE = "skip_start_background_service";
    public static final int TAP_COUNT_FOR_SENDING_ALERT = 5;

    public static final String PACKAGE_NAME = "edu.nandboolean.incaseofemergency";

    public static final String EMERGENCY_CONTACT_NAME_PREF_KEY = PACKAGE_NAME + ".contactname";
    public static final String EMERGENCY_CONTACT_NUMBER_PREF_KEY = PACKAGE_NAME + ".contactnumber";
    public static final String DEFAULT_EMERGENCY_SMS_TEMPLATE = "EMERGENCY ALERT!! Please arrive ASAP to %s.\n(%s)";
}
