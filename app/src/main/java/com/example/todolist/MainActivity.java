package com.example.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskChangedListener {

    private EditText inputTask;
    private Button btnAdd;
    private TextView txtTime;
    private RecyclerView rvTasks;

    private ArrayList<String> tasks = new ArrayList<>();
    private TaskAdapter adapter;

    private int selectedHour = -1;
    private int selectedMinute = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTask = findViewById(R.id.inputTask);
        btnAdd = findViewById(R.id.btnAdd);
        txtTime = findViewById(R.id.txtTime);
        rvTasks = findViewById(R.id.rvTasks);

        adapter = new TaskAdapter(this, tasks, this);
        rvTasks.setAdapter(adapter);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        txtTime.setOnClickListener(v -> {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    MainActivity.this,
                    (view, hourOfDay, minute1) -> {
                        selectedHour = hourOfDay;
                        selectedMinute = minute1;
                        txtTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    },
                    hour, minute, true
            );
            timePickerDialog.show();
        });

        // Inilah versi kamu yang dimasukkan langsung:
        btnAdd.setOnClickListener(v -> {
            String taskName = inputTask.getText().toString().trim();

            if (taskName.isEmpty()) {
                Toast.makeText(this, "Please enter a task.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedHour == -1 || selectedMinute == -1) {
                Toast.makeText(this, "Please select a time for the reminder.", Toast.LENGTH_SHORT).show();
                return;
            }

            tasks.add(taskName);
            adapter.notifyItemInserted(tasks.size() - 1);
            rvTasks.scrollToPosition(tasks.size() - 1);

            scheduleReminder(taskName, tasks.size(), selectedHour, selectedMinute);

            inputTask.setText("");
            txtTime.setText("Select Time");
            selectedHour = selectedMinute = -1;

            Toast.makeText(this, "Task added & reminder set!", Toast.LENGTH_SHORT).show();
        });
    }

    private void scheduleReminder(String taskName, int notificationId, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("taskName", taskName);
        intent.putExtra("notificationId", notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    @Override
    public void onTaskListChanged() {
        // Optional if needed
    }
}
