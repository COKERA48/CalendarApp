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

public class TemplateActivity extends AppCompatActivity {

    private static final String TAG = "TemplateActivity";
    DatabaseHelper dbHelper;
    private ListView listViewTemplates;
    private int catId;
    private String catName;
    private Integer catIcon;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null ) {
            catId = bundle.getInt("categoryID");
            catName = bundle.getString("categoryName");
            catIcon = bundle.getInt("categoryIcon");
        }
        setTitle("Choose Template");

        image = (ImageView) findViewById(R.id.image);
        image.setImageResource(catIcon);
        TextView textViewCatName = findViewById(R.id.textViewCatName);
        textViewCatName.setText(catName);
        listViewTemplates = (ListView) findViewById(R.id.listViewTemplates);
        dbHelper = new DatabaseHelper(this);

        populateListView();

        listViewTemplates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tempName = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "TemplateActivity: onItemClick: You've clicked on: " + tempName);
                Cursor tempData = dbHelper.getTemplateRepeatValue(tempName);

                String repeats = null;
                while(tempData.moveToNext()) {
                    repeats = tempData.getString(0);
                }
                if (repeats != null) {
                    Log.d(TAG, "TemplateActivity: onItemClick: the repeat value is: " + repeats);
                    Intent intent = new Intent(TemplateActivity.this, TaskActivity.class);
                    intent.putExtra("templateRepeats", repeats);
                    intent.putExtra("templateName", tempName);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(TemplateActivity.this, "No repeat value associated with that template.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying Templates to ListView");

        Cursor data = dbHelper.getTemplates(catId);


        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()) {
            listData.add(data.getString(1));
        }


        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listViewTemplates.setAdapter(adapter);
    }
}
