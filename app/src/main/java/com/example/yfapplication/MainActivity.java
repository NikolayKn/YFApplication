
package com.example.yfapplication;

import androidx.appcompat.app.AppCompatActivity;

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
    private static MyFragment mfragment = new MyFragment();
    private MyListener myListener;
    private Data mData;



    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData = Data.getInstance();
        myListener = new MyListener();
        mData.addListener(myListener);


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mfragment)
                .commit();
        client = new OkHttpClient();
        Log.d(TAG, "91f19start creating request");
        Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        BucketWebSocketListener listener = new BucketWebSocketListener(getApplicationContext());
        WebSocket ws = client.newWebSocket(request, listener);
        Log.d(TAG, "91f19 finish creating websocket");
        client.dispatcher().executorService().shutdown();
    }

    public void setText() {
        Log.d(TAG, "91f19 set_text in main");
       // mfragment.setText(Data.getInstance().getmessage());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mfragment.fragmentsetText(Data.getInstance().getmessage());
                //output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
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
                System.out.println("listener_works!!!!");
                setText();
            }
            if ("variableBucket".equals(propertyName)){

            }
        }
    }


}
