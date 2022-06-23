package space.zengk.finalproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import space.zengk.finalproject.R;

/*
 * Katherine Zeng, Rachel Li, Winston Chen
 * Final Project
 */
public class ChoosePet extends Fragment implements View.OnClickListener {
    private static final String ARG_REGISTERED = "registered";

    private IfromChoosePet mListener;
    private boolean alreadyRegistered;

    private ImageView beagleChoice, corgiChoice, germanShepChoice, huskyChoice, pomChoice, pugChoice;

    public ChoosePet() {
        // Required empty public constructor
    }

    public static ChoosePet newInstance(boolean alreadyRegistered) {
        ChoosePet fragment = new ChoosePet();
        Bundle args = new Bundle();
        args.putBoolean(ARG_REGISTERED, alreadyRegistered);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            alreadyRegistered = getArguments().getBoolean(ARG_REGISTERED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_pet, container, false);
        beagleChoice = view.findViewById(R.id.chooseBeagleImg);
        corgiChoice = view.findViewById(R.id.chooseCorgiImg);
        germanShepChoice = view.findViewById(R.id.chooseGermanShepImg);
        huskyChoice = view.findViewById(R.id.chooseHuskyImg);
        pomChoice = view.findViewById(R.id.choosePomImg);
        pugChoice = view.findViewById(R.id.choosePugImg);

        beagleChoice.setImageResource(R.drawable.beagle_profile);
        corgiChoice.setImageResource(R.drawable.corgi_profile);
        germanShepChoice.setImageResource(R.drawable.germanshepherd_profile);
        huskyChoice.setImageResource(R.drawable.husky_profile);
        pomChoice.setImageResource(R.drawable.pomeranian_profile);
        pugChoice.setImageResource(R.drawable.pug_profile);


        beagleChoice.setOnClickListener(this);
        corgiChoice.setOnClickListener(this);
        germanShepChoice.setOnClickListener(this);
        huskyChoice.setOnClickListener(this);
        pomChoice.setOnClickListener(this);
        pugChoice.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.chooseBeagleImg:
                mListener.petAvatarChosen("beagle", alreadyRegistered);
                break;
            case R.id.chooseCorgiImg:
                mListener.petAvatarChosen("corgi", alreadyRegistered);
                break;
            case R.id.chooseGermanShepImg:
                mListener.petAvatarChosen("germanShepherd", alreadyRegistered);
                break;
            case R.id.chooseHuskyImg:
                mListener.petAvatarChosen("husky", alreadyRegistered);
                break;
            case R.id.choosePomImg:
                mListener.petAvatarChosen("pomeranian", alreadyRegistered);
                break;
            case R.id.choosePugImg:
                mListener.petAvatarChosen("pug", alreadyRegistered);
                break;
            default:
                break;
        }
    }

    public interface IfromChoosePet {
        void petAvatarChosen(String pet, boolean alreadyRegistered);   
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IfromChoosePet){
            this.mListener = (IfromChoosePet) context;
        }else{
            throw new RuntimeException(context.toString()
                    + "must implement IfromChoosePet");
        }
    }
}