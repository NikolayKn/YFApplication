package com.example.yfapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityWithoutCharging extends AppCompatActivity {

    private boolean isChargingNew;
    private boolean isChargingOld = false;


    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int ChargingStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            isChargingNew = ChargingStatus != BatteryManager.BATTERY_STATUS_DISCHARGING;
            if (isChargingNew == isChargingOld) return;
            isChargingOld = isChargingNew;
            if (isChargingOld) {
                Toast.makeText(ActivityWithoutCharging.this, "charging", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActivityWithoutCharging.this, MainActivity.class));
                finish();
            }
            Log.d(TAG, "91f19 BATTERY: " + isChargingOld);
        }
    };
    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.without_charging_activity);
        Log.d(TAG, "91f19 create new activity");
        noChargeFragment chargeFragment = new noChargeFragment();
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_container2, chargeFragment).commit();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(batteryReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "91f19 destroy new activity");
    }

    @Override
    public void onBackPressed() {
        return;
    }
}

