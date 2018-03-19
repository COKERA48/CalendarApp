package com.example.matts.calendarapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Matts on 3/18/2018.
 */

public class Alarm extends BroadcastReceiver {
    private static final String TAG = "Alarm";
    private String taskName;
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar c = Calendar.getInstance();
        Log.d(TAG, "Alarm worked." + c.getTime().toString());
        Bundle bundle = intent.getExtras();
        if (bundle != null ) {
            taskName = bundle.getString("taskName");
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Builder builder = new Builder(context)
                .setContentIntent(pendingIntent)
                .setContentText(taskName)
                .setContentTitle("Quick Mention")
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.ic_notification);
            builder.setColor(context.getResources().getColor(R.color.colorAccent2));
        } else {
            builder.setSmallIcon(R.drawable.ic_notification);
        }
        notificationManager.notify(100,builder.build());


    }
}
