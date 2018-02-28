package com.example.matts.calendarapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private static final String TAG = "CategoryActivity";
    DatabaseHelper dbHelper;
    private ListView listViewCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle("Choose Category");

        listViewCategories = (ListView) findViewById(R.id.listViewCategories);
        dbHelper = new DatabaseHelper(this);

        populateListView();

        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String catName = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "CategoryActivity: onItemClick: You've clicked on: " + catName);
                Cursor catData = dbHelper.getCategoryID(catName);
                int itemID = -1;
                while(catData.moveToNext()) {
                    itemID = catData.getInt(0);
                }
                if (itemID > -1) {
                    Log.d(TAG, "CategoryActivity: onItemClick: the id is: " + itemID);
                    Intent intent = new Intent(CategoryActivity.this, TemplateActivity.class);
                    intent.putExtra("categoryID", itemID);
                    intent.putExtra("categoryName", catName);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(CategoryActivity.this, "No id associated with that category.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying Categories to ListView");

        Cursor data = dbHelper.getCategories();

        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()) {
            listData.add(data.getString(1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.single_row, R.id.textView, listData);
        listViewCategories.setAdapter(adapter);
    }
}
