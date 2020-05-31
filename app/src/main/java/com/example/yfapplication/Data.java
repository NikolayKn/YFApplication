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
    private String message = "Nothing";
    private bucketNum bucket; // Номер ведра
    private String name; // Имя заказчика
    private int ordername; // Номер заказа
    private String mealname; // Название блюда
    private int timecooking; // Время готовки
    private modeNum mode; // Режим


    public synchronized static Data getInstance() {
        if (sInstance == null) {
            sInstance = new Data();
        }
        return sInstance;
    }


    public static String getmessage(){
        return sInstance.message;

    }

    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void setVariableMessage(String newValue) {
        Log.d(TAG, "91f19 setVariableMessage");
        String oldValue = message;
        message = newValue;
        Log.d(TAG,"91f19 in_setvariable_before "+ message);
        support.firePropertyChange("variableMessage", oldValue, newValue);
        Log.d(TAG,"91f19 in_setvariable_after "+ message);
    }

    public void setVariableBucket(bucketNum newValue) {
        Log.d(TAG, "91f19 setVariableBucket");
        bucketNum oldValue = bucket;
        bucket = newValue;
        Log.d(TAG,"91f19 in_setvariable_before "+ bucket.toString());
        support.firePropertyChange("variableBucket", oldValue, newValue);
        Log.d(TAG,"91f19 in_setvariable_after "+ bucket.toString());
    }

    //Json parser
    public void JsonParser(JSONObject json){
        try {
            switch (json.getInt("mode")){
                case 0: // Режим ожидания заказа (Нет никакой информации)
                    mode = modeNum.WAITING;
                    break;
                case 1: //Режим готовки блюда (Информация есть)
                    mode = modeNum.COOKING;
                    name = json.getString("name");
                    ordername = json.getInt("ordername");
                    mealname = json.getString("mealname");
                    timecooking = json.getInt("timecooking");
                    break;
                case 2: //Режим готовности блюда (Информация есть)
                    mode = modeNum.READY;
                    name = json.getString("name");
                    ordername = json.getInt("ordername");
                    mealname = json.getString("mealname");
                    timecooking = json.getInt("timecooking");
                    break;
                case 3: // Режим смены тарелки (Информация есть)
                    mode = modeNum.PUT;
                    name = json.getString("name");
                    ordername = json.getInt("ordername");
                    mealname = json.getString("mealname");
                    timecooking = json.getInt("timecooking");
                    break;
            }
            Log.d(TAG, "91f19 Updating data by json");
        }
        catch (JSONException e){
            Log.d(TAG, "91f19 JSON ERROR");
        }
    }
}

enum bucketNum {
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4);
    private int num;
    bucketNum(int num){
        this.num = num;
    }
};

enum modeNum { // Режим
    WAITING(0),
    COOKING(1),
    READY(2),
    PUT(3);
    private int num;
    modeNum(int num){
        this.num = num;
    }
};