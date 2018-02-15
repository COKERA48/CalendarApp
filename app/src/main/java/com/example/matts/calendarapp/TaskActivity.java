package com.example.matts.calendarapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaskActivity extends AppCompatActivity {
    EditText editTextTaskName, editTextDate, editTextStartTime, editTextEndTime, editTextDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Bundle bundle = getIntent().getExtras();
        String dateSelected = bundle.getString("date");

        editTextTaskName = (EditText) findViewById(R.id.editTextTaskName);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextStartTime = (EditText) findViewById(R.id.editTextStartTime);
        editTextEndTime = (EditText) findViewById(R.id.editTextEndTime);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        editTextDate.setText(dateSelected);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String formattedDate = df.format(c.getTime());

        editTextStartTime.setText(formattedDate);
    }
}
