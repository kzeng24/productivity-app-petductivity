package space.zengk.finalproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import space.zengk.finalproject.R;

/*
 * Katherine Zeng, Rachel Li, Winston Chen
 * Final Project
 */
public class SetTimer extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button buttonSetTimerDone;
    private EditText editTextHours, editTextMinutes, editTextSeconds;
    private IFromSetTimerFragment mListener;

    public SetTimer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_timer, container, false);

        buttonSetTimerDone = view.findViewById(R.id.btn_setTimer_done);
        editTextHours = view.findViewById(R.id.enterHour_setTimer);
        editTextMinutes = view.findViewById(R.id.enterMinutes_setTimer);
        editTextSeconds = view.findViewById(R.id.enterSeconds_setTimer);

        buttonSetTimerDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextHours.getText().toString().equals("")) {
                    toastMsg("Must enter number of hours.");
                }
                else if (editTextMinutes.getText().toString().equals("")) {
                    toastMsg("Must enter number of minutes.");
                }
                else if (editTextSeconds.getText().toString().equals("")) {
                    toastMsg("Must enter number of seconds.");
                }
                else {
                    int hours = Integer.parseInt(editTextHours.getText().toString());
                    int minutes = Integer.parseInt(editTextMinutes.getText().toString());
                    int seconds = Integer.parseInt(editTextSeconds.getText().toString());
                    mListener.startStudying(hours, minutes, seconds);
                }

            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFromSetTimerFragment){
            this.mListener = (IFromSetTimerFragment) context;
        }
        else{
            throw new RuntimeException(context.toString() + "must implement IFromSetTimerFragment");
        }
    }

    private void toastMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public interface IFromSetTimerFragment {
        void startStudying(int hours, int minutes, int seconds);
    }
}