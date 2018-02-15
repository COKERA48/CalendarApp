package com.example.matts.calendarapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;
    DatePicker datePicker;
    int day, month, year;
    String formattedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
    }

    @Override
    public void onClick(View view) {

        if (view == button)
            day = datePicker.getDayOfMonth();
            month = datePicker.getMonth();
            year = datePicker.getYear();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            formattedDate = sdf.format(calendar.getTime());
            finish();
            Intent intent = new Intent(MainActivity.this, TaskActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("date", formattedDate);
            intent.putExtras(bundle);
            startActivity(intent);
    }
}
