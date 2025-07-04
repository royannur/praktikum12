package com.example.todolist;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TaskTest {

    @Test
    public void testTaskName() {
        Task task = new Task("Belajar Unit Test");
        assertEquals("Belajar Unit Test", task.getName());
    }
}
