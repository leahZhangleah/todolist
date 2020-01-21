package com.example.android.todolist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class MainViewModel extends ViewModel {
    private Repository mRepository;


    public MainViewModel(Repository repository){
        mRepository = repository;
    }

    public LiveData<List<Event>> getEvents() {
        return mRepository.getEvents();
    }

}
