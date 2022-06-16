package space.zengk.finalproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import space.zengk.finalproject.R;

public class ChoosePet extends Fragment implements View.OnClickListener {

    private IfromChoosePet mListener;

    private ImageView beagleChoice, corgiChoice, germanShepChoice, huskyChoice, pomChoice, pugChoice;

    public ChoosePet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.chooseBeagleImg:
                mListener.petAvatarChosen("beagle");
                break;
            case R.id.chooseCorgiImg:
                mListener.petAvatarChosen("corgi");
                break;
            case R.id.chooseGermanShepImg:
                mListener.petAvatarChosen("germanShepherd");
                break;
            case R.id.chooseHuskyImg:
                mListener.petAvatarChosen("husky");
                break;
            case R.id.choosePomImg:
                mListener.petAvatarChosen("pomeranian");
                break;
            case R.id.choosePugImg:
                mListener.petAvatarChosen("pug");
                break;
            default:
                break;
        }
    }

    public interface IfromChoosePet {
        void petAvatarChosen(String pet);   //(Uri uri);
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