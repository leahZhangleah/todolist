package com.example.android.todolist;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private int mId;
    private Repository mRepository;
    public DetailViewModelFactory(Repository repository,int id) {
        mRepository = repository;
        mId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new DetailViewModel(mRepository,mId);
    }

}
