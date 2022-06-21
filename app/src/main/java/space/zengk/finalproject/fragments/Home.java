package space.zengk.finalproject.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import space.zengk.finalproject.objects.User;
import space.zengk.finalproject.R;

public class Home extends Fragment {

    private static final String ARG_USER = "user";
    private static final String ARG_REWARD = "reward";

    private Button btnStartStudying, btnRewardPet;
    private TextView textViewUsername, textViewPetName, textViewStreak, textViewPoints;
    private ImageView imageViewMoodEmoji, imageViewPet;
    private ProgressBar progressBarMood;
    private IFromHomeFragment mListener;
    private User user;
    private String reward;


    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(User user, String reward) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putString(ARG_REWARD, reward);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
            reward = getArguments().getString(ARG_REWARD);
            Log.d("demo", "onCreate: " + user.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        btnStartStudying = rootView.findViewById(R.id.btn_home_startStudying);
        btnRewardPet = rootView.findViewById(R.id.btnHomeRewardPet);
        textViewUsername = rootView.findViewById(R.id.username_home);
        textViewPetName = rootView.findViewById(R.id.petName_home);
        textViewStreak = rootView.findViewById(R.id.streak_home);
        textViewPoints = rootView.findViewById(R.id.points_home);
        progressBarMood = rootView.findViewById(R.id.progressBar_home_mood);
        imageViewMoodEmoji = rootView.findViewById(R.id.emoji_home);
        imageViewPet = rootView.findViewById(R.id.petImg_home);

        btnStartStudying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToSetTimer();
            }
        });

        btnRewardPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToRewardPet();
            }
        });

        textViewUsername.setText(user.getUsername());
        textViewPetName.setText(user.getPetName());
        textViewStreak.setText(String.valueOf(user.getStreak()));
        textViewPoints.setText(String.valueOf(user.getPoints()));

        if (reward == null) {
            // Set pet image
            switch(user.getPetType()) {
                case "beagle":
                    imageViewPet.setImageResource(R.drawable.beagle_home);
                    break;
                case "corgi":
                    imageViewPet.setImageResource(R.drawable.corgi_ribbon);
                    break;
                case "germanShepherd":
                    imageViewPet.setImageResource(R.drawable.germanshepherd_home);
                    break;
                case "husky":
                    imageViewPet.setImageResource(R.drawable.husky_home);
                    break;
                case "pomeranian":
                    imageViewPet.setImageResource(R.drawable.pomeranian_home);
                    break;
                case "pug":
                    imageViewPet.setImageResource(R.drawable.pug_waving);
                    break;
                default:
                    break;
            }
        }
        else {
            setRewardPetImage();
        }

        // Set mood image and progress mood bar
        if (user.getStreak() >= 12) {
            progressBarMood.setProgress(100);
            imageViewMoodEmoji.setImageResource(R.drawable.awesome);
        } else if (user.getStreak() >= 4) {
            progressBarMood.setProgress(66);
            imageViewMoodEmoji.setImageResource(R.drawable.happy);
        } else if (user.getStreak() >= 2) {
            progressBarMood.setProgress(33);
            imageViewMoodEmoji.setImageResource(R.drawable.sad);
        } else {
            progressBarMood.setProgress(0);
            imageViewMoodEmoji.setImageResource(R.drawable.angry);
        }
        
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFromHomeFragment){
            this.mListener = (IFromHomeFragment) context;
        }
        else{
            throw new RuntimeException(context.toString() + "must implement IFromHomeFragment");
        }
    }

    // displays image of pet after they were rewarded 
    
    public void setRewardPetImage() {
        switch (user.getPetType()) {
            case "beagle":
                setBeagleRewardImg(reward);
                break;
            case "corgi":
                setCorgiRewardImg(reward);
                break;
            case "germanShepherd":
                setGShepherdRewardImg(reward);
                break;
            case "husky":
                setHuskyRewardImg(reward);
                break;
            case "pomeranian":
                setPomRewardImg(reward);
                break;
            case "pug":
                setPugRewardImg(reward);
                break;
            default:
                break;
        }
    }

    private void setBeagleRewardImg(String reward) {
        switch(reward) {
            case "Feed":
                imageViewPet.setImageResource(R.drawable.beagle_talking);
                toastMsg("Thank you for feeding your beagle");
                break;
            case "Play":
                imageViewPet.setImageResource(R.drawable.beagle_ball);
                toastMsg("Thank you for playing with your beagle");
                break;
            case "Walk":
                imageViewPet.setImageResource(R.drawable.beagle_playful);
                toastMsg("Thank you for walking your beagle");
                break;
            default:
                break;
        }
    }

    private void setCorgiRewardImg(String reward) {
        switch(reward) {
            case "Feed":
                imageViewPet.setImageResource(R.drawable.corgi_bone);
                toastMsg("Thank you for feeding your corgi");
                break;
            case "Play":
                imageViewPet.setImageResource(R.drawable.corgi_playful);
                toastMsg("Thank you for playing with your corgi");
                break;
            case "Walk":
                imageViewPet.setImageResource(R.drawable.corgi_walking);
                toastMsg("Thank you for walking your corgi");
                break;
            default:
                break;
        }
    }

    private void setGShepherdRewardImg(String reward) {
        switch(reward) {
            case "Feed":
                imageViewPet.setImageResource(R.drawable.german_feed);
                toastMsg("Thank you for feeding your german shepherd");
                break;
            case "Play":
                imageViewPet.setImageResource(R.drawable.germanshepherd_sunglasses);
                toastMsg("Thank you for playing with your german shepherd");
                break;
            case "Walk":
                imageViewPet.setImageResource(R.drawable.german_walk);
                toastMsg("Thank you for walking your german shepherd");
                break;
            default:
                break;
        }
    }

    private void setHuskyRewardImg(String reward) {
        switch(reward) {
            case "Feed":
                imageViewPet.setImageResource(R.drawable.husky_feed);
                toastMsg("Thank you for feeding your husky");
                break;
            case "Play":
                imageViewPet.setImageResource(R.drawable.husky_hi);
                toastMsg("Thank you for playing with your husky");
                break;
            case "Walk":
                imageViewPet.setImageResource(R.drawable.husky_happy);
                toastMsg("Thank you for walking your husky");
                break;
            default:
                break;
        }
    }

    private void setPomRewardImg(String reward) {
        switch(reward) {
            case "Feed":
                imageViewPet.setImageResource(R.drawable.pomeranian_feed);
                toastMsg("Thank you for feeding your pomeranian");
                break;
            case "Play":
                imageViewPet.setImageResource(R.drawable.pomeranian_playful);
                toastMsg("Thank you for playing with your pomeranian");
                break;
            case "Walk":
                imageViewPet.setImageResource(R.drawable.pomeranian_happy);
                toastMsg("Thank you for walking your pomeranian");
                break;
            default:
                break;
        }
    }

    private void setPugRewardImg(String reward) {
        switch(reward) {
            case "Feed":
                imageViewPet.setImageResource(R.drawable.pug_donut);
                toastMsg("Thank you for feeding your pug");
                break;
            case "Play":
                imageViewPet.setImageResource(R.drawable.pug_sunglasses);
                toastMsg("Thank you for playing with your pug");
                break;
            case "Walk":
                imageViewPet.setImageResource(R.drawable.pug_walking);
                toastMsg("Thank you for walking your pug");
                break;
            default:
                break;
        }
    }

    private void toastMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public interface IFromHomeFragment {
        void goToSetTimer();
        void goToRewardPet();
    }
}