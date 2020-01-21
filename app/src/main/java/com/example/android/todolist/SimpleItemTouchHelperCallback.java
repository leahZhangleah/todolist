package com.example.android.todolist;

import android.content.Context;

import java.util.List;

public class SimpleItemTouchHelperCallback {
    private String LOG_TAG = SimpleItemTouchHelperCallback.class.getName();
    AppDatabase mDb;
    List<Event> events;

    private ToDoListAdapter adapter;
    public SimpleItemTouchHelperCallback(ToDoListAdapter adapter, Context context){
        this.adapter = adapter;
        mDb = AppDatabase.getsInstance(context);
        events = adapter.getEvents();
    }


}
