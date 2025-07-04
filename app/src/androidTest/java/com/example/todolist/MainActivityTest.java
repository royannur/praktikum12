package com.example.todolist;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.todolist.R;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void addTask_displaysInTextView() {
        onView(withId(com.example.todolist.R.id.inputTask)).perform(typeText("Uji UI Test"), closeSoftKeyboard());
        onView(withId(R.id.btnAdd)).perform(click());
        onView(withId(R.id.txtTask)).check(matches(withText("Uji UI Test")));
    }
}
