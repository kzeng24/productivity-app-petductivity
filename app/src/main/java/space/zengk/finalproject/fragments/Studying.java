package space.zengk.finalproject.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import java.util.Random;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Arrays;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.os.CountDownTimer;
import space.zengk.finalproject.R;
import android.widget.Toast;

public class Studying extends Fragment {

    private static final String ARG_HOURS = "hours";
    private static final String ARG_MINUTES = "minutes";
    private static final String ARG_SECONDS = "seconds";
    private static final String ARG_PETNAME = "petName";
    private static final String ARG_PETTYPE = "petType";

    private int initHours;
    private int initMinutes;
    private int initSeconds;
    private String petName, petType;
    private TextView textViewStudyingHours, textViewStudyingMinutes, textViewStudyingSeconds,
        textViewStudyingPetSpeech;
    private ImageView imageViewStudyingPet;
    private Button buttonStudyingStartTimer;
    private ProgressBar progressBarStudying;
    private Boolean isStartStudyingButton;
    private Boolean isBreakStudyingButton;
    private IFromStudying iFromStudying;

    public Studying() {
        // Required empty public constructor
    }

    public static Studying newInstance(int hours, int minutes, int seconds, String petName, String petType) {
        Studying fragment = new Studying();
        Bundle args = new Bundle();
        args.putInt(ARG_HOURS, hours);
        args.putInt(ARG_MINUTES, minutes);
        args.putInt(ARG_SECONDS, seconds);
        args.putString(ARG_PETNAME, petName);
        args.putString(ARG_PETTYPE, petType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            initHours = getArguments().getInt(ARG_HOURS);
            initMinutes = getArguments().getInt(ARG_MINUTES);
            initSeconds = getArguments().getInt(ARG_SECONDS);
            petName = getArguments().getString(ARG_PETNAME);
            petType = getArguments().getString(ARG_PETTYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_studying, container, false);

        textViewStudyingHours = view.findViewById(R.id.textViewStudyingHours);
        textViewStudyingMinutes = view.findViewById(R.id.textViewStudyingMinutes);
        textViewStudyingSeconds = view.findViewById(R.id.textViewStudyingSeconds);
        textViewStudyingPetSpeech = view.findViewById(R.id.textViewStudyingPetSpeech);
        imageViewStudyingPet = view.findViewById(R.id.imageViewStudyingPet);
        buttonStudyingStartTimer = view.findViewById(R.id.buttonStudyingStartTimer);
        progressBarStudying = view.findViewById(R.id.progressBarStudying);

        buttonStudyingStartTimer.setText("Start timer");
        isStartStudyingButton = true;
        isBreakStudyingButton = false;

        setPet();

        ArrayList<String> listOfSpeeches = new ArrayList(
            Arrays.asList("Good luck studying!","Keep up the good work!",
                "You are crushing it as always!", "You can do it!", "You're the smartest, most hard-working person ever!",
                "I can't wait to play when you're done studying!"));
        Random rand = new Random();
        String randomSpeech = listOfSpeeches.get(rand.nextInt(listOfSpeeches.size()));
        textViewStudyingPetSpeech.setText(petName+ ": \""+ randomSpeech + "\"");

        long numMilliseconds = ((initHours * 3600) + (initMinutes * 60) + initSeconds + 1)*1000;

        CountDownTimer timer = new CountDownTimer(numMilliseconds, 1000) {
             public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                int hours = (int) (secondsRemaining / 3600);
                int minutes = (int) ((secondsRemaining % 3600) / 60);
                int seconds = (int) (secondsRemaining % 60);

                textViewStudyingHours.setText(String.valueOf(hours));
                textViewStudyingMinutes.setText(String.valueOf(minutes));
                textViewStudyingSeconds.setText(String.valueOf(seconds));

                // percentage completed
                int progress = (int) (((double) (numMilliseconds - millisUntilFinished)) / (double) numMilliseconds * 100.0);
                progressBarStudying.setProgress(progress, true);
            }

             public void onFinish() {
                 toastMsg("Good job! You finished your study session!");
                 buttonStudyingStartTimer.setText("Reward Pet");
                 progressBarStudying.setProgress(100, true);
                 isBreakStudyingButton = false;
             }
        };


        buttonStudyingStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartStudyingButton) {
                    buttonStudyingStartTimer.setText("Break study session");
                    isStartStudyingButton = false;
                    isBreakStudyingButton = true;
                    timer.start();
                }
                else if (isBreakStudyingButton) {
                    timer.cancel();
                    toastMsg("Oh no! You did not finish your study session!");
                    iFromStudying.goToFailure();
                }
                else {
                    // TODO: change back to minutes
                    int minutesStudied = ((initHours * 3600) + initMinutes * 60) + initSeconds;
                    iFromStudying.goToSuccess(minutesStudied);
                }
            }
        });

        return view;
    }

    // MainActivity will call this method when a pet is chosen
    // sets pet image and type
    private void setPet() {
        switch (this.petType) {
            case "beagle":
                imageViewStudyingPet.setImageResource(R.drawable.beagle_studying);
                break;
            case "corgi":
                imageViewStudyingPet.setImageResource(R.drawable.corgi_studying);
                break;
            case "germanShepherd":
                imageViewStudyingPet.setImageResource(R.drawable.germanshepherd_studying);
                break;
            case "husky":
                imageViewStudyingPet.setImageResource(R.drawable.husky_studying);
                break;
            case "pomeranian":
                imageViewStudyingPet.setImageResource(R.drawable.pomeranian_studying);
                break;
            case "pug":
                imageViewStudyingPet.setImageResource(R.drawable.pug_studying);
                break;
            default:
                break;
        }
    }

    public interface IFromStudying {
        // calculates reward points based on minutes studied
        void goToSuccess(int minutes);
        void goToFailure();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFromStudying){
            this.iFromStudying = (IFromStudying) context;
        }
        else{
            throw new RuntimeException(context.toString() + "must implement IFromStudying");
        }
    }
    
    private void toastMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
