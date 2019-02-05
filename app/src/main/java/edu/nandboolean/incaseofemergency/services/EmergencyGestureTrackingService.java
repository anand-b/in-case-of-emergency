package edu.nandboolean.incaseofemergency.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import edu.nandboolean.incaseofemergency.activities.MainActivity;
import edu.nandboolean.incaseofemergency.R;
import edu.nandboolean.incaseofemergency.broadcastreceivers.EmergencyGestureBroadcastReceiver;
import edu.nandboolean.incaseofemergency.utils.ICEConstants;

public class EmergencyGestureTrackingService extends Service {

    BroadcastReceiver emergencyGestureReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        this.emergencyGestureReceiver = new EmergencyGestureBroadcastReceiver();
        registerReceiver(emergencyGestureReceiver, filter);

        String channelId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = getChannelId("ICE");
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("In Case of Emergency listening for 5 consecutive power button presses in the background")
                .setContentText("Tap to stop.")
                .setSmallIcon(R.mipmap.ic_launcher);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(ICEConstants.EXTRA_SKIP_START_SERVICE, false);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        this.startForeground(this.getApplication().getApplicationInfo().uid, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getChannelId(String channelID) {
        NotificationChannel channel = new NotificationChannel(channelID, channelID, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setLightColor(Color.RED);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        return channelID;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(emergencyGestureReceiver);
    }
}
