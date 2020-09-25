package com.alexlearn.mvvmappfood;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    private static AppExecutors instance;

    public static AppExecutors getInstance(){
        if(instance == null){
            instance = new AppExecutors();
        }
        return instance;
    }

    // works with background thread
   private final Executor mDiskIO = Executors.newSingleThreadExecutor();
    //sent info from background thread to main thread
    private final Executor mMainThreadExecutor = new MainThreadExecutor();

    public Executor diskIO(){
        return mDiskIO;
    }

    public Executor mainThread(){
        return mMainThreadExecutor;
    }

    //This class post data to the main thread
    private static class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler  = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}
