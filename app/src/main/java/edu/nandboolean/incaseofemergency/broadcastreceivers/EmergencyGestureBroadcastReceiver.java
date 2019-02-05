package edu.nandboolean.incaseofemergency.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import edu.nandboolean.incaseofemergency.utils.ICEAlertRaiser;
import edu.nandboolean.incaseofemergency.utils.ICEConstants;
import edu.nandboolean.incaseofemergency.utils.ICEEventLogger;
import edu.nandboolean.incaseofemergency.utils.UserLocationTracker;

public class EmergencyGestureBroadcastReceiver extends BroadcastReceiver {

    private ICEEventLogger broadcastEventLogger;

    public EmergencyGestureBroadcastReceiver() {
        super();
        broadcastEventLogger = new ICEEventLogger();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        broadcastEventLogger.logEvent();
        if (broadcastEventLogger.getCurrentEventCount() == ICEConstants.TAP_COUNT_FOR_SENDING_ALERT) {
            ICEAlertRaiser.raiseAlert(context, UserLocationTracker.getLocationTracker(context).getLastKnownLocation());
            broadcastEventLogger.clear();
        }
    }
}
