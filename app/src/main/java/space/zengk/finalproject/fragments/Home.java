package space.zengk.finalproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import space.zengk.finalproject.R;
import space.zengk.finalproject.objects.User;

/*
 * Katherine Zeng, Rachel Li, Winston Chen
 * Final Project
 */
public class Home extends Fragment implements View.OnClickListener {

    private static final String ARG_USER = "user";
    private static final String ARG_REWARD = "reward";

    private Button btnStartStudying, btnRewardPet, btnEdit, btnLogout;
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
        btnLogout = rootView.findViewById(R.id.logoutPetBtn);
        btnEdit = rootView.findViewById(R.id.editHomeBtn);
        textViewUsername = rootView.findViewById(R.id.username_home);
        textViewPetName = rootView.findViewById(R.id.petName_home);
        textViewStreak = rootView.findViewById(R.id.streak_home);
        textViewPoints = rootView.findViewById(R.id.points_home);
        progressBarMood = rootView.findViewById(R.id.progressBar_home_mood);
        imageViewMoodEmoji = rootView.findViewById(R.id.emoji_home);
        imageViewPet = rootView.findViewById(R.id.petImg_home);

        textViewUsername.setText(user.getUsername());
        textViewPetName.setText(user.getPetName());
        textViewStreak.setText(String.valueOf(user.getStreak()));
        textViewPoints.setText(String.valueOf(user.getPoints()));

        btnStartStudying.setOnClickListener(this);
        btnRewardPet.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

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
            imageViewMoodEmoji.setImageResource(R.drawable.pleading);
        }
        
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_home_startStudying:
                mListener.goToSetTimer();
                break;
            case R.id.btnHomeRewardPet:
                mListener.goToRewardPet();
                break;
            case R.id.editHomeBtn:
                mListener.goToEditHome();
                break;
            case R.id.logoutPetBtn:
                mListener.logout();
                break;
            default:
                break;
        }
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
                setRewardPetImageHelper(reward, "beagle", R.drawable.beagle_talking, R.drawable.beagle_ball, R.drawable.beagle_playful);
                break;
            case "corgi":
                setRewardPetImageHelper(reward, "corgi", R.drawable.corgi_bone, R.drawable.corgi_playful, R.drawable.corgi_walking);
                break;
            case "germanShepherd":
                setRewardPetImageHelper(reward, "german shepherd", R.drawable.german_feed, R.drawable.germanshepherd_sunglasses, R.drawable.german_walk);
                break;
            case "husky":
                setRewardPetImageHelper(reward, "husky", R.drawable.husky_feed, R.drawable.husky_hi, R.drawable.husky_happy);
                break;
            case "pomeranian":
                setRewardPetImageHelper(reward, "pomeranian", R.drawable.pomeranian_feed, R.drawable.pomeranian_playful, R.drawable.pomeranian_happy);
                break;
            case "pug":
                setRewardPetImageHelper(reward, "pug", R.drawable.pug_donut, R.drawable.pug_sunglasses, R.drawable.pug_walking);
                break;
            default:
                break;
        }
    }

    private void setRewardPetImageHelper(String reward, String petType, int feedingPic, int playingPic, int walkingPic) {
        switch(reward) {
            case "Feed":
                imageViewPet.setImageResource(feedingPic);
                toastMsg("Thank you for feeding your " + petType);
                break;
            case "Play":
                imageViewPet.setImageResource(playingPic);
                toastMsg("Thank you for playing with your " + petType);
                break;
            case "Walk":
                imageViewPet.setImageResource(walkingPic);
                toastMsg("Thank you for walking your " + petType);
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
        void goToEditHome();
        void logout();
    }
}