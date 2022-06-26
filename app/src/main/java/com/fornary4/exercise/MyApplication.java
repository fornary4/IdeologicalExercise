package com.fornary4.exercise;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

public class MyApplication extends Application {
    private static MyApplication mAPP;

    public static MyApplication getInstance() {
        return mAPP;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAPP = this;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
