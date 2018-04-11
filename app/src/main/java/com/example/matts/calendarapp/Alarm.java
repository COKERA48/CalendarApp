package com.example.matts.calendarapp;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import com.example.matts.calendarapp.data.Contract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Matts on 3/18/2018.
 */

public class Alarm extends BroadcastReceiver {
    private static final String TAG = "Alarm";
    private String taskName, startDate, startTime, endDate, endTime, repeats, notes;

    private long interval, initialTime, timestamp;
    private int alarmId;
    Calendar c;
    private Uri mCurrentReminderUri;
    @Override
    public void onReceive(Context context, Intent intent) {
        c = Calendar.getInstance();
        mCurrentReminderUri = intent.getData();

        Bundle bundle = intent.getExtras();
        if (bundle != null ) {
            interval = bundle.getLong("interval");
           // alarmId = bundle.getInt("alarmId");
            initialTime = bundle.getLong("initialTime");
        }

        ContentResolver mResolver = context.getContentResolver();
        Cursor cursor = mResolver.query(mCurrentReminderUri, new String[] {
                        Contract.TaskEntry.KEY_NAME,
                        Contract.TaskEntry.KEY_START_DATE,
                        Contract.TaskEntry.KEY_START_TIME,
                        Contract.TaskEntry.KEY_END_DATE,
                        Contract.TaskEntry.KEY_END_TIME,
                        Contract.TaskEntry.KEY_REPEATS,
                        Contract.TaskEntry.KEY_NOTES,
                        Contract.TaskEntry.KEY_ALARM_ID,
                        Contract.TaskEntry.KEY_TIMESTAMP},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            taskName = cursor.getString(cursor.getColumnIndex(Contract.TaskEntry.KEY_NAME));
            startDate = cursor.getString(cursor.getColumnIndex(Contract.TaskEntry.KEY_START_DATE));
            startTime = cursor.getString(cursor.getColumnIndex(Contract.TaskEntry.KEY_START_TIME));
            endDate = cursor.getString(cursor.getColumnIndex(Contract.TaskEntry.KEY_END_DATE));
            endTime = cursor.getString(cursor.getColumnIndex(Contract.TaskEntry.KEY_END_TIME));
            repeats = cursor.getString(cursor.getColumnIndex(Contract.TaskEntry.KEY_REPEATS));
            notes = cursor.getString(cursor.getColumnIndex(Contract.TaskEntry.KEY_NOTES));
            alarmId = cursor.getInt(cursor.getColumnIndex(Contract.TaskEntry.KEY_ALARM_ID));
            timestamp = cursor.getLong(cursor.getColumnIndex(Contract.TaskEntry.KEY_TIMESTAMP));
            cursor.close();
        }

        Log.d(TAG, "Alarm worked." + c.getTime().toString() + " initialTime: " + initialTime + " interval: " + interval + " timestamp: " + timestamp + " alarmId: " + alarmId);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Builder builder = new Builder(context)
                .setContentIntent(pendingIntent)
                .setContentTitle(taskName)
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.ic_notification);
            builder.setColor(context.getResources().getColor(R.color.colorAccent2));
        } else {
            builder.setSmallIcon(R.drawable.ic_notification);
        }
        notificationManager.notify(alarmId,builder.build());

        if (interval != 0) {
            try {
                rescheduleAlarm(context);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void rescheduleAlarm(Context context) throws ParseException {

        initialTime += interval;
        c.setTimeInMillis(initialTime);
        timestamp = c.getTimeInMillis();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        DateFormat tf = new SimpleDateFormat("hh:mm a", Locale.US);

        startDate = df.format(c.getTime());
        startTime = tf.format(c.getTime());

        DateFormat dtf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);

        Date end = dtf.parse(endDate + " " + endTime);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(end.getTime());
        long endTimeInMillis = c2.getTimeInMillis();
        endTimeInMillis += interval;
        c2.setTimeInMillis(endTimeInMillis);
        endDate = df.format(c2.getTime());

        endTime = tf.format(c2.getTime());
        Log.d(TAG, "endDate: " + endDate + " endTime: " + endTime);
        ContentValues values = new ContentValues();

        values.put(Contract.TaskEntry.KEY_NAME, taskName);
        values.put(Contract.TaskEntry.KEY_START_DATE, startDate);
        values.put(Contract.TaskEntry.KEY_START_TIME, startTime);
        values.put(Contract.TaskEntry.KEY_END_DATE, endDate);
        values.put(Contract.TaskEntry.KEY_END_TIME, endTime);
        values.put(Contract.TaskEntry.KEY_REPEATS, repeats);
        values.put(Contract.TaskEntry.KEY_NOTES, notes);
        values.put(Contract.TaskEntry.KEY_ALARM_ID, alarmId);
        values.put(Contract.TaskEntry.KEY_TIMESTAMP, timestamp);

        Intent intent = new Intent(context.getApplicationContext(), Alarm.class);
        Uri newUri = context.getContentResolver().insert(Contract.TaskEntry.CONTENT_URI, values);
        intent.setData(newUri);

        intent.putExtra("initialTime", initialTime);
        intent.putExtra("interval", interval);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, initialTime, pendingIntent);

            }
            else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, initialTime, pendingIntent);
            }
        }
    }

}
