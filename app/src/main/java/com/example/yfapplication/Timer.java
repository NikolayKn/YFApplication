package com.example.yfapplication;

import android.annotation.SuppressLint;
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
        void callingBack();
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

        @SuppressLint("DefaultLocale")
        String time = String.format("%d min\n left", minutes + 1);

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
                String time = String.format("Soon");
                textView.setText(time);
                //callback.callingBack();
            }
        }.start();
    }

}
