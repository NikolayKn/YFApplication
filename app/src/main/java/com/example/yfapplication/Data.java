package com.example.yfapplication;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.json.*;

import androidx.annotation.Nullable;

public class Data {
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private static Data sInstance;
    private static final String TAG = "myLogs";
    private bucketNum bucket; // Номер ведра
    private String name; // Имя заказчика
    private int ordername; // Номер заказа
    private String mealname; // Название блюда
    private int timecooking; // Время готовки
    private modeNum mode = modeNum.WAITING; // Режим


    public synchronized static Data getInstance() {
        if (sInstance == null) {
            sInstance = new Data();
        }
        return sInstance;
    }


    public modeNum getMode() {
        return sInstance.mode;
    }

    public int getbucket() {
        return sInstance.bucket.ordinal();
    }


    public String getName() {
        return sInstance.name;
    }

    public int getOrdername() {
        return sInstance.ordername;
    }

    public String getMealname() {
        return sInstance.mealname;
    }

    public int getTimecooking() {
        return sInstance.timecooking;
    }

    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    private void setVariableMode(modeNum newValue) {
        Log.d(TAG, "91f19 setVariableMode");
        modeNum oldValue = mode;
        mode = newValue;
        support.firePropertyChange("variableMode", oldValue, newValue);
    }

    public void setVariableBucket(int newVal) {
        bucketNum NewValue = bucketNum.FIRST;
        switch (newVal) {
            case 0:
                NewValue = bucketNum.FIRST;
                break;
            case 1:
                NewValue = bucketNum.SECOND;
                break;
            case 2:
                NewValue = bucketNum.THIRD;
                break;
            case 3:
                NewValue = bucketNum.FOURTH;
                break;

        }
        Log.d(TAG, "91f19 setVariableBucket");
        bucketNum oldValue = bucket;
        bucket = NewValue;
        support.firePropertyChange("variableBucket", oldValue, NewValue);
    }

    //Json parser
    public void JsonParser(JSONObject json) {
        try {
            switch (json.getInt("mode")) {
                case 0: // Режим ожидания заказа (Нет никакой информации)
                    sInstance.setVariableMode(modeNum.WAITING);
                    break;
                case 1: //Режим готовки блюда (Информация есть)
                    sInstance.setVariableMode(modeNum.COOKING);
                    name = json.getString("name");
                    ordername = json.getInt("orderId");
                    mealname = json.getString("bowlName");
                    timecooking = json.getInt("timecooking");
                    break;
                case 2: //Режим готовности блюда (Информация есть)
                    sInstance.setVariableMode(modeNum.READY);
                    name = json.getString("name");
                    ordername = json.getInt("orderId");
                    mealname = json.getString("bowlName");
                    timecooking = json.getInt("timecooking");
                    break;
                case 3: // Режим смены тарелки (Информация есть)
                    sInstance.setVariableMode(modeNum.PUT);
                    name = json.getString("name");
                    ordername = json.getInt("orderId");
                    mealname = json.getString("bowlName");
                    timecooking = json.getInt("timecooking");
                    break;
            }
            Log.d(TAG, "91f19 Updating data by json");
        } catch (JSONException e) {
            Log.d(TAG, "91f19 JSON ERROR");
        }
    }
}

enum bucketNum {
    FIRST,
    SECOND,
    THIRD,
    FOURTH

};

enum modeNum { // Режим
    WAITING,
    COOKING,
    READY,
    PUT

};