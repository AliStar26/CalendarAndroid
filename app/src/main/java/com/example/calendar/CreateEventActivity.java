package com.example.calendar;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private EditText startTimeEditText, endTimeEditText;
    private CheckBox allDayCheckBox;
    private Button colorPickerButton, saveButton;
    private long startTime, endTime;
    private int eventColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        allDayCheckBox = findViewById(R.id.allDayCheckBox);
        colorPickerButton = findViewById(R.id.eventColorPicker);
        saveButton = findViewById(R.id.saveButton);

        allDayCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            startTimeEditText.setEnabled(!isChecked);
            endTimeEditText.setEnabled(!isChecked);
        });

        startTimeEditText.setOnClickListener(v -> showTimePickerDialog(true));
        endTimeEditText.setOnClickListener(v -> showTimePickerDialog(false));

        colorPickerButton.setOnClickListener(v -> {
            eventColor = 0xFF00FF00; // Зеленый
            Toast.makeText(this, "Цвет выбран", Toast.LENGTH_SHORT).show();
        });

        saveButton.setOnClickListener(v -> saveEvent());
    }

    private void showTimePickerDialog(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            if (isStartTime) {
                startTime = calendar.getTimeInMillis();
                startTimeEditText.setText(hourOfDay + ":" + minute);
            } else {
                endTime = calendar.getTimeInMillis();
                endTimeEditText.setText(hourOfDay + ":" + minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void saveEvent() {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, titleEditText.getText().toString());
        values.put(CalendarContract.Events.DESCRIPTION, descriptionEditText.getText().toString());
        if (allDayCheckBox.isChecked()) {
            values.put(CalendarContract.Events.ALL_DAY, 1);
        } else {
            values.put(CalendarContract.Events.DTSTART, startTime);
            values.put(CalendarContract.Events.DTEND, endTime);
        }
        values.put(CalendarContract.Events.EVENT_COLOR, eventColor);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC");

        Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        if (uri != null) {
            Toast.makeText(this, "Событие добавлено", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка добавления события", Toast.LENGTH_SHORT).show();
        }
    }
}