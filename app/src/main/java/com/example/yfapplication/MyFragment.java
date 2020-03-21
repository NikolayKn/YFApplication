package com.example.yfapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

    String[] selected_bucket = {"Первое", "Второе", "Третье", "Четвёртое"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mfragment, container, false);
        Context context = view.getContext();

        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(context, R.array.choice, android.R.layout.simple_spinner_item);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = view.findViewById(R.id.spinner);

        spinner.setAdapter(Adapter);
        spinner.setPrompt("Title");

        if (getActivity() != null){
            MainActivity ma = (MainActivity) getActivity();
            spinner.setSelection(ma.loadText());
        }


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
}


