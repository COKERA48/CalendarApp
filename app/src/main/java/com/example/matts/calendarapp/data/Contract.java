package com.example.matts.calendarapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Matts on 3/29/2018.
 */

public class Contract {
    private Contract() {}

    static final String CONTENT_AUTHORITY = "com.example.matts.calendarapp";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_VEHICLE1 = "reminder-path";
    static final String PATH_VEHICLE2 = "category-path";
    static final String PATH_VEHICLE3 = "template-path";

    public static final class TaskEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VEHICLE1);

        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE1;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE1;

        static final String TABLE_NAME = "tasks";

        public final static String _ID1 = BaseColumns._ID;

        public static final String KEY_NAME = "task_name";
        public static final String KEY_START_DATE = "start_date";
        public static final String KEY_START_TIME = "start_time";
        public static final String KEY_END_DATE = "end_date";
        public static final String KEY_END_TIME = "end_time";
        public static final String KEY_REPEATS = "repeats";
        public static final String KEY_NOTES = "notes";
        public static final String KEY_ALARM_ID = "alarm_id";
        public static final String KEY_TIMESTAMP = "start_time_in_millis";

    }

    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VEHICLE2);
        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE2;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE2;

        final static String TABLE_NAME = "categories";

        public final static String _ID2 = BaseColumns._ID;
        public static final String KEY_NAME = "category_name";
        public static final String KEY_ICON = "icon";
        public static final String KEY_CREATED_BY_USER = "created_by_user";
        public static final String KEY_USAGE = "usage";
    }

    public static final class TemplateEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VEHICLE3);
        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE3;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE3;

        final static String TABLE_NAME = "templates";

        public final static String _ID3 = BaseColumns._ID;
        public static final String KEY_NAME = "template_name";
        public static final String KEY_REPEATS = "repeats";
        public static final String KEY_TEMP_CAT = "template_category";
        public static final String KEY_CREATED_BY_USER = "created_by_user";
        public static final String KEY_USAGE = "usage";
    }


}
