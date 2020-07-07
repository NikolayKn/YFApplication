
package com.example.yfapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.neovisionaries.ws.client.HostnameUnverifiedException;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class MainActivity extends AppCompatActivity {

    public static boolean isDebuggingMode = false;

    SharedPreferences sPref;
    final String SAVED_TEXT = "0";
    private WebSocketFactory factory;
    private com.neovisionaries.ws.client.WebSocket ws;
    private MyListener myListener;
    private  boolean isChargingNew;
    private  boolean isChargingOld = true;
    private Data mData;
    private FullFragment waiting_fragment = new FullFragment("WAITING");
    private FullFragment cooking_fragment = new FullFragment("COOKING");
    private FullFragment ready_fragment = new FullFragment("READY");
    private FullFragment put_fragment = new FullFragment("PUT");
    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int ChargingStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            isChargingNew = ChargingStatus != BatteryManager.BATTERY_STATUS_DISCHARGING;
            if (isChargingNew == isChargingOld) return;
            isChargingOld = isChargingNew;
            if (!isChargingOld) {
                Toast.makeText(MainActivity.this, "not charging", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ActivityWithoutCharging.class));
                finish();
            }
            Log.d(TAG, "91f19 BATTERY: " + isChargingOld);
        }
    };
    private static final String TAG = "myLogs";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        mData = Data.getInstance();
        loadText();
        myListener = new MyListener();
        mData.addListener(myListener);
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        switch (Data.getInstance().getMode()) {
            case WAITING:
                mTransaction.add(R.id.fragment_container,waiting_fragment).commit();
                Log.d(TAG, "91f19 fragment waiting create first time  ");
                break;
            case COOKING:
                mTransaction.add(R.id.fragment_container,cooking_fragment).commit();

                break;
            case READY:
                mTransaction.add(R.id.fragment_container,ready_fragment).commit();

                break;
            case PUT:
                mTransaction.add(R.id.fragment_container,put_fragment).commit();

                break;
        }
        Button btn = (Button) findViewById(R.id.main_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDebuggingMode){
                    isDebuggingMode = false;
                    setConnection();
                    Toast.makeText(MainActivity.this, "Exiting debugging mode", Toast.LENGTH_SHORT).show();
                }
                else {
                    isDebuggingMode = true;
                    Toast.makeText(MainActivity.this, "Entering debugging mode", Toast.LENGTH_SHORT).show();
                    closeConnection("Entering debugging mode");
                    FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
                    mTransaction.replace(R.id.fragment_container, waiting_fragment);
                }
            }
        });
        setConnection();

    }


    // Метод устанавливает соединение с вебсокетом
    private void setConnection(){
        try {
            ExecutorService s = Executors.newSingleThreadExecutor();
            factory = new WebSocketFactory().setConnectionTimeout(5000);
            ws = factory.createSocket("ws://192.168.1.100:8080/yf");
            ws.addListener(new BucketWebSocketListenerTrue());
            Log.d(TAG, "91f19 Start connection");
            Future<WebSocket> future = ws.connect(s);
            future.get();
            boolean flag = ws.isOpen();
            Log.d(TAG, "91f19 " + flag);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "91f19 Failed to connect websocket");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection(String reason){
        ws = ws.disconnect(1000, reason);
    }

    public void setData() {
        Log.d(TAG, "91f19 set_text in main");
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        switch (Data.getInstance().getMode()) {
            case WAITING:
                fragmentTransaction.replace(R.id.fragment_container, waiting_fragment).commit();
                Log.d(TAG, "91f19 add waiting fragment");
                break;
            case COOKING:
                Log.d(TAG, "91f19 add cooking fragment");
                fragmentTransaction.replace(R.id.fragment_container, cooking_fragment).commit();
                break;
            case READY:
                Log.d(TAG, "91f19 add ready fragment");
                fragmentTransaction.replace(R.id.fragment_container,ready_fragment).commit();
                break;
            case PUT:
                Log.d(TAG, "91f19 add put fragment");
                fragmentTransaction.replace(R.id.fragment_container,put_fragment).commit();
                break;
        }
    }

    // Методы реализуют сохранение и загрузку выбора spinner

    // Сохранение
    void saveText() {
        Log.d(TAG, "91f19 save text in main");
       // Объект shared preference
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        // Сохранение выбора
        ed.putInt(SAVED_TEXT, mData.getbucket());
        ed.commit();

        Toast.makeText(this, "bucketId saved", Toast.LENGTH_SHORT).show();
    }

    // Загрузка
    void loadText() {
        Log.d(TAG, "91f19 load text in main");
        sPref = getPreferences(MODE_PRIVATE);
        // Достаем сохраненное ранее значение (По умолчанию - 0)
        mData.setVariableBucket(sPref.getInt(SAVED_TEXT, 0));
        // toast для проверки
        Toast.makeText(this, "bucketId loaded", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "91f19 onStop mainActivity");
        saveText();
        closeConnection("Activity destroyed");
        //unregisterReceiver(batteryReceiver);
        mData.removeListener(myListener);
        super.onStop();
    }

    private void changeBucket(){
        JSONObject json = new JSONObject();
        JSONObject dataJson = new JSONObject();
        try {
            json.put("com", "InitModuleLcd");
            dataJson.put("moduleId", Data.getInstance().getbucket() + 1);
            //dataJson.put("Mode", 2);
            //dataJson.put("Name", "Nikolay");
            //dataJson.put("OrderId", 1223);
            //dataJson.put("BowlName", "Cesar Salad");
            //dataJson.put("TimeCooking", 15);
            json.put("data", dataJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "91f19 " + json.toString());
        ws.sendText(json.toString());
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "91f19 onDestroy mainActivity");
        Toast.makeText(MainActivity.this, "DESTROY!!!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Main destroyed");
        super.onDestroy();
    }

    class MyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            String propertyName = event.getPropertyName();
            if ("variableMode".equals(propertyName)) {
                Log.d(TAG, "91f19 listener works!");
                setData();
            }
            if ("variableBucket".equals(propertyName)){
                changeBucket();
                saveText();
            }
        }
    }




}
