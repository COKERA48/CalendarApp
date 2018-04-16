package com.example.matts.calendarapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matts.calendarapp.data.Contract;

/**
 * Created by Matts on 4/15/2018.
 */

public class TemplateListAdapter extends CursorAdapter {

    boolean editMode;

    public TemplateListAdapter(Context context, Cursor c, boolean editMode) {
        super(context, c);
        this.editMode = editMode;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.single_row_template, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtTitle = (TextView) view.findViewById(R.id.textViewTemplateName);
        ImageView editIcon = (ImageView) view.findViewById(R.id.editIcon);

        String name = cursor.getString(cursor.getColumnIndex(Contract.TemplateEntry.KEY_NAME));

        txtTitle.setText(name);

        if (editMode) {
            editIcon.setVisibility(View.VISIBLE);
        }
            else editIcon.setVisibility(View.GONE);

        }

}
