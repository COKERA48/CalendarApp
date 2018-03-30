package com.example.matts.calendarapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Matts on 3/29/2018.
 */

public class Contract {
    private Contract() {}

    public static final String CONTENT_AUTHORITY = "com.example.matts.calendarapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_VEHICLE = "reminder-path";

    public static final class TaskEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VEHICLE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;

        public final static String TABLE_NAME = "tasks";

        public final static String _ID = BaseColumns._ID;

        public static final String KEY_NAME = "task_name";
        public static final String KEY_START_DATE = "start_date";
        public static final String KEY_START_TIME = "start_time";
        public static final String KEY_END_DATE = "end_date";
        public static final String KEY_END_TIME = "end_time";
        public static final String KEY_REPEATS = "repeats";
        public static final String KEY_NOTES = "notes";

    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
}
