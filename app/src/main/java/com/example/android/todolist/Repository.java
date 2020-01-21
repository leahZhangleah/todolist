package com.example.android.todolist;

import android.arch.lifecycle.LiveData;

import java.util.List;

public class Repository {
    private EventDao mEventDao;
    private LiveData<List<Event>> events;
    private LiveData<Event> event;

    public Repository(EventDao eventDao){
        mEventDao = eventDao;

    }
    public LiveData<List<Event>> getEvents(){
        events = mEventDao.loadEvents();
        return events;
    }

    public LiveData<Event> getEventById(int id){
        event = mEventDao.loadTaskById(id);
        return event;
    }
}
