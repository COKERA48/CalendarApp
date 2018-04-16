package com.example.matts.calendarapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.matts.calendarapp.data.Contract;


public class ListTasksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ListTasksActivity";
    SimpleCursorAdapter adapter;
    private static final int VEHICLE_LOADER = 0;
    private ActionBarDrawerToggle menuToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tasks);

        //setup side menu and toggle button
        NavigationView menu = findViewById(R.id.navigationView);
        Toolbar toolbar = findViewById(R.id.toolbarListTasks);
        menu.setNavigationItemSelectedListener(this); //have app call onNavigationItemSelected() when menu option is used
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        menuToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(menuToggle);
        menuToggle.syncState();
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = findViewById(R.id.listView);
        TextView textViewNoTasks = findViewById(R.id.textViewNoTasks);
        listView.setEmptyView(textViewNoTasks);

        adapter = new SimpleCursorAdapter(this,
                R.layout.single_row_task,
                null,
                new String[] { Contract.TaskEntry.KEY_NAME, Contract.TaskEntry.KEY_START_DATE, Contract.TaskEntry.KEY_START_TIME },
                new int[] { R.id.textViewTaskName, R.id.textViewTaskDate, R.id.textViewTaskTime }, 0);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(ListTasksActivity.this, TaskActivity.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(Contract.TaskEntry.CONTENT_URI, id);

                intent.setData(currentVehicleUri);

                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                Contract.TaskEntry._ID,
                Contract.TaskEntry.KEY_NAME,
                Contract.TaskEntry.KEY_START_DATE,
                Contract.TaskEntry.KEY_START_TIME,
                Contract.TaskEntry.KEY_END_DATE,
                Contract.TaskEntry.KEY_END_TIME,
                Contract.TaskEntry.KEY_REPEATS,
                Contract.TaskEntry.KEY_NOTES,
                Contract.TaskEntry.KEY_ALARM_ID,
                Contract.TaskEntry.KEY_TIMESTAMP

        };

        return new CursorLoader(this,   // Parent activity context
                Contract.TaskEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                Contract.TaskEntry.KEY_TIMESTAMP);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    //from menu interface
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    //from menu interface
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.newTask:
                startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
                return true;
            case R.id.calendar:
                startActivity(new Intent(getApplicationContext(), CalendarViewActivity.class));
                return true;
        }

        return false;
    }

}
