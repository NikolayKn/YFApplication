package com.example.yfapplication;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class noChargeFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.waiting_mode, container, false);
        TextView textView = (TextView) view.findViewById(R.id.waiting_heading);
        textView.setText(R.string.need_charge);
        return view;
    }

}
