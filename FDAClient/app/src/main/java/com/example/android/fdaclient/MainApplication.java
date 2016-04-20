package com.example.android.fdaclient;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by sanji on 4/19/2016.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
