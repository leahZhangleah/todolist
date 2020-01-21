package com.example.android.todolist;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private Repository mRepository;

    public MainViewModelFactory(Repository repository){
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(mRepository);
    }
}
