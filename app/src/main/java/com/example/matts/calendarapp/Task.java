package com.example.matts.calendarapp;

import android.util.Log;

/**
 * Created by Matts on 2/18/2018.
 */

public class Task {

    public static final String TAG = "Task";
    private String _name;
    private String _date;
    private String _startTime;
    private String _endTime;
    private String _notes;

    public Task(String name, String date, String startTime, String endTime, String notes)
    {
        _name = name;
        _date = date;
        _startTime = startTime;
        _endTime = endTime;
        _notes = notes;
    }


    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_startTime() {
        return _startTime;
    }

    public void set_startTimeI(String _startTime) {
        this._startTime = _startTime;
    }

    public String get_endTime() {
        return _endTime;
    }

    public void set_endTime(String _endTime) {
        this._endTime = _endTime;
    }

    public String get_notes() {
        return _notes;
    }

    public void set_notes(String _notes) {
        this._notes = _notes;
    }

    public void displayTask()
    {
        Log.d(TAG, "Name: " + _name + "\nDate: " + _date + "\nStarts: " + _startTime + "\nEnds: " + _endTime + "\nNotes: " + _notes);
    }
}
