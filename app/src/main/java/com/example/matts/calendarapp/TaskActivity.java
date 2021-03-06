package com.example.matts.calendarapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.matts.calendarapp.data.Contract;
import com.example.matts.calendarapp.data.DatabaseHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private EditText editTextTaskName, editTextNotes;
    private TextView tvStartDate, tvStartTime, tvEndDate, tvEndTime;
    private DatePickerDialog.OnDateSetListener StartDateSetListener, EndDateSetListener;
    private TimePickerDialog.OnTimeSetListener StartTimeSetListener, EndTimeSetListener;
    Calendar calStart, calEnd;
    private Button buttonSaveTask;
    private static final String TAG = "TaskActivity";
    Spinner spinner;
    DateFormat dateFormat, timeFormat;
    String sourceClass = "";
    int alarmId = 0;
    Uri newUri;
    long timestamp;
    String templateName, templateRepeats;
    int idTempCat, usage;



    private Uri mCurrentReminderUri;

    private static final int EXISTING_VEHICLE_LOADER = 0;

    // Values for orientation change
    private static final String KEY_NAME = "title_key";
    private static final String KEY_START_TIME = "start_time_key";
    private static final String KEY_START_DATE = "start_date_key";
    private static final String KEY_END_TIME = "end_time_key";
    private static final String KEY_END_DATE = "end_date_key";
    private static final String KEY_REPEATS = "repeats_key";
    private static final String KEY_NOTES = "notes_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);


        Toolbar toolbar = findViewById(R.id.toolbarTask);
        setSupportActionBar(toolbar);

        editTextTaskName = findViewById(R.id.editTextTaskName);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvEndTime = findViewById(R.id.tvEndTime);
        editTextNotes = findViewById(R.id.editTextNotes);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);
        Button buttonDeleteTask = findViewById(R.id.buttonDeleteTask);
        spinner = findViewById(R.id.spinner);
        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sourceClass = bundle.getString("ClassName");
        }

        if (sourceClass.equals("TemplateActivity")) {
            Log.d(TAG, "test");
            setTitle("New Task");

            getLoaderManager().initLoader(1, null, this);

            buttonDeleteTask.setVisibility(View.GONE);
        } else {

            setTitle("Edit Task");
            buttonSaveTask.setText("Update Task");

            getLoaderManager().initLoader(EXISTING_VEHICLE_LOADER, null, this);
        }

        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        timeFormat = new SimpleDateFormat( "hh:mm a", Locale.US);

        // Create calendar for start date and set initial display of start date and time.
        calStart = Calendar.getInstance();
        calStart.set(Calendar.SECOND, 0);
        tvStartDate.setText(dateFormat.format(calStart.getTime()));
        tvStartTime.setText(timeFormat.format(calStart.getTime()));

        // Create calendar for end date. Task end time is set to one hour after start time.
        calEnd = Calendar.getInstance();
        calEnd.set(Calendar.SECOND, 0);
        calEnd.add(Calendar.HOUR_OF_DAY, 1);
        tvEndDate.setText(dateFormat.format(calEnd.getTime()));
        tvEndTime.setText(timeFormat.format(calEnd.getTime()));



        tvStartDate.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        buttonSaveTask.setOnClickListener(this);
        buttonDeleteTask.setOnClickListener(this);

        // To save state on device rotation
        if (savedInstanceState != null) {
            String savedName = savedInstanceState.getString(KEY_NAME);
            editTextTaskName.setText(savedName);

            String savedStartDate = savedInstanceState.getString(KEY_START_DATE);
            tvStartDate.setText(savedStartDate);

            String savedStartTime = savedInstanceState.getString(KEY_START_TIME);
            tvStartTime.setText(savedStartTime);

            String savedEndDate = savedInstanceState.getString(KEY_END_DATE);
            tvEndDate.setText(savedEndDate);

            String savedEndTime = savedInstanceState.getString(KEY_END_TIME);
            tvEndTime.setText(savedEndTime);

            String savedRepeats = savedInstanceState.getString(KEY_REPEATS);
            setRepeatsValue(savedRepeats);

            String savedNotes = savedInstanceState.getString(KEY_NOTES);
            editTextNotes.setText(savedNotes);

        }

        // Listener for start date DatePicker. Gets date picked and displays to start date textview.
        // Resets end date to the same as start date.
        StartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                calStart.set(y, m, d);
                tvStartDate.setText(dateFormat.format(calStart.getTime()));
                calEnd.set(y, m, d);
                tvEndDate.setText(dateFormat.format(calEnd.getTime()));
                //checkTimes();

            }
        };
        // Listener for start time TimePicker. Gets time picked and displays to start time textView.
        // Resets the end time to an hour after the start time.
        StartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                calStart.set(Calendar.HOUR_OF_DAY, h);
                calStart.set(Calendar.MINUTE, m);
                calEnd.set(Calendar.HOUR_OF_DAY, h+1);
                calEnd.set(Calendar.MINUTE, m);
                tvStartTime.setText(timeFormat.format(calStart.getTime()));
                tvEndTime.setText(timeFormat.format(calEnd.getTime()));
                //checkTimes();
            }
        };
        // Listener for end date DatePicker. Gets date picked and displays to end date textview.
        EndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                calEnd.set(y, m, d);
                tvEndDate.setText(dateFormat.format(calEnd.getTime()));
                //checkTimes();
            }
        };
        // Listener for end time TimePicker. Gets time picked and displays to end time textView.
        EndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                calEnd.set(Calendar.HOUR_OF_DAY, h);
                calEnd.set(Calendar.MINUTE, m);
                tvEndTime.setText(timeFormat.format(calEnd.getTime()));
                //checkTimes();
            }
        };

    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_NAME, editTextTaskName.getText());
        outState.putCharSequence(KEY_START_TIME, tvStartTime.getText());
        outState.putCharSequence(KEY_START_DATE, tvStartDate.getText());
        outState.putCharSequence(KEY_END_TIME, tvEndTime.getText());
        outState.putCharSequence(KEY_END_DATE, tvEndDate.getText());
        outState.putCharSequence(KEY_REPEATS, spinner.getSelectedItem().toString());
        outState.putCharSequence(KEY_NOTES, editTextNotes.getText());
    }

    // Gets string array of spinner options. Compares the options to the repeat value of
    // template. Sets spinner selected value to that option.
    private void setRepeatsValue(String repeats) {
        String[] repeatStrings = this.getResources().getStringArray(R.array.repeat_options);
        for (int i = 0; i < repeatStrings.length; i++)
        {
            if (repeatStrings[i].equals(repeats)) {
                spinner.setSelection(i);
            }
        }
    }

    public boolean checkTimes() {

        // If start date/time is after end date/time OR start date/time is before now,
        // user may not save task.
        if (calStart.compareTo(calEnd) > 0) {
            Toast.makeText(this, "Start time must be before end time.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        //Calendar now = Calendar.getInstance();
        /*
        if (calStart.compareTo(now) < 0 ) {
            Toast.makeText(this, "Start time must be set to a future time.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        */
        else return true;

    }


    public void saveTask() {
        if (checkTimes()) {


            String name = editTextTaskName.getText().toString();
            String startDate = dateFormat.format(calStart.getTime());
            String startTime = timeFormat.format(calStart.getTime());
            String endDate = dateFormat.format(calEnd.getTime());
            String endTime = timeFormat.format(calEnd.getTime());
            String repeats = String.valueOf(spinner.getSelectedItem());
            String notes = editTextNotes.getText().toString();

            String startDateTime = startDate + " " + startTime;
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
            Calendar c = Calendar.getInstance();
            try {
                Date start = df.parse(startDateTime);
                c.setTimeInMillis(start.getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            long timestamp = c.getTimeInMillis();
            Log.d(TAG, "sort time: " + timestamp);

            if (alarmId == 0) {
                alarmId = (int) System.currentTimeMillis();
            }

            ContentValues values = new ContentValues();

            values.put(Contract.TaskEntry.KEY_NAME, name);
            values.put(Contract.TaskEntry.KEY_START_DATE, startDate);
            values.put(Contract.TaskEntry.KEY_START_TIME, startTime);
            values.put(Contract.TaskEntry.KEY_END_DATE, endDate);
            values.put(Contract.TaskEntry.KEY_END_TIME, endTime);
            values.put(Contract.TaskEntry.KEY_REPEATS, repeats);
            values.put(Contract.TaskEntry.KEY_NOTES, notes);
            values.put(Contract.TaskEntry.KEY_ALARM_ID, alarmId);
            values.put(Contract.TaskEntry.KEY_TIMESTAMP, timestamp);

            if (sourceClass.equals("TemplateActivity")) {
                // This is a NEW reminder, so insert a new reminder into the provider,
                // returning the content URI for the new reminder.
                newUri = getContentResolver().insert(Contract.TaskEntry.CONTENT_URI, values);

                usage = usage + 1;
                ContentValues templateValues = new ContentValues();
                templateValues.put(Contract.TemplateEntry.KEY_USAGE, usage);
                getContentResolver().update(mCurrentReminderUri, templateValues, null, null);
                Log.d(TAG, "template usage: " + usage);

                ContentResolver categoryResolver = this.getContentResolver();
                String selection = Contract.CategoryEntry._ID2 + " = " + idTempCat;
                Cursor cursor = categoryResolver.query(Contract.CategoryEntry.CONTENT_URI, new String[] {
                        Contract.CategoryEntry._ID2, Contract.CategoryEntry.KEY_USAGE }, selection, null, null);


                if (cursor != null && cursor.moveToFirst()) {
                    int catId = cursor.getInt(cursor.getColumnIndex(Contract.CategoryEntry._ID2));
                    int usageCat = cursor.getInt(cursor.getColumnIndex(Contract.CategoryEntry.KEY_USAGE));

                    usageCat++;

                    ContentValues categoryValues = new ContentValues();
                    categoryValues.put(Contract.CategoryEntry.KEY_USAGE, usageCat);

                    Uri categoryUri = ContentUris.withAppendedId(Contract.CategoryEntry.CONTENT_URI, catId);
                    getContentResolver().update(categoryUri, categoryValues, null, null);
                    Log.d(TAG, "category usage: " + usageCat);
                }



                // Show a toast message depending on whether or not the insertion was successful.
                if (newUri == null) {
                    // If the new content URI is null, then there was an error with insertion.
                    Toast.makeText(this, "Something went wrong.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the insertion was successful and we can display a toast.
                    Toast.makeText(this, "Task Saved!",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    setAlarm();


                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentReminderUri, values, null, null);

                // Show a toast message depending on whether or not the update was successful.
                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    Toast.makeText(this, "Something went wrong.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the update was successful and we can display a toast.
                    Toast.makeText(this, "Task Updated!",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    setAlarm();

                }
            }
        }


    }

    private void setAlarm() {
        long interval = 0;
        switch (spinner.getSelectedItem().toString()) {
            case "Every Day":
                interval = 60000;
                //interval = 86400000;
                break;
            case "Every Other Day":
                interval = 172800000;
                break;
            case "Every Week":
                interval = 604800000;
                break;
            case "Every 2 Weeks":
                interval = 1209600000;
                break;
            case "Every Month":
                interval = 2629746000L;
                break;
            case "Every Year":
                interval = 31556952000L;
                break;
        }

        long initialTime = calStart.getTimeInMillis();

        Intent intent = new Intent(getApplicationContext(), Alarm.class);

        if (newUri != null) {
            intent.setData(newUri);

        }
        else intent.setData(mCurrentReminderUri);

        intent.putExtra("initialTime", initialTime);
        intent.putExtra("interval", interval);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, initialTime, pendingIntent);

            }
            else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, initialTime, pendingIntent);
            }

            Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "setAlarm: time" + calStart.getTime().toString() + " " + interval);

        }
    }

    public void deleteAlarm() {
        Intent intent = new Intent(getApplicationContext(), Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.tvStartDate:
                DatePickerDialog dpdStart = new DatePickerDialog(TaskActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, StartDateSetListener, calStart.get(Calendar.YEAR), calStart.get(Calendar.MONTH), calStart.get(Calendar.DAY_OF_MONTH));
                if (dpdStart.getWindow() != null)
                    dpdStart.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpdStart.show();

                break;
            case R.id.tvStartTime:
                TimePickerDialog tpdStart = new TimePickerDialog(TaskActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, StartTimeSetListener, calStart.get(Calendar.HOUR_OF_DAY), calStart.get(Calendar.MINUTE), false);
                if(tpdStart.getWindow() != null)
                    tpdStart.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tpdStart.show();
                break;
            case R.id.tvEndDate:
                DatePickerDialog dpdEnd = new DatePickerDialog(TaskActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, EndDateSetListener, calEnd.get(Calendar.YEAR), calEnd.get(Calendar.MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
                if (dpdEnd.getWindow() != null)
                    dpdEnd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpdEnd.show();
                break;
            case R.id.tvEndTime:
                TimePickerDialog tpdEnd = new TimePickerDialog(TaskActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, EndTimeSetListener, calEnd.get(Calendar.HOUR_OF_DAY), calEnd.get(Calendar.MINUTE), false);
                if (tpdEnd.getWindow() != null)
                    tpdEnd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tpdEnd.show();
                break;
            case R.id.buttonSaveTask:
                saveTask();
                break;
            case R.id.buttonDeleteTask:
                deleteTask();
                break;

        }


    }

    private void deleteTask() {

        // Only perform the delete if this is an existing reminder.
        if (!sourceClass.equals("TemplateActivity")) {
            // Call the ContentResolver to delete the reminder at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentreminderUri
            // content URI already identifies the reminder that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentReminderUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "Error Deleting Task.",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, "Task Deleted!",
                        Toast.LENGTH_SHORT).show();
                deleteAlarm();
            }
        }

        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader loader = null;
        switch (i){
            case EXISTING_VEHICLE_LOADER:
                String[] projection = {
                        Contract.TaskEntry._ID1,
                        Contract.TaskEntry.KEY_NAME,
                        Contract.TaskEntry.KEY_START_DATE,
                        Contract.TaskEntry.KEY_START_TIME,
                        Contract.TaskEntry.KEY_END_DATE,
                        Contract.TaskEntry.KEY_END_TIME,
                        Contract.TaskEntry.KEY_REPEATS,
                        Contract.TaskEntry.KEY_NOTES,
                        Contract.TaskEntry.KEY_ALARM_ID,
                        Contract.TaskEntry.KEY_TIMESTAMP
                };

                // This loader will execute the ContentProvider's query method on a background thread
                loader = new CursorLoader(this,   // Parent activity context
                        mCurrentReminderUri,         // Query the content URI for the current reminder
                        projection,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        null);                  // Default sort order
                break;
            case 1:
                String[] projection2 = {
                        Contract.TemplateEntry._ID3,
                        Contract.TemplateEntry.KEY_NAME,
                        Contract.TemplateEntry.KEY_REPEATS,
                        Contract.TemplateEntry.KEY_TEMP_CAT,
                        Contract.TemplateEntry.KEY_CREATED_BY_USER,
                        Contract.TemplateEntry.KEY_USAGE
                };
                loader = new CursorLoader(this,   // Parent activity context
                        mCurrentReminderUri,         // Query the content URI for the current reminder
                        projection2,             // Columns to include in the resulting Cursor
                        null,                   // No selection clause
                        null,                   // No selection arguments
                        null);                  // Default sort order
                break;
            default:
                return null;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        switch (loader.getId()) {
            case 0:
                // Proceed with moving to the first row of the cursor and reading data from it
                // (This should be the only row in the cursor)
                if (cursor.moveToFirst()) {
                    int nameColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_NAME);
                    int startDateColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_START_DATE);
                    int startTimeColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_START_TIME);
                    int endDateColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_END_DATE);
                    int endTimeColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_END_TIME);
                    int repeatsColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_REPEATS);
                    int notesColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_NOTES);
                    int alarmIdColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_ALARM_ID);
                    int timestampColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_TIMESTAMP);

                    // Extract out the value from the Cursor for the given column index
                    String name = cursor.getString(nameColumnIndex);
                    String startDate = cursor.getString(startDateColumnIndex);
                    String startTime = cursor.getString(startTimeColumnIndex);
                    String endDate = cursor.getString(endDateColumnIndex);
                    String endTime = cursor.getString(endTimeColumnIndex);
                    String repeats = cursor.getString(repeatsColumnIndex);
                    String notes = cursor.getString(notesColumnIndex);
                    alarmId = cursor.getInt(alarmIdColumnIndex);
                    timestamp = cursor.getLong(timestampColumnIndex);


                    // Update the views on the screen with the values from the database
                    editTextTaskName.setText(name);
                    tvStartDate.setText(startDate);
                    tvStartTime.setText(startTime);
                    tvEndDate.setText(endDate);
                    tvEndTime.setText(endTime);
                    setRepeatsValue(repeats);
                    editTextNotes.setText(notes);

                    // Set calendar objects with date and time values from database
                    try {
                        String endDateTime = endDate + " " + endTime;
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
                        calStart.setTimeInMillis(timestamp);
                        Date end = df.parse(endDateTime);
                        calEnd.setTimeInMillis(end.getTime());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                if (cursor.moveToFirst()){
                    int nameColumnIndex = cursor.getColumnIndex(Contract.TemplateEntry.KEY_NAME);
                    int repeatsColumnIndex = cursor.getColumnIndex(Contract.TemplateEntry.KEY_REPEATS);
                    int idTempCatIndex = cursor.getColumnIndex(Contract.TemplateEntry.KEY_TEMP_CAT);
                    int usageColumnIndex = cursor.getColumnIndex(Contract.TemplateEntry.KEY_USAGE);

                    templateName = cursor.getString(nameColumnIndex);
                    templateRepeats = cursor.getString(repeatsColumnIndex);
                    idTempCat = cursor.getInt(idTempCatIndex);
                    usage = cursor.getInt(usageColumnIndex);

                    editTextTaskName.setText(templateName);
                    setRepeatsValue(templateRepeats);

                }
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
