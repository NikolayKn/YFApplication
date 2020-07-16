package com.example.yfapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class statusReceiver extends BroadcastReceiver {

    public statusReceiver() {
    }

    private static final String TAG = "myLogs";

    @Override
    public void onReceive(Context context, Intent intent) {
        String CONNECTED_ACTION = "android.intent.action.ACTION_POWER_CONNECTED";
        String DISCONNECTED_ACTION = "android.intent.action.ACTION_POWER_DISCONNECTED";
        String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
        // an Intent broadcast.
       // Toast.makeText(context, "BATTERY receiver works!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "91f19 receiver works!");
        String action = intent.getAction();
        if (CONNECTED_ACTION.equalsIgnoreCase(action)) {
            //Toast.makeText(context, "charging", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "91f19 charging");
            Data.getInstance().setPhoneStatus(true);
            return;
        }
        if (DISCONNECTED_ACTION.equalsIgnoreCase(action)) {
            Data.getInstance().setPhoneStatus(false);
            Log.d(TAG, "91f19 not charging");
           // Toast.makeText(context, "not charging", Toast.LENGTH_SHORT).show();
        }
        if (BOOT_ACTION.equalsIgnoreCase(action)) {
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }


    }
}
