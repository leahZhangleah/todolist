package com.example.android.todolist.dagger;

import android.app.Application;

import com.example.android.todolist.AppDatabase;
import com.example.android.todolist.EventDao;
import com.example.android.todolist.Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class EventModule {
    Application application;
    public EventModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    Repository providesRepository(EventDao eventDao){
        Repository repository = new Repository(eventDao);
        return repository;
    }

    @Provides
    @Singleton
    EventDao providesEventDao(AppDatabase appDatabase){
        EventDao eventDao = appDatabase.getDao();
        return eventDao;
    }

    @Provides
    @Singleton
    AppDatabase providesDataBase(){
        AppDatabase appDatabase = AppDatabase.getsInstance(application);
        return appDatabase;
    }
}
