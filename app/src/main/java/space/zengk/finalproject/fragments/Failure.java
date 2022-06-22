package space.zengk.finalproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import space.zengk.finalproject.R;

public class Failure extends Fragment implements View.OnClickListener {

    private static final String ARG_POINTS = "points";
    private static final String ARG_PETTYPE = "petType";
    
    private int mPoints;
    private String petType;

    private ImageView imageViewFailurePet;
    private TextView textViewPunishment;
    private Button buttonBackToHome;

    private IFromFailure iFromFailure;

    public Failure() {
        // Required empty public constructor
    }
    
    public static Failure newInstance(int points, String petType) {
        Failure fragment = new Failure();
        Bundle args = new Bundle();
        args.putInt(ARG_POINTS, points);
        args.putString(ARG_PETTYPE, petType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPoints = getArguments().getInt(ARG_POINTS);
            petType = getArguments().getString(ARG_PETTYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_failure, container, false);

        this.textViewPunishment = view.findViewById(R.id.textViewFailurePetPunishment);
        this.imageViewFailurePet = view.findViewById(R.id.imageViewFailurePet);
        this.buttonBackToHome = view.findViewById(R.id.buttonFailureBackToHome);

        String failureMSG = "You lose " + this.mPoints + " points";
        this.textViewPunishment.setText(failureMSG);

        setPet();

        buttonBackToHome.setOnClickListener(this);

        iFromFailure.sendFailureNotification();

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonFailureBackToHome) {
            iFromFailure.goToHomeFromFailure(mPoints);
        }
    }

    // MainActivity will call this method when a pet is chosen
    // sets pet image and type
    private void setPet() {
        switch (this.petType) {
            case "beagle":
                imageViewFailurePet.setImageResource(R.drawable.beagle_sleeping);
                break;
            case "corgi":
                imageViewFailurePet.setImageResource(R.drawable.corgi_sleeping);
                break;
            case "germanShepherd":
                imageViewFailurePet.setImageResource(R.drawable.germanshepherd_sleeping);
                break;
            case "husky":
                imageViewFailurePet.setImageResource(R.drawable.husky_sleeping);
                break;
            case "pomeranian":
                imageViewFailurePet.setImageResource(R.drawable.pomeranian_sad);
                break;
            case "pug":
                imageViewFailurePet.setImageResource(R.drawable.pug_sleeping);
                break;
            default:
                break;
        }
    }

    public interface IFromFailure {
        void goToHomeFromFailure(int points);
        void sendFailureNotification();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFromFailure) {
            iFromFailure = (IFromFailure) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IFromFailure!");
        }
    }
}