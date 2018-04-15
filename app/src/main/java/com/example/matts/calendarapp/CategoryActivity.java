package com.example.matts.calendarapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.matts.calendarapp.data.Contract;
import com.example.matts.calendarapp.data.DatabaseHelper;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private static final String TAG = "CategoryActivity";
    private ListView listViewCategories;
    SimpleCursorAdapter adapter;
    CategoryListAdapter customAdapter;
    AlertDialog.Builder alertDialogBuilder;
    View promptView;
    boolean editMode = false;
    Button editButton, addButton;
    String catName;
    int createdByUser;
    boolean newCategory = false;
    EditText editTextName;
    Uri currentVehicleUri;
    Cursor cursor;
    ContentResolver mResolver;



    private static final int VEHICLE_LOADER = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_category);
            setTitle("Choose Category");

            listViewCategories = (ListView) findViewById(R.id.listViewCategories);
            editButton = findViewById(R.id.buttonEdit);
            editButton.setOnClickListener(this);
            addButton = findViewById(R.id.buttonAdd);
            addButton.setOnClickListener(this);
            addButton.setVisibility(View.GONE);


            mResolver = this.getContentResolver();
            cursor = mResolver.query(Contract.CategoryEntry.CONTENT_URI, new String[] {
                    Contract.CategoryEntry._ID2, Contract.CategoryEntry.KEY_NAME, Contract.CategoryEntry.KEY_ICON, Contract.CategoryEntry.KEY_CREATED_BY_USER }, null, null, null);
           customAdapter = new CategoryListAdapter(getApplicationContext(), cursor, editMode);


           adapter = new SimpleCursorAdapter(this,
                    R.layout.single_row_category,
                    null,
                    new String[] { Contract.CategoryEntry.KEY_ICON,Contract.CategoryEntry.KEY_NAME },
                    new int[] { R.id.icon, R.id.textView }, 0);




            listViewCategories.setAdapter(customAdapter);
            listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    currentVehicleUri = ContentUris.withAppendedId(Contract.CategoryEntry.CONTENT_URI, id);
                    if(!editMode) {
                        Intent intent = new Intent(CategoryActivity.this, TemplateActivity.class);

                        // Set the URI on the data field of the intent
                        intent.setData(currentVehicleUri);

                        startActivity(intent);
                    }
                    else {

                        Cursor cursor = getContentResolver().query(currentVehicleUri, new String[] {
                                Contract.CategoryEntry._ID2, Contract.CategoryEntry.KEY_NAME, Contract.CategoryEntry.KEY_ICON, Contract.CategoryEntry.KEY_CREATED_BY_USER }, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            catName = cursor.getString(cursor
                                    .getColumnIndex(Contract.CategoryEntry.KEY_NAME));
                            createdByUser = cursor.getInt(cursor.getColumnIndex(Contract.CategoryEntry.KEY_CREATED_BY_USER));
                            newCategory = false;

                        }
                        if (createdByUser == 1) {
                            showInputDialog();
                        }
                    }

                }
            });

            getLoaderManager().initLoader(VEHICLE_LOADER, null, this);


        }

    private void showInputDialog() {
        alertDialogBuilder = new AlertDialog.Builder(CategoryActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(CategoryActivity.this);
        promptView = layoutInflater.inflate(R.layout.category_input_dialog, null);
        alertDialogBuilder.setView(promptView);

        editTextName = (EditText) promptView.findViewById(R.id.editTextCatName);
        if(!newCategory) {
            editTextName.setText(catName);
        }


        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ContentValues values = new ContentValues();
                        values.put(Contract.CategoryEntry.KEY_NAME, editTextName.getText().toString());
                        values.put(Contract.CategoryEntry.KEY_ICON, R.drawable.ic_person);
                        values.put(Contract.CategoryEntry.KEY_CREATED_BY_USER, 1);
                        if(newCategory) {
                            mResolver.insert(Contract.CategoryEntry.CONTENT_URI, values);
                        } else mResolver.update(currentVehicleUri, values, null, null);
                        editTextName.setText("");

                    }
                })


                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        if(!newCategory) {
            alertDialogBuilder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mResolver.delete(currentVehicleUri, null, null);
                }
            });
        }
        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                Contract.CategoryEntry._ID2,
                Contract.CategoryEntry.KEY_NAME,
                Contract.CategoryEntry.KEY_ICON,
                Contract.CategoryEntry.KEY_CREATED_BY_USER


        };

        return new CursorLoader(this,   // Parent activity context
                Contract.CategoryEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        customAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        customAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonEdit:
                if (!editMode) {
                    editMode = true;
                    addButton.setVisibility(View.VISIBLE);
                    editButton.setText("Done");
                }
                else if (editMode) {
                    editMode = false;
                    addButton.setVisibility(View.GONE);
                    editButton.setText("Edit");
                }
                break;
            case R.id.buttonAdd:
                newCategory = true;
                showInputDialog();
                break;

        }
        customAdapter = new CategoryListAdapter(getApplicationContext(), cursor, editMode);
        listViewCategories.setAdapter(customAdapter);
    }
}
