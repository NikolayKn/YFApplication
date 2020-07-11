
package com.example.yfapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    public static boolean isDebuggingMode = false;

    SharedPreferences sPref;
    final String SAVED_TEXT = "0";
    private com.neovisionaries.ws.client.WebSocket ws;
    private MyListener myListener;
    private boolean isCreate = true;
    private Data mData;
    private FullFragment waiting_fragment = new FullFragment("WAITING");
    private FullFragment cooking_fragment = new FullFragment("COOKING");
    private FullFragment ready_fragment = new FullFragment("READY");
    private FullFragment put_fragment = new FullFragment("PUT");
    private static final String TAG = "myLogs";

    void turnOff() {
        Log.d(TAG, "91f19 mainActivity_turnOff");
        if (mData.isChargingStatus()) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.screenBrightness = -1;
            getWindow().setAttributes(params);
        } else {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.screenBrightness = 0;
            getWindow().setAttributes(params);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData = Data.getInstance();
        loadText();
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this, R.array.choice, R.layout.spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner_main);

        spinner.setAdapter(Adapter);
        spinner.setPrompt("Title");

        int bucket = mData.getInstance().getbucket();
        Log.d(TAG, "91f19 Last chose selected " + bucket);
        spinner.setSelection(bucket);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "91f19 Spinner item selected " + i);
                Data.getInstance().setVariableBucket(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        myListener = new MyListener();
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        switch (Data.getInstance().getMode()) {
            case WAITING:
                mTransaction.add(R.id.fragment_container, waiting_fragment).commit();
                Log.d(TAG, "91f19 fragment waiting create first time  ");
                break;
            case COOKING:
                mTransaction.add(R.id.fragment_container, cooking_fragment).commit();

                break;
            case READY:
                mTransaction.add(R.id.fragment_container, ready_fragment).commit();

                break;
            case PUT:
                mTransaction.add(R.id.fragment_container, put_fragment).commit();

                break;
        }
        Button btn = findViewById(R.id.main_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDebuggingMode) return;
                switch (mData.getMode()) {
                    case WAITING:
                        mData.setVariableModeDebug(1);
                        Toast.makeText(MainActivity.this, "cooking debugging mode", Toast.LENGTH_SHORT).show();
                        break;
                    case COOKING:
                        mData.setVariableModeDebug(2);
                        Toast.makeText(MainActivity.this, "ready debugging mode", Toast.LENGTH_SHORT).show();
                        break;
                    case READY:
                        mData.setVariableModeDebug(3);
                        Toast.makeText(MainActivity.this, "put debugging mode", Toast.LENGTH_SHORT).show();
                        break;
                    case PUT:
                        mData.setVariableModeDebug(0);
                        Toast.makeText(MainActivity.this, "waiting debugging mode", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (isDebuggingMode) {
                    isDebuggingMode = false;
                    setConnection();
                    Toast.makeText(MainActivity.this, "Exiting debugging mode", Toast.LENGTH_SHORT).show();
                } else {
                    isDebuggingMode = true;
                    Toast.makeText(MainActivity.this, "Entering debugging mode", Toast.LENGTH_SHORT).show();
                    closeConnection("Entering debugging mode");
                    FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
                    mTransaction.replace(R.id.fragment_container, waiting_fragment);
                }
                return true;
            }
        });
        // Загрузка состояния зарядки при запуске приложения
        Intent batteryStatus = getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert batteryStatus != null;
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
        mData.setPhoneStatus(isCharging);

        //Установление соединения с вебсокетом
        setConnection();
    }


    @Override
    protected void onStart() {
        Log.d(TAG, "91f19 onStart");
        mData.addListener(myListener);
        if (isCreate) isCreate = false;
        else {
            try {
                recreateConnection();
            } catch (IOException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onStart();
    }


    // Метод устанавливает соединение с вебсокетом
    private void setConnection() {
        try {
            ExecutorService s = Executors.newSingleThreadExecutor();
            Log.d(TAG, "91f19 Start connection");
            WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(3000);
            ws = factory.createSocket("ws://192.168.1.100:8080/yf");
            ws.addListener(new BucketWebSocketListenerTrue());
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

    private void recreateConnection() throws IOException, ExecutionException, InterruptedException {
        ExecutorService s = Executors.newSingleThreadExecutor();
        Log.d(TAG, "91f19 Start connection");
        ws = ws.recreate();
        Future<WebSocket> future = ws.connect(s);
        future.get();
    }

    private void closeConnection(String reason) {
        ws.disconnect(1000, reason);
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
                fragmentTransaction.replace(R.id.fragment_container, ready_fragment).commit();
                break;
            case PUT:
                Log.d(TAG, "91f19 add put fragment");
                fragmentTransaction.replace(R.id.fragment_container, put_fragment).commit();
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

    private void changeBucket() {
        JSONObject json = new JSONObject();
        JSONObject dataJson = new JSONObject();
        try {
            json.put("com", "ChangeModuleLcd");
            dataJson.put("moduleId", Data.getInstance().getbucket() + 1);
            json.put("data", dataJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "91f19 " + json.toString());
        ws.sendText(json.toString());
    }


    class MyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            String propertyName = event.getPropertyName();
            if ("variableMode".equals(propertyName)) {
                Log.d(TAG, "91f19 listener works!");
                setData();
            }
            if ("variableBucket".equals(propertyName)) {
                changeBucket();
                saveText();
            }
            if ("PhoneStatus".equals(propertyName)) {
                turnOff();
            }
        }
    }


}
