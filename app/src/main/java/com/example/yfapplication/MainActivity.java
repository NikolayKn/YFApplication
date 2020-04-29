
package com.example.yfapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
        mData =Data.getInstance();
        myListener = new MyListener();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mfragment)
                .commit();
        client = new OkHttpClient();
        Log.d(TAG, "91f19start creating request");
        Request request = new Request.Builder().url("ws://echo.websocket.org").build();
        EchoWebSocketListener listener = new EchoWebSocketListener(getApplicationContext());
        WebSocket ws = client.newWebSocket(request, listener);
        Log.d(TAG, "91f19 finish creating websocket");
        //client.dispatcher().executorService().shutdown();



    }

    public static void setText() {
        Log.d(TAG, "91f19 settext in main");
        mfragment.setText(Data.getInstance().getmessage());
    }


    // Методы реализуют сохранение и загрузку выбора spinner

    // Сохранение
    void saveText(int number) {
        Log.d(TAG, "91f19 save text in main");
        Log.d(TAG,"91f19 "+ mData.getmessage());
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
            Log.d(TAG, "listener works!");
            setText();
            //System.out.println("Property \"" + event.getPropertyName() + "\" has new value: " + event.getNewValue());
        }
    }


}
