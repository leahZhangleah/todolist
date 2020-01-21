package com.example.android.todolist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel {
    private int mId;
    private Repository mRepository;

    public DetailViewModel(Repository repository, int id) {
        mRepository = repository;
        mId = id;
    }

    public LiveData<Event> loadEventById(){
        return mRepository.getEventById(mId);
    }
}
