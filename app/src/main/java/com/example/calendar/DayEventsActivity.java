package com.example.calendar;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DayEventsActivity extends AppCompatActivity {

    private ListView eventsListView;
    private long selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_events);

        eventsListView = findViewById(R.id.eventsListView);
        selectedDate = getIntent().getLongExtra("selectedDate", -1);

        loadEventsForSelectedDate();
    }

    private void loadEventsForSelectedDate() {
        Uri uri = CalendarContract.Events.CONTENT_URI;
        String[] projection = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.EVENT_COLOR
        };

        String selection = CalendarContract.Events.DTSTART + ">= ? AND " + CalendarContract.Events.DTEND + "<= ?";
        String[] selectionArgs = new String[]{
                String.valueOf(selectedDate),
                String.valueOf(selectedDate + 86400000)
        };

        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (cursor != null) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursor,
                    new String[]{CalendarContract.Events.TITLE, CalendarContract.Events.DESCRIPTION},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );
            eventsListView.setAdapter(adapter);
        }
    }
}