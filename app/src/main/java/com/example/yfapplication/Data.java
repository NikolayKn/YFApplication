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
    private String name = "default"; // Имя заказчика
    private int ordername = 101; // Номер заказа
    private String mealname = "default"; // Название блюда
    private int timecooking = 62; // Время готовки
    private modeNum mode = modeNum.WAITING; // Режим


    synchronized static Data getInstance() {
        if (sInstance == null) {
            sInstance = new Data();
        }
        return sInstance;
    }


    modeNum getMode() {
        return sInstance.mode;
    }

    int getbucket() {
        return sInstance.bucket.ordinal();
    }


    public String getName() {
        return sInstance.name;
    }

    String getOrdername() {
        return String.valueOf(sInstance.ordername);
    }

    String getMealname() {
        return sInstance.mealname;
    }

    int getTimecooking() {
        return sInstance.timecooking;
    }

    void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    void removeListener(PropertyChangeListener listener){
        support.removePropertyChangeListener(listener);
    }

    private void setVariableMode(modeNum newValue) {
        Log.d(TAG, "91f19 setVariableMode");
        modeNum oldValue = mode;
        mode = newValue;
        support.firePropertyChange("variableMode", oldValue, newValue);
    }

    public void setVariableModeDebug(int i){
        modeNum value = modeNum.WAITING;
        switch (i){
            case 0:
                value = modeNum.WAITING;
                break;
            case 1:
                value = modeNum.COOKING;
                break;
            case 2:
                value = modeNum.READY;
                break;
            case 3:
                value = modeNum.PUT;
                break;
        }
        setVariableMode(value);
    }

    void setVariableBucket(int newVal) {
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
        Log.d(TAG, "91f19 setVariableBucket # " + newVal);
        bucketNum oldValue = bucket;
        bucket = NewValue;
        support.firePropertyChange("variableBucket", oldValue, NewValue);
    }

    //Json parser
    void JsonParser(JSONObject json_inner) {
            try {
                JSONObject json = json_inner.getJSONObject("data");
                switch (json.getInt("Mode")) {
                    case 0: // Режим ожидания заказа (Нет никакой информации)
                        sInstance.setVariableMode(modeNum.WAITING);
                        break;
                    case 1: //Режим готовки блюда (Информация есть)
                        sInstance.setVariableMode(modeNum.COOKING);
                        name = json.getString("Name");
                        ordername = json.getInt("OrderId");
                        mealname = json.getString("BowlName");
                        timecooking = json.getInt("TimeCooking");
                        break;
                    case 2: //Режим готовности блюда (Информация есть)
                        sInstance.setVariableMode(modeNum.READY);
                        name = json.getString("Name");
                        ordername = json.getInt("OrderId");
                        mealname = json.getString("BowlName");
                        timecooking = json.getInt("TimeCooking");
                        break;
                    case 3: // Режим смены тарелки (Информация есть)
                        sInstance.setVariableMode(modeNum.PUT);
                        name = json.getString("Name");
                        ordername = json.getInt("OrderId");
                        mealname = json.getString("BowlName");
                        timecooking = json.getInt("TimeCooking");
                        break;
                }
                Log.d(TAG, "91f19 Updating data by json");
            } catch (JSONException e) {
                e.printStackTrace();
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