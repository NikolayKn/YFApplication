package com.example.yfapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class batteryReceiver extends BroadcastReceiver {

    public batteryReceiver() {
    }

    private boolean isChargingNew;
    private boolean isChargingOld = true;
    private static final String TAG = "myLogs";
    private final String DISCONNECTED_ACTION = "android.intent.action.ACTION_POWER_DISCONNECTED";
    private final String CONNECTED_ACTION = "android.intent.action.ACTION_POWER_CONNECTED";


    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        Toast.makeText(context, "BATTERY receiver works!", Toast.LENGTH_SHORT).show();
        String action = intent.getAction();
        if (CONNECTED_ACTION.equalsIgnoreCase(action)) {
            Toast.makeText(context, "charging", Toast.LENGTH_SHORT).show();
            Data.getInstance().setPhoneStatus(true);
            return;
        }
        if (DISCONNECTED_ACTION.equalsIgnoreCase(action)) {
            Data.getInstance().setPhoneStatus(false);
            Toast.makeText(context, "not charging", Toast.LENGTH_SHORT).show();

        }

    }
}
