package com.example.matts.calendarapp;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.matts.calendarapp.data.Contract;
import com.example.matts.calendarapp.data.DatabaseHelper;

public class TemplateActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "TemplateActivity";
    DatabaseHelper dbHelper;
    private ListView listViewTemplates;
    private int catId;
    private String catName;
    private Integer catIcon;
    ImageView image;
    private Uri mCurrentReminderUri;
    private static final int VEHICLE_LOADER = 0;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        setTitle("Choose Template");

        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();

        ContentResolver mResolver = this.getContentResolver();
        Cursor cursor = mResolver.query(mCurrentReminderUri, new String[] {
                Contract.CategoryEntry._ID2, Contract.CategoryEntry.KEY_NAME, Contract.CategoryEntry.KEY_ICON }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            catId = cursor.getInt(cursor
                    .getColumnIndex(Contract.CategoryEntry._ID2));
            catName = cursor.getString(cursor.getColumnIndex(Contract.CategoryEntry.KEY_NAME));
            catIcon = cursor.getInt(cursor.getColumnIndex(Contract.CategoryEntry.KEY_ICON));
            cursor.close();
        }


        image = (ImageView) findViewById(R.id.image);
        image.setImageResource(catIcon);
        TextView textViewCatName = findViewById(R.id.textViewCatName);
        textViewCatName.setText(catName);

        adapter = new SimpleCursorAdapter(this,
                R.layout.single_row_template,
                null,
                new String[] { Contract.TemplateEntry.KEY_NAME },
                new int[] { R.id.textViewTemplateName }, 0);

        listViewTemplates = (ListView) findViewById(R.id.listViewTemplates);
        listViewTemplates.setAdapter(adapter);

        listViewTemplates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(TemplateActivity.this, TaskActivity.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(Contract.TemplateEntry.CONTENT_URI, id);


                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);
                intent.putExtra("ClassName", "TemplateActivity");
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                Contract.TemplateEntry._ID3,
                Contract.TemplateEntry.KEY_NAME,
                Contract.TemplateEntry.KEY_REPEATS,
                Contract.TemplateEntry.KEY_TEMP_CAT


        };

        String selection = "(" + Contract.TemplateEntry.KEY_TEMP_CAT + " = '" + catId + "')";

        return new CursorLoader(this,   // Parent activity context
                Contract.TemplateEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                selection,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
