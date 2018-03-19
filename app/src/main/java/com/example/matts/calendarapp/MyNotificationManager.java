package com.example.matts.calendarapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Matts on 3/16/2018.
 */

public class MyNotificationManager {
    /*
    private Context context;
    private static MyNotificationManager instance;

    private MyNotificationManager(Context ctx) {
        context = ctx;
    }

    public static synchronized MyNotificationManager getInstance(Context ctx) {
        if (instance == null)
            instance = new MyNotificationManager(ctx);
        return instance;
    }

    public void displayNotification(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.qm_launch_logo)
                .setContentTitle(title)
                .setContentText(body);

        //activity that will be opened when user clicks notification
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (myNotificationManager != null) {
            myNotificationManager.notify(1, builder.build());
        }
    }
    */
}
