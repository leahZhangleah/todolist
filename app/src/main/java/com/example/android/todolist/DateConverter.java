package com.example.android.todolist;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public long toTimestamp(Date date){
        return date!=null? date.getTime():null;
    }
    @TypeConverter
    public Date toDate(Long timeStamp){
        return timeStamp !=null? new Date(timeStamp):null;
    }
}
