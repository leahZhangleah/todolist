package com.example.android.todolist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = Event.class,version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "todolist";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context){
        if (sInstance==null){
            //singleton pattern: the app can only create one single instance of this object
            synchronized (LOCK){
                Log.d(LOG_TAG,"Creating new database instance");
                sInstance = Room.databaseBuilder(context,AppDatabase.class,AppDatabase.DATABASE_NAME).build();
            }
        }
        Log.d(LOG_TAG,"getting the database instance");
        return sInstance;
    }

    public abstract EventDao getDao();
}
