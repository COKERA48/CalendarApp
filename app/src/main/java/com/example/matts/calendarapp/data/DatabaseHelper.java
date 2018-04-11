package com.example.matts.calendarapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.matts.calendarapp.R;
import com.example.matts.calendarapp.data.Contract;

/**
 * Created by Matts on 2/18/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME = "quick_mention_db";
    private static final int DB_VERSION = 35;

    DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        /* Create task table */
        String createTableTask = "CREATE TABLE " + Contract.TaskEntry.TABLE_NAME + " (" +
                Contract.TaskEntry._ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.TaskEntry.KEY_NAME + " TEXT, " +
                Contract.TaskEntry.KEY_START_DATE + " TEXT, " +
                Contract.TaskEntry.KEY_START_TIME + " TEXT, " +
                Contract.TaskEntry.KEY_END_DATE + " TEXT, " +
                Contract.TaskEntry.KEY_END_TIME + " TEXT, " +
                Contract.TaskEntry.KEY_REPEATS + " TEXT, " +
                Contract.TaskEntry.KEY_NOTES + " TEXT, " +
                Contract.TaskEntry.KEY_ALARM_ID + " INTEGER, " +
                Contract.TaskEntry.KEY_TIMESTAMP + " INTEGER)";
        db.execSQL(createTableTask);

        String createTableCategory = "CREATE TABLE " + Contract.CategoryEntry.TABLE_NAME + " (" +
                Contract.CategoryEntry._ID2 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.CategoryEntry.KEY_NAME + " TEXT, " +
                Contract.CategoryEntry.KEY_ICON + " INTEGER)";
        db.execSQL(createTableCategory);


        String createTableTemplate = "CREATE TABLE " + Contract.TemplateEntry.TABLE_NAME + " (" +
                Contract.TemplateEntry._ID3 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.TemplateEntry.KEY_NAME + " TEXT, " +
                Contract.TemplateEntry.KEY_REPEATS + " TEXT, " +
                Contract.TemplateEntry.KEY_TEMP_CAT + " INTEGER, " +
                "FOREIGN KEY (" + Contract.TemplateEntry.KEY_TEMP_CAT + ") REFERENCES " + Contract.CategoryEntry.TABLE_NAME + "(" + Contract.CategoryEntry._ID2 + "))";
        db.execSQL(createTableTemplate);

        /* Insert values into category table */
        db.execSQL("INSERT INTO " + Contract.CategoryEntry.TABLE_NAME + "(" + Contract.CategoryEntry.KEY_NAME + ", " + Contract.CategoryEntry.KEY_ICON + ") VALUES ('Home', " + R.drawable.ic_home + ")");
        db.execSQL("INSERT INTO " + Contract.CategoryEntry.TABLE_NAME + "(" + Contract.CategoryEntry.KEY_NAME + ", " + Contract.CategoryEntry.KEY_ICON + ") VALUES ('Auto', " + R.drawable.ic_auto + ")");
        db.execSQL("INSERT INTO " + Contract.CategoryEntry.TABLE_NAME + "(" + Contract.CategoryEntry.KEY_NAME + ", " + Contract.CategoryEntry.KEY_ICON + ") VALUES ('Health', " + R.drawable.ic_health + ")");
        db.execSQL("INSERT INTO " + Contract.CategoryEntry.TABLE_NAME + "(" + Contract.CategoryEntry.KEY_NAME + ", " + Contract.CategoryEntry.KEY_ICON + ") VALUES ('My Templates', " + R.drawable.ic_person + ")");




        /* Insert values into template table */
        db.execSQL("INSERT INTO " + Contract.TemplateEntry.TABLE_NAME + "(" + Contract.TemplateEntry.KEY_NAME + ", " +
                Contract.TemplateEntry.KEY_REPEATS + ", " + Contract.TemplateEntry.KEY_TEMP_CAT + ") VALUES ('Do Laundry', 'Every Week', 1 )");
        db.execSQL("INSERT INTO " + Contract.TemplateEntry.TABLE_NAME + "(" + Contract.TemplateEntry.KEY_NAME + ", " +
                Contract.TemplateEntry.KEY_REPEATS + ", " + Contract.TemplateEntry.KEY_TEMP_CAT + ") VALUES ('Mow Lawn', 'Every 2 Weeks', 1 )");
        db.execSQL("INSERT INTO " + Contract.TemplateEntry.TABLE_NAME + "(" + Contract.TemplateEntry.KEY_NAME + ", " +
                Contract.TemplateEntry.KEY_REPEATS + ", " + Contract.TemplateEntry.KEY_TEMP_CAT + ") VALUES ('Wash Car', 'Every Week', 2 )");
        db.execSQL("INSERT INTO " + Contract.TemplateEntry.TABLE_NAME + "(" + Contract.TemplateEntry.KEY_NAME + ", " +
                Contract.TemplateEntry.KEY_REPEATS + ", " + Contract.TemplateEntry.KEY_TEMP_CAT + ") VALUES ('Drink Water', 'Every Day', 3 )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.TaskEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.TemplateEntry.TABLE_NAME);
        onCreate(db);

    }




}
