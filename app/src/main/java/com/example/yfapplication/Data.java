package com.example.yfapplication;

import android.util.Log;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Data {
    private String message = "nothing";
    private static Data sInstance;
    private static final String TAG = "myLogs";

    public synchronized static Data getInstance() {
        if (sInstance == null) {
            sInstance = new Data();
        }
        return sInstance;
    }

    public static void addmessage(String text){
        // sInstance.message = text;
        //setVariable(text);
        Log.d(TAG, "91f19 data changed");
    }

    public static String getmessage(){
        return sInstance.message;

    }


    //private String variable = "Initial";
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public  void setVariable(String newValue) {
        Log.d(TAG, "91f19 setVariable");
        String oldValue = message;
        message = newValue;
        support.firePropertyChange("variable", oldValue, newValue);
        Log.d(TAG,"91f19 "+ message);
    }
}