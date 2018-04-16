package com.example.matts.calendarapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
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
    MenuItem addTemplate, editTemplate, doneEditing;
    boolean editMode = false;
    Cursor cursorTemplates;
    TemplateListAdapter customAdapter;
    String templateName;
    int createdByUser;
    boolean newTemplate = false;
    String repeats;
    Uri currentVehicleUri;
    EditText editTextName;
    Spinner spinner;
    ContentResolver mResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        setTitle("Choose Template");

        Toolbar toolbar = findViewById(R.id.toolbarTemplate);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();


        mResolver = this.getContentResolver();
        Cursor cursor = mResolver.query(mCurrentReminderUri, new String[] {
                Contract.CategoryEntry._ID2, Contract.CategoryEntry.KEY_NAME, Contract.CategoryEntry.KEY_ICON, Contract.CategoryEntry.KEY_CREATED_BY_USER }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            catId = cursor.getInt(cursor
                    .getColumnIndex(Contract.CategoryEntry._ID2));
            catName = cursor.getString(cursor.getColumnIndex(Contract.CategoryEntry.KEY_NAME));
            catIcon = cursor.getInt(cursor.getColumnIndex(Contract.CategoryEntry.KEY_ICON));
            cursor.close();
        }

        String selection = "(" + Contract.TemplateEntry.KEY_TEMP_CAT + " = '" + catId + "')";
        cursorTemplates = mResolver.query(Contract.TemplateEntry.CONTENT_URI, new String [] { Contract.TemplateEntry._ID3,
                Contract.TemplateEntry.KEY_NAME, Contract.TemplateEntry.KEY_CREATED_BY_USER, Contract.TemplateEntry.KEY_TEMP_CAT }, selection, null, null);
        customAdapter = new TemplateListAdapter(getApplicationContext(), cursorTemplates, editMode);


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
        listViewTemplates.setAdapter(customAdapter);

        listViewTemplates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                currentVehicleUri = ContentUris.withAppendedId(Contract.TemplateEntry.CONTENT_URI, id);
                if (!editMode) {
                    Intent intent = new Intent(TemplateActivity.this, TaskActivity.class);

                    // Set the URI on the data field of the intent
                    intent.setData(currentVehicleUri);
                    intent.putExtra("ClassName", "TemplateActivity");
                    startActivity(intent);
                }
                else {

                    Cursor cursor = getContentResolver().query(currentVehicleUri, new String[] {
                            Contract.TemplateEntry._ID3, Contract.TemplateEntry.KEY_NAME, Contract.TemplateEntry.KEY_REPEATS, Contract.TemplateEntry.KEY_TEMP_CAT, Contract.TemplateEntry.KEY_CREATED_BY_USER }, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        templateName = cursor.getString(cursor
                                .getColumnIndex(Contract.TemplateEntry.KEY_NAME));
                        createdByUser = cursor.getInt(cursor.getColumnIndex(Contract.TemplateEntry.KEY_CREATED_BY_USER));
                        repeats = cursor.getString(cursor.getColumnIndex(Contract.TemplateEntry.KEY_REPEATS));


                    }
                    newTemplate = false;
                     showInputDialog();

                }



            }
        });

        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);

    }


    public boolean onCreateOptionsMenu (Menu menu)

    {
        getMenuInflater().inflate(R.menu.toolbar_template, menu);
        addTemplate = menu.findItem(R.id.action_add);
        editTemplate = menu.findItem(R.id.action_edit);
        doneEditing = menu.findItem(R.id.action_done);
        addTemplate.setVisible(false);
        doneEditing.setVisible(false);

        return true;

    }



    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {

            case R.id.action_edit:
                addTemplate.setVisible(true);
                editMode = true;
                editTemplate.setVisible(false);
                doneEditing.setVisible(true);

                break;
            case R.id.action_add:
                newTemplate = true;
                showInputDialog();
                break;
            case R.id.action_done:
                editMode = false;
                addTemplate.setVisible(false);
                editTemplate.setVisible(true);
                doneEditing.setVisible(false);
        }
        customAdapter = new TemplateListAdapter(getApplicationContext(), cursorTemplates, editMode);
        listViewTemplates.setAdapter(customAdapter);

        return super.onOptionsItemSelected(item);



    }

    private void showInputDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TemplateActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(TemplateActivity.this);
        View promptView = layoutInflater.inflate(R.layout.template_input_dialog, null);
        alertDialogBuilder.setView(promptView);


        editTextName = (EditText) promptView.findViewById(R.id.editTextTempName);
        TextView textViewName = promptView.findViewById(R.id.textViewName);
        spinner = promptView.findViewById(R.id.spinner2);
        TextView textViewTitle = promptView.findViewById(R.id.textViewTitle);

        if (!newTemplate) {
            if (createdByUser == 0) {
                editTextName.setVisibility(View.GONE);
                textViewName.setVisibility(View.GONE);
                textViewTitle.setText(templateName);

            }
            else {
                editTextName.setText(templateName);
                textViewTitle.setVisibility(View.GONE);
            }

            String[] repeatStrings = getResources().getStringArray(R.array.repeat_options);
            for (int i = 0; i < repeatStrings.length; i++)
            {
                if (repeatStrings[i].equals(repeats)) {
                    spinner.setSelection(i);
                }
            }
        }

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ContentValues values = new ContentValues();

                        values.put(Contract.TemplateEntry.KEY_REPEATS, String.valueOf(spinner.getSelectedItem()));

                        // If this is a new template, get name input,  set created by user to 1 (true), and set category foreign key
                        if(newTemplate) {
                            values.put(Contract.TemplateEntry.KEY_NAME, editTextName.getText().toString());
                            values.put(Contract.TemplateEntry.KEY_CREATED_BY_USER, 1);
                            values.put(Contract.TemplateEntry.KEY_TEMP_CAT, catId);
                            // insert new template to db
                            mResolver.insert(Contract.TemplateEntry.CONTENT_URI, values);
                        }
                        // If existing template
                        else  {
                            // If created by user, get updated name
                            if (createdByUser == 1) {
                                values.put(Contract.TemplateEntry.KEY_NAME, editTextName.getText().toString());
                            }
                            // update changes to template
                            mResolver.update(currentVehicleUri, values, null, null);
                        }

                    }
                })


                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        if(createdByUser == 1) {
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
                Contract.TemplateEntry._ID3,
                Contract.TemplateEntry.KEY_NAME,
                Contract.TemplateEntry.KEY_REPEATS,
                Contract.TemplateEntry.KEY_CREATED_BY_USER,
                Contract.TemplateEntry.KEY_TEMP_CAT,
                Contract.TemplateEntry.KEY_USAGE


        };

        String selection = "(" + Contract.TemplateEntry.KEY_TEMP_CAT + " = '" + catId + "')";

        return new CursorLoader(this,
                Contract.TemplateEntry.CONTENT_URI,
                projection,
                selection,                  // select templates that belong to category chosen
                null,
                Contract.TemplateEntry.KEY_USAGE + " DESC");    // sort templates by usage
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        customAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        customAdapter.swapCursor(null);
    }
}
