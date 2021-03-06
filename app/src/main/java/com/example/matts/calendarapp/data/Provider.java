package com.example.matts.calendarapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Matts on 3/29/2018.
 */

public class Provider extends ContentProvider{
    public static final String LOG_TAG = Provider.class.getSimpleName();

    private static final int REMINDER = 100;
    private static final int REMINDER_ID = 101;

    private static final int CATEGORY = 200;
    private static final int CATEGORY_ID = 201;

    private static final int TEMPLATE = 300;
    private static final int TEMPLATE_ID = 301;



    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_VEHICLE1, REMINDER);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_VEHICLE1 + "/#", REMINDER_ID);

        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_VEHICLE2, CATEGORY);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_VEHICLE2 + "/#", CATEGORY_ID);

        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_VEHICLE3, TEMPLATE);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_VEHICLE3 + "/#", TEMPLATE_ID);

    }

    private DatabaseHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                cursor = database.query(Contract.TaskEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case REMINDER_ID:
                selection = Contract.TaskEntry._ID1 + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(Contract.TaskEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CATEGORY:
                cursor = database.query(Contract.CategoryEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CATEGORY_ID:
                selection = Contract.CategoryEntry._ID2 + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(Contract.CategoryEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TEMPLATE:
                cursor = database.query(Contract.TemplateEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TEMPLATE_ID:
                selection = Contract.TemplateEntry._ID3 + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(Contract.TemplateEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return Contract.TaskEntry.CONTENT_LIST_TYPE;
            case REMINDER_ID:
                return Contract.TaskEntry.CONTENT_ITEM_TYPE;
            case CATEGORY:
                return Contract.CategoryEntry.CONTENT_LIST_TYPE;
            case CATEGORY_ID:
                return Contract.CategoryEntry.CONTENT_ITEM_TYPE;
            case TEMPLATE:
                return Contract.TemplateEntry.CONTENT_LIST_TYPE;
            case TEMPLATE_ID:
                return Contract.TemplateEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return insertReminder(uri, contentValues);
            case CATEGORY:
                return insertCategory(uri, contentValues);
            case TEMPLATE:
                return insertTemplate(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertTemplate(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(Contract.TemplateEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertReminder(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(Contract.TaskEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertCategory(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(Contract.CategoryEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                rowsDeleted = database.delete(Contract.TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REMINDER_ID:
                selection = Contract.TaskEntry._ID1 + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(Contract.TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORY:
                rowsDeleted = database.delete(Contract.CategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORY_ID:
                selection = Contract.CategoryEntry._ID2 + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(Contract.CategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TEMPLATE:
                rowsDeleted = database.delete(Contract.TemplateEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TEMPLATE_ID:
                selection = Contract.TemplateEntry._ID3 + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(Contract.TemplateEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return updateReminder(uri, contentValues, selection, selectionArgs);
            case REMINDER_ID:
                selection = Contract.TaskEntry._ID1 + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateReminder(uri, contentValues, selection, selectionArgs);
            case CATEGORY:
                return updateCategory(uri, contentValues, selection, selectionArgs);
            case CATEGORY_ID:
                selection = Contract.CategoryEntry._ID2 + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateCategory(uri, contentValues, selection, selectionArgs);
            case TEMPLATE:
                return updateTemplate(uri, contentValues, selection, selectionArgs);
            case TEMPLATE_ID:
                selection = Contract.TemplateEntry._ID3 + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateTemplate(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateCategory(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(Contract.CategoryEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private int updateTemplate(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(Contract.TemplateEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private int updateReminder(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(Contract.TaskEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
