package com.example.android.todolist;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM events")
    LiveData<List<Event>> loadEvents();

    @Insert
    void insertEvent(Event event);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEvent(Event event);

    @Delete
    void deleteEvent(Event event);

    @Query("SELECT * FROM events WHERE id = :id")
    LiveData<Event> loadTaskById(int id);
}
