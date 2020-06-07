package com.example.yfapplication;

import android.os.CountDownTimer;
import android.widget.TextView;

public class Timer {
    final private TextView textView;

    private CountDownTimer timer = new CountDownTimer(timeLeftInMillsSeconds, 1000) {
        @Override
        public void onTick(long l) {
            timeLeftInMillsSeconds = l;
            updateTimer();
        }

        @Override
        public void onFinish() {

        }
    };
    private long timeLeftInMillsSeconds;

    public Timer(TextView textView, long timeLeftInMillsSeconds) {
        this.textView = textView;
        this.timeLeftInMillsSeconds = timeLeftInMillsSeconds;
    }

    public void updateTimer() {
        int minutes = (int) timeLeftInMillsSeconds / 60000;
        int seconds = (int) timeLeftInMillsSeconds % 60000 / 1000;
        String time = String.format("%02d : %02d", minutes, seconds);
    }

}
