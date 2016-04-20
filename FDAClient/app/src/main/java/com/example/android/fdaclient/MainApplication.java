package com.example.android.fdaclient;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by sanji on 4/19/2016.
 */
public class MainApplication extends Application {
    private static MainApplication sInstance;

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized MainApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG","In application onCreate");
        Parse.initialize(this, getResources().getString(R.string.applicationid),
                getResources().getString(R.string.clientkey));


//        ParsePush.subscribeInBackground("", new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//
//                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
//                } else {
//                    Log.e("com.parse.push", "failed to subscribe for push", e);
//                }
//            }
//        });

        ParseInstallation.getCurrentInstallation().saveInBackground();
        // initialize the singleton
        sInstance = this;

    }
}
