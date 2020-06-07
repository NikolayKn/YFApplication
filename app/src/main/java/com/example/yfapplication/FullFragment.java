package com.example.yfapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static java.sql.Types.NULL;

public class FullFragment extends Fragment {
    View view;
    private static final String TAG = "myLogs";
    private final int idFragment;
    private final int idMode;
    private final int idSpinner;
    private final int idMeal_name;
    private final int idName;
    private final int idTime_cooking;
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
               // Mode_text = getResources().getString(R.string.message_ready);
                Mode_text ="Ready!\nOrder №";
                break;
            case "COOKING":
                idFragment = R.layout.cooking_fragment;
                idMode = R.id.cooking_mode;
                idSpinner = R.layout.cooking_spinner_item;
                idMeal_name = R.id.meal_name;
                idName = R.id.name;
                idTime_cooking = R.id.time_cooking;
                //Mode_text = getContext().getResources().getString(R.string.message_cooking);
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
        //TextView Mes = (TextView) view.findViewById(idMessage);
        //TextView Mode = (TextView) view.findViewById(idMode);

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
            String time = Integer.toString(Data.getInstance().getTimecooking());
            Time_cooking.setText(time);
        }




        // Адаптер строкового массива для выбора ведра
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(context, R.array.choice,idSpinner);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = view.findViewById(R.id.spinner);

        spinner.setAdapter(Adapter);
        spinner.setPrompt("Title");

        // Установка предыдущего выбора пользователя
        if (getActivity() != null){
            spinner.setSelection(Data.getInstance().getbucket());
        }


        // Слушатель спинера
        // TODO Связь с определённым ведром
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Data.getInstance().setVariableBucket(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;
    }

    public synchronized void fragmentsetData() {
        Log.d(TAG, "91f19 set text in fragment");
        //TextView Mes = (TextView) view.findViewById(idMessage);


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
            String time = Integer.toString(Data.getInstance().getTimecooking());
            Time_cooking.setText(time);
        }



        //Mes.setText(Data.getInstance().getName());
        //getFragmentManager().beginTransaction().detach(this).commit();
        //getFragmentManager().beginTransaction().attach(this).commit();
    }

}