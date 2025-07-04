package com.example.todolist;

public class Task {
    private String name;
    private boolean completed;
    private long reminderTimeMillis;  // waktu pengingat dalam millisecs (epoch)

    public Task(String name) {
        this.name = name;
        this.completed = false;
        this.reminderTimeMillis = 0;  // default tidak ada pengingat
    }

    // Getter & Setter untuk semua field, termasuk reminderTimeMillis
    // ...
}


