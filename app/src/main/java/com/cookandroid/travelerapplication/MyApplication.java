package com.cookandroid.travelerapplication;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static MyApplication instance;

    public MyApplication() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}

