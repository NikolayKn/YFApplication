package com.example.yfapplication;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;


// Класс для создания таймера готовности блюда
public class Timer {
    private static final String TAG = "myLogs";
    final private TextView textView;
    private long timeLeftInMillsSeconds;

    //TODO реализовать callback
    interface Callback{
        void callingback();
    }

    Callback callback;

    public void registerCallback(Callback callback){
        this.callback = callback;
    }

    private CountDownTimer timer;


    public Timer(TextView textView, long timeLeftInMillsSeconds) {
        this.textView = textView;
        this.timeLeftInMillsSeconds = timeLeftInMillsSeconds*1000;

    }

    // Обновление таймера
    public void updateTimer() {
        int minutes = (int) timeLeftInMillsSeconds / 60000;
        int seconds = (int) timeLeftInMillsSeconds % 60000 / 1000;
        String time = String.format("Ready in %02d : %02d", minutes, seconds);
        textView.setText(time);
    }

    public void startTimer() {
        Log.d(TAG, "91f19 Timer started");
        //Создание таймера
        timer = new CountDownTimer(timeLeftInMillsSeconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillsSeconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                //callback.callingback();
            }
        }.start();
    }
}
