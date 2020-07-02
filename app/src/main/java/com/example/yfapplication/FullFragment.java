package com.example.yfapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static java.sql.Types.NULL;

public class FullFragment extends Fragment {
    View view;
    private static final String TAG = "myLogs";
    private final int idFragment;
    private Spinner spinner;
    private final int idMode;
    private final int idSpinner;
    private final int idMeal_name;
    private final int idName;
    private final int idTime_cooking;
    private int animation;
    private String Mode_text ="Mode_text";

    public FullFragment(String Mode){
        super();
        switch (Mode){
            case "WAITING":
                idFragment = R.layout.waiting_fragment;
                idMode = NULL;
                idSpinner = R.layout.waiting_spinner_item;
                idMeal_name = NULL;
                idName = NULL;
                idTime_cooking = NULL;
                animation = R.anim.alpha_change;
                break;
            case "PUT":
                idFragment = R.layout.put_fragment;
                idMode = NULL;
                idSpinner = R.layout.put_spinner_item;
                idMeal_name = NULL;
                idName = NULL;
                idTime_cooking = NULL;
                break;
            case "READY":
                idFragment = R.layout.ready_fragment;
                idMode = R.id.ready_mode;
                idSpinner = R.layout.ready_spinner_item;
                idMeal_name = R.id.meal_name;
                idName = R.id.name;
                idTime_cooking = NULL;
                Mode_text ="Ready!\nOrder №";
                break;
            case "COOKING":
                idFragment = R.layout.cooking_fragment;
                idMode = R.id.cooking_mode;
                idSpinner = R.layout.cooking_spinner_item;
                idMeal_name = R.id.meal_name;
                idName = R.id.name;
                idTime_cooking = R.id.time_cooking;
                Mode_text ="Cooking\nOrder №";

                break;
            default:
                idFragment = R.layout.waiting_fragment;
                idMode = NULL;
                idSpinner = R.layout.waiting_spinner_item;
                idMeal_name = NULL;
                idName = NULL;
                idTime_cooking = NULL;
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(idFragment, container, false);
        Context context = view.getContext();

        fragmentsetData();

        // Адаптер строкового массива для выбора ведра
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(context, R.array.choice,idSpinner);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = view.findViewById(R.id.spinner);

        spinner.setAdapter(Adapter);
        spinner.setPrompt("Title");

        // Установка предыдущего выбора пользователя
        if (getActivity() != null){
            int bucket = Data.getInstance().getbucket();
            Log.d(TAG, "91f19 Last chose selected" + bucket);
            spinner.setSelection(bucket);
        }


        // Слушатель спинера
        // TODO Связь с определённым ведром
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "91f19 Spinner item selected " + i);
                Data.getInstance().setVariableBucket(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(TAG, "91f19 Nothing selected");
            }
        });



        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (animation != NULL){
            TextView text = (TextView) view.findViewById(R.id.waiting_mode);
            text.clearAnimation();
            Log.d(TAG, "91f19 Animation cleared");
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "91f19 fragment destroys");
        super.onDestroy();
    }

    public synchronized void fragmentsetData() {
        Log.d(TAG, "91f19 set text in fragment");

        if (idMode!=NULL) {
            TextView Mode = (TextView) view.findViewById(idMode);
            Mode.setText(Mode_text + Data.getInstance().getOrdername());
        }
        if (idMeal_name!=NULL) {
            TextView Meal_name = (TextView) view.findViewById(idMeal_name);
            Meal_name.setText(Data.getInstance().getMealname());
        }
        if (idName!=NULL) {
            TextView Name = (TextView) view.findViewById(idName);
            Name.setText(Data.getInstance().getName());
        }
        if (idTime_cooking!=NULL) {
            TextView Time_cooking = (TextView) view.findViewById(idTime_cooking);
            Timer timer = new Timer(Time_cooking, Data.getInstance().getTimecooking());
            //timer.registerCallback(new Timer.Callback() {
                //@Override
                //public void callingBack() {
                    //Log.d(TAG, "91f19 Calling back");
                    //FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    //transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                    //transaction.replace(R.id.fragment_container, new FullFragment("READY")).commit();
                //}
            //});
            timer.startTimer();
        }
        if (animation != NULL){
            TextView text = (TextView) view.findViewById(R.id.waiting_mode);
            Log.d(TAG, "91f19 starting animation");
            Animation anim = AnimationUtils.loadAnimation(view.getContext(), animation);
            text.startAnimation(anim);
        }
    }

}