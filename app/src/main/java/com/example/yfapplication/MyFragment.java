package com.example.yfapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yfapplication.R;


public class MyFragment extends Fragment {

    View view;
    private static final String TAG = "myLogs";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mfragment, container, false);
        Context context = view.getContext();
        TextView Mes = (TextView) view.findViewById(R.id.message);


        // Адаптер строкового массива для выбора ведра
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(context, R.array.choice, android.R.layout.simple_spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = view.findViewById(R.id.spinner);

        spinner.setAdapter(Adapter);
        spinner.setPrompt("Title");

        // Установка предыдущего выбора пользователя
        if (getActivity() != null){
            MainActivity ma = (MainActivity) getActivity();
            spinner.setSelection(ma.loadText());
        }


        // Слушатель спинера
        // TODO Связь с определённым ведром
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (getActivity() != null) {
                    MainActivity ma = (MainActivity) getActivity();
                    ma.saveText(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ;


        return view;
    }

    public synchronized void fragmentsetText(String item) {
        Log.d(TAG, "91f19 set text in fragment");
        TextView Mes = (TextView) view.findViewById(R.id.message);
        Mes.setText(item);
        //getFragmentManager().beginTransaction().detach(this).commit();
        //getFragmentManager().beginTransaction().attach(this).commit();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "91f19 onDetach");
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "91f19 onAttach");
        super.onAttach(context);
    }
}