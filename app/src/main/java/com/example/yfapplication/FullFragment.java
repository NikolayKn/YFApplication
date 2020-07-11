package com.example.yfapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static java.sql.Types.NULL;

public class FullFragment extends Fragment {
    private View view;
    private static final String TAG = "myLogs";
    private final int idFragment;
    private final String Mode;
    private final int orderID;
    private final int idMeal_name;
    private final int idName;
    private final int idTime_cooking;
    private int animation;
    private View.OnLongClickListener listener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(view.getContext(), "Easter egg!", Toast.LENGTH_LONG).show();
            return true;
        }
    };


    FullFragment(String Mode) {
        super();
        this.Mode = Mode;
        switch (Mode) {
            case "WAITING":
                idFragment = R.layout.waiting_mode;
                orderID = NULL;
                idMeal_name = NULL;
                idName = NULL;
                idTime_cooking = NULL;
                animation = R.anim.alpha_change;
                break;
            case "PUT":
                idFragment = R.layout.put_mode;
                orderID = NULL;
                idMeal_name = NULL;
                idName = NULL;
                idTime_cooking = NULL;
                break;
            case "READY":
                idFragment = R.layout.ready_mode;
                orderID = R.id.orderID;
                idMeal_name = NULL;
                idName = NULL;
                idTime_cooking = NULL;
                break;
            case "COOKING":
                idFragment = R.layout.cooking_mode;
                orderID = R.id.orderID;
                idMeal_name = NULL;
                idName = NULL;
                idTime_cooking = R.id.cooking_time;

                break;
            default:
                idFragment = R.layout.waiting_mode;
                orderID = NULL;
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

        fragmentSetData();

        if (Mode.equals("WAITING")) {
            TextView waiting = view.findViewById(R.id.waiting_heading);
            waiting.setOnLongClickListener(listener);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (animation != NULL) {
            TextView text = view.findViewById(R.id.waiting_heading);
            Log.d(TAG, "91f19 starting animation");
            Animation anim = AnimationUtils.loadAnimation(view.getContext(), animation);
            text.startAnimation(anim);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (animation != NULL) {
            TextView text = view.findViewById(R.id.waiting_heading);
            text.clearAnimation();
            Log.d(TAG, "91f19 Animation cleared");
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "91f19 fragment destroys");
        super.onDestroy();
    }

    private synchronized void fragmentSetData() {
        Log.d(TAG, "91f19 set text in fragment");

        if (orderID != NULL) {
            TextView order = view.findViewById(orderID);
            order.setText(Data.getInstance().getOrderName());
        }
        if (idMeal_name != NULL) {
            TextView Meal_name = view.findViewById(idMeal_name);
            Meal_name.setText(Data.getInstance().getMealName());
        }
        if (idName != NULL) {
            TextView Name = view.findViewById(idName);
            Name.setText(Data.getInstance().getName());
        }
        if (idTime_cooking != NULL) {
            TextView Time_cooking = view.findViewById(idTime_cooking);
            Timer timer = new Timer(Time_cooking, Data.getInstance().getTimeCooking());
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
    }


}