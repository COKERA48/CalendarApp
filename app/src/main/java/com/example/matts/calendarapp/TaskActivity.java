package com.example.matts.calendarapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextTaskName, editTextNotes;
    private TextView textViewDateInput, textViewStartTimeInput, textViewEndTimeInput;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener2;
    Calendar c, c2;
    DatabaseHelper dbHelper;

    private int year, month, day, hourStart, minuteStart, hourEnd, minuteEnd;
    private Button buttonSaveTask;
    String date, timeStart, timeEnd, formattedDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setTitle("New Task");

        editTextTaskName = findViewById(R.id.editTextTaskName);
        textViewDateInput = findViewById(R.id.textViewDateInput);
        textViewStartTimeInput = findViewById(R.id.textViewStartTimeInput);
        textViewEndTimeInput = findViewById(R.id.textViewEndTimeInput);
        editTextNotes = findViewById(R.id.editTextNotes);
        buttonSaveTask = findViewById(R.id.buttonSaveTask);
        dbHelper = new DatabaseHelper(this);

        c = Calendar.getInstance();
        updateDate();
        hourStart = c.get(Calendar.HOUR_OF_DAY);
        minuteStart = c.get(Calendar.MINUTE);
        updateTimeStart(hourStart, minuteStart);

        c2 = Calendar.getInstance();
        hourEnd = hourStart + 1;
        minuteEnd = minuteStart;
        updateTimeEnd(hourEnd, minuteEnd);

        hourEnd = hourStart + 1;
        minuteEnd = minuteStart;
        updateTimeEnd(hourEnd, minuteEnd);



        textViewDateInput.setOnClickListener(this);
        textViewStartTimeInput.setOnClickListener(this);
        textViewEndTimeInput.setOnClickListener(this);
        buttonSaveTask.setOnClickListener(this);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                c.set(Calendar.YEAR, y);
                c.set(Calendar.MONTH, m);
                c.set(Calendar.DAY_OF_MONTH, d);
                updateDate();
            }
        };
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                hourStart   = h;
                minuteStart = m;
                updateTimeStart(hourStart,minuteStart);
            }
        };
        mTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                hourEnd = h;
                minuteEnd = m;
                updateTimeEnd(hourEnd, minuteEnd);
            }
        };
    }

    public void updateDate()
    {
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
        formattedDate = simpleDateFormat.format(c.getTime());
        textViewDateInput.setText(formattedDate);
    }

    public void updateTimeStart(int hours, int minute)
    {
        String am_pm;
        if (hours > 12) {
            hours -= 12;
            am_pm = "PM";
        } else if (hours == 0) {
            hours += 12;
            am_pm = "AM";
        } else if (hours == 12)
            am_pm = "PM";
        else
            am_pm = "AM";
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, minute);
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat( "hh:mm", Locale.US);
        timeStart = simpleDateFormat.format(c.getTime()) + " " + am_pm; //format your time

        textViewStartTimeInput.setText(timeStart);
        checkTimes();
    }

    public void updateTimeEnd (int hours, int minute)
    {
        String am_pm = "";
        if (hours > 12) {
            hours -= 12;
            am_pm = "PM";
        } else if (hours == 0) {
            hours += 12;
            am_pm = "AM";
        } else if (hours == 12)
            am_pm = "PM";
        else
            am_pm = "AM";
        c2.set(Calendar.HOUR_OF_DAY, hours);
        c2.set(Calendar.MINUTE, minute);
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat( "hh:mm", Locale.US);
        timeEnd = simpleDateFormat.format(c2.getTime()) + " " + am_pm; //format your time

        textViewEndTimeInput.setText(timeEnd);

        checkTimes();


    }

    public void checkTimes() {
        if((hourStart > hourEnd ) || (hourStart == hourEnd && minuteStart > minuteEnd))
            buttonSaveTask.setEnabled(false);
        else buttonSaveTask.setEnabled(true);
    }

    public void saveTask() {
        String name = editTextTaskName.getText().toString();
        String notes = editTextNotes.getText().toString();
        boolean insertData = dbHelper.addData(name, formattedDate, timeStart, timeEnd, notes);
        if(insertData) {
            Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TaskActivity.this, ListTasksActivity.class);
            startActivity(intent);
        }

        else Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.textViewDateInput:
                DatePickerDialog dpd = new DatePickerDialog(TaskActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpd.show();
                break;
            case R.id.textViewStartTimeInput:
                TimePickerDialog tpd = new TimePickerDialog(TaskActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener, hourStart, minuteStart, false);
                tpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tpd.show();
                break;
            case R.id.textViewEndTimeInput:
                TimePickerDialog tpd2 = new TimePickerDialog(TaskActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener2, hourEnd, minuteEnd, false);
                tpd2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tpd2.show();
                break;
            case R.id.buttonSaveTask:
                saveTask();

                break;

        }


    }
}
