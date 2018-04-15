package com.example.matts.calendarapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matts.calendarapp.data.Contract;

import java.util.ArrayList;

/**
 * Created by Matts on 4/12/2018.
 */

public class CategoryListAdapter extends CursorAdapter {
boolean editMode;

    public CategoryListAdapter(Context context, Cursor c, boolean editMode) {
        super(context, c);
        this.editMode = editMode;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.single_row_category, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView txtTitle = (TextView) view.findViewById(R.id.textView);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        ImageView editIcon = (ImageView) view.findViewById(R.id.editIcon);

        String name = cursor.getString(cursor.getColumnIndex(Contract.CategoryEntry.KEY_NAME));
        int icon = cursor.getInt(cursor.getColumnIndex(Contract.CategoryEntry.KEY_ICON));
        int createdByUser = cursor.getInt(cursor.getColumnIndex(Contract.CategoryEntry.KEY_CREATED_BY_USER));


        txtTitle.setText(name);
        imageView.setImageResource(icon);
        if (editMode) {
            if(createdByUser != 0){
                editIcon.setVisibility(View.VISIBLE);
            }
            else editIcon.setVisibility(View.GONE);

        }


    }
}
