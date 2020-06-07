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

public class FullFragment extends Fragment {
    View view;
    private static final String TAG = "myLogs";
    private final int idFragment;
    private final int idMessage;
    private final int idSpinner;

    public FullFragment(String Mode){
        super();
        switch (Mode){
            case "WAITING":
                idFragment = R.layout.waiting_fragment;
                idMessage = R.id.waiting_message;
                idSpinner = R.layout.waiting_spinner_item;
                break;
            case "PUT":
                idFragment = R.layout.put_fragment;
                idMessage = R.id.put_message;
                idSpinner = R.layout.put_spinner_item;
                break;
            case "READY":
                idFragment = R.layout.ready_fragment;
                idMessage = R.id.ready_message;
                idSpinner = R.layout.ready_spinner_item;
                break;
            case "COOKING":
                idFragment = R.layout.cooking_fragment;
                idMessage = R.id.cooking_message;
                idSpinner = R.layout.cooking_spinner_item;
                break;
            default:
                idFragment = R.layout.waiting_fragment;
                idMessage = R.id.waiting_message;
                idSpinner = R.layout.waiting_spinner_item;
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(idFragment, container, false);
        Context context = view.getContext();
        TextView Mes = (TextView) view.findViewById(idMessage);



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
        TextView Mes = (TextView) view.findViewById(idMessage);

        Mes.setText(Data.getInstance().getName());
        //getFragmentManager().beginTransaction().detach(this).commit();
        //getFragmentManager().beginTransaction().attach(this).commit();
    }

}