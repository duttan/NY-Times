package com.example.nytimes.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;



public class MainApplication extends Application {

    public static volatile Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        MainApplication.context = getApplicationContext();
    }

}
