package com.example.android.todolist;

import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    private static final Object LOCK = new Object();
    private static AppExecutor sInstance;
    private final Executor diskIO,mainThread,networkIO;

    public AppExecutor(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    public static AppExecutor getsInstance(){
        if (sInstance==null){
            synchronized (LOCK){
                sInstance = new AppExecutor(Executors.newSingleThreadExecutor(),
                        new MainThreadExecutor(),
                        Executors.newFixedThreadPool(3));

            }
        }
        return sInstance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private android.os.Handler mainThreadHandler = new android.os.Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
