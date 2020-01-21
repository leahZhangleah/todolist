package com.example.android.todolist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "events")
public class Event {
    @PrimaryKey (autoGenerate = true)
    private int id;
    @ColumnInfo (name = "priority_level")
    private int priorityLevel;
    private String event;
    private Date date;

    @Ignore
    public Event(int id, int priorityLevel, String event,Date date) {
        this.id = id;
        this.priorityLevel = priorityLevel;
        this.event = event;
        this.date = date;
    }

    public Event(int priorityLevel, String event, Date date) {
        this.priorityLevel = priorityLevel;
        this.event = event;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public String getEvent() {
        return event;
    }

    public Date getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
