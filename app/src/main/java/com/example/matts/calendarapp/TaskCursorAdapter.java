package com.example.matts.calendarapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.matts.calendarapp.data.Contract;

/**
 * Created by Matts on 3/29/2018.
 */

public class TaskCursorAdapter extends CursorAdapter {

    private TextView textViewTaskName, textViewTaskDate, textViewTaskTime;

    public TaskCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.single_row_task, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        textViewTaskName = view.findViewById(R.id.textViewTaskName);
        textViewTaskDate = view.findViewById(R.id.textViewTaskDate);
        textViewTaskTime = view.findViewById(R.id.textViewTaskTime);

        int nameColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_NAME);
        int startDateColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_START_DATE);
        int startTimeColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_START_TIME);
        int endDateColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_END_DATE);
        int endTimeColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_END_TIME);
        int repeatsColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_REPEATS);
        int notesColumnIndex = cursor.getColumnIndex(Contract.TaskEntry.KEY_NOTES);

        String name = cursor.getString(nameColumnIndex);
        String startDate = cursor.getString(startDateColumnIndex);
        String startTime = cursor.getString(startTimeColumnIndex);
        String endDate = cursor.getString(endDateColumnIndex);
        String endTime = cursor.getString(endTimeColumnIndex);
        String repeats = cursor.getString(repeatsColumnIndex);
        String notes = cursor.getString(notesColumnIndex);


        textViewTaskName.setText(name);
        textViewTaskDate.setText(startDate);
        textViewTaskTime.setText(startTime);
    }
}
