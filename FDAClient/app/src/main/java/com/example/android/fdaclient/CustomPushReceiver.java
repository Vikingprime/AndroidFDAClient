package com.example.android.fdaclient;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Vikingprime on 4/20/2016.
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {

    public CustomPushReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {

        Log.d("TAG","PUSH RECEIVED");
        super.onPushReceive(context, intent);
        Log.d("TAG","PUSH RECEIVED");

    }
}
