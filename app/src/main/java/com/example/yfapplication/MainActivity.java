
package com.example.yfapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.Request;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sPref;
    final String SAVED_TEXT = "0";
    private OkHttpClient client;
    //private static Waiting_Fragment waiting_fragment = new Waiting_Fragment();
    private MyListener myListener;
    private Data mData;
    private Waiting_Fragment waiting_fragment = new Waiting_Fragment();
    private Cooking_Fragment cooking_fragment = new Cooking_Fragment();
    private Ready_Fragment ready_fragment = new Ready_Fragment();
    private Put_Fragment put_fragment = new Put_Fragment();
    private FragmentTransaction mtransaction = getSupportFragmentManager().beginTransaction();
    enum modeNum { // Режим
        WAITING,
        COOKING,
        READY,
        PUT
    }
    modeNum mode= modeNum.WAITING;
    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData = Data.getInstance();
        myListener = new MyListener();
        mData.addListener(myListener);
       // Waiting_Fragment waiting_fragment = new Waiting_Fragment();






        switch (mode) {
            case WAITING:
                mtransaction.add(R.id.fragment_container,waiting_fragment).commit();
                Log.d(TAG, "91f19 fragment waiting create first time  ");

                break;
            case COOKING:
                mtransaction.add(R.id.fragment_container,cooking_fragment).commit();

                break;
            case READY:
                mtransaction.add(R.id.fragment_container,ready_fragment).commit();

                break;
            case PUT:
                mtransaction.add(R.id.fragment_container,put_fragment).commit();

                break;
        }






        client = new OkHttpClient();
        Log.d(TAG, "91f19start creating request");
        Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        BucketWebSocketListener listener = new BucketWebSocketListener(getApplicationContext());
        WebSocket ws = client.newWebSocket(request, listener);
        Log.d(TAG, "91f19 finish creating websocket");
        client.dispatcher().executorService().shutdown();
    }

    public void setData() {
        Log.d(TAG, "91f19 set_text in main");
        switch (mode) {
            case WAITING:
                mtransaction.replace(R.id.fragment_container,waiting_fragment).commit();
                Log.d(TAG, "91f19 add waiting fragment");
                runOnUiThread(new Runnable() {
                    @Override

                        public void run() {
                            waiting_fragment.fragmentsetText(Data.getInstance().getmessage());
                    }
                });

                break;
            case COOKING:
                mtransaction.replace(R.id.fragment_container,cooking_fragment).commit();
                Log.d(TAG, "91f19 add cooking fragment");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cooking_fragment.fragmentsetText(Data.getInstance().getmessage());

                    }
                });
                break;
            case READY:
                mtransaction.replace(R.id.fragment_container,ready_fragment).commit();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waiting_fragment.fragmentsetText(Data.getInstance().getmessage());

                    }
                });
                break;
            case PUT:
                mtransaction.replace(R.id.fragment_container,put_fragment).commit();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waiting_fragment.fragmentsetText(Data.getInstance().getmessage());

                    }
                });
                break;
        }
    }



    // Методы реализуют сохранение и загрузку выбора spinner

    // Сохранение
    void saveText(int number) {
        Log.d(TAG, "91f19 save text in main");
        Log.d(TAG,"91f19 in save_text_in_main "+ mData.getmessage());
        // Объект shared preference
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        // Сохранение выбора
        ed.putInt(SAVED_TEXT, number);
        ed.commit();

        // toast для проверки
        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();

    }

    // Загрузка
    int loadText() {
        Log.d(TAG, "91f19 load text in main");
        sPref = getPreferences(MODE_PRIVATE);
        // Достаем сохраненное ранее значение (По умолчанию - 0)
        int savedText = sPref.getInt(SAVED_TEXT, 0);
        // toast для проверки
        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();




        return savedText;
    }

    class MyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            String propertyName = event.getPropertyName();
            if ("variableMessage".equals(propertyName)) {
                Log.d(TAG, "91f19 listener works!");
                mode = modeNum.COOKING;
              /*  FragmentTransaction mtransaction = getSupportFragmentManager().beginTransaction();
                switch (mode){
                    case WAITING:
                        mtransaction.remove(waiting_fragment).commit();//.commit();

                        Log.d(TAG, "91f19 remove waiting fragment");
                        break;
                    case COOKING:
                        mtransaction.remove(cooking_fragment);//.commit();
                        break;
                    case READY:
                        mtransaction.remove(ready_fragment);//.commit();
                        break;
                    case PUT:
                        mtransaction.remove(put_fragment);//.commit();
                        break;
                }*/
                setData();
            }
            if ("variableBucket".equals(propertyName)){

            }
        }
    }


}
