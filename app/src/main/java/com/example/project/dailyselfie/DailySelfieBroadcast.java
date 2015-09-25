package com.example.project.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hoang on 25/09/2015.
 */
public class DailySelfieBroadcast extends BroadcastReceiver{

    public static int REQUEST_CODE = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    public void showNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, REQUEST_CODE, intent, 0);
        Notification.Builder mBuilder = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setContentTitle("Daily Selfie")
                .setContentText("Time for another selfie");
        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(REQUEST_CODE, mBuilder.build());
    }
}
