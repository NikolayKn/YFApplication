package com.example.yfapplication;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;


// Класс для создания таймера готовности блюда
class Timer {
    private static final String TAG = "myLogs";
    final private TextView textView;
    private long timeLeftInMillsSeconds;

    /*interface Callback{
        //void callingBack();
    }

    private Callback callback;

    public void registerCallback(Callback callback){
        this.callback = callback;
    }*/


    Timer(TextView textView, long timeLeftInMillsSeconds) {
        this.textView = textView;
        this.timeLeftInMillsSeconds = timeLeftInMillsSeconds*1000;

    }

    // Обновление таймера
    private void updateTimer() {
        int minutes = (int) timeLeftInMillsSeconds / 60000;

        @SuppressLint("DefaultLocale")
        String time = String.format("%d min\n left", minutes + 1);

        textView.setText(time);
    }

    void startTimer() {
        Log.d(TAG, "91f19 Timer started");
        //Создание таймера
        //callback.callingBack();
        new CountDownTimer(timeLeftInMillsSeconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillsSeconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                String time = "Soon";
                textView.setText(time);
                //callback.callingBack();
            }
        }.start();
    }

}
