package com.example.yfapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class batteryReceiver extends BroadcastReceiver {

    public  batteryReceiver(){}

    private  boolean isChargingNew;
    private  boolean isChargingOld = true;
    private static final String TAG = "myLogs";

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        Toast.makeText(context, "AAA", Toast.LENGTH_SHORT).show();
        /*int ChargingStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        isChargingNew = ChargingStatus != BatteryManager.BATTERY_STATUS_DISCHARGING;
        if (isChargingNew == isChargingOld) return;
        isChargingOld = isChargingNew;
        if (!isChargingOld) {
            Toast.makeText(context, "not charging", Toast.LENGTH_SHORT).show();
            //context.startActivity(new Intent(context, ActivityWithoutCharging.class));
        }*/
        //Log.d(TAG, "91f19 BATTERY: " + isChargingOld);
    }
}
