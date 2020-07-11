package com.example.yfapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class batteryReceiver extends BroadcastReceiver {

    public batteryReceiver() {
    }

    private static final String TAG = "myLogs";

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        Toast.makeText(context, "BATTERY receiver works!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "91f19 BATTERY receiver works!");
        String action = intent.getAction();
        String CONNECTED_ACTION = "android.intent.action.ACTION_POWER_CONNECTED";
        if (CONNECTED_ACTION.equalsIgnoreCase(action)) {
            Toast.makeText(context, "charging", Toast.LENGTH_SHORT).show();
            Data.getInstance().setPhoneStatus(true);
            return;
        }
        String DISCONNECTED_ACTION = "android.intent.action.ACTION_POWER_DISCONNECTED";
        if (DISCONNECTED_ACTION.equalsIgnoreCase(action)) {
            Data.getInstance().setPhoneStatus(false);
            Toast.makeText(context, "not charging", Toast.LENGTH_SHORT).show();

        }

    }
}
