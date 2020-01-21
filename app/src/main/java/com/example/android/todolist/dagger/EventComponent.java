package com.example.android.todolist.dagger;

import com.example.android.todolist.DetailActivity;
import com.example.android.todolist.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {EventModule.class})
public interface EventComponent {
    void inject(MainActivity activity);
    void inject(DetailActivity activity);
}
