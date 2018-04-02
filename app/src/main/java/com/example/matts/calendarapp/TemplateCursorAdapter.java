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
 * Created by Matts on 4/1/2018.
 */

public class TemplateCursorAdapter extends CursorAdapter {

    TextView textViewTemplateName;

    public TemplateCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.single_row_template, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        textViewTemplateName = view.findViewById(R.id.textViewTemplateName);

        int nameColumnIndex = cursor.getColumnIndex(Contract.TemplateEntry.KEY_NAME);

        String name = cursor.getString(nameColumnIndex);

        textViewTemplateName.setText(name);
    }
}
