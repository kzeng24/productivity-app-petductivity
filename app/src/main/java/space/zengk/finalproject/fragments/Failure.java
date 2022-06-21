package space.zengk.finalproject.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import space.zengk.finalproject.R;

public class Failure extends Fragment implements View.OnClickListener {

    private static final String ARG_POINTS = "points";
    
    private int mPoints;

    private TextView textViewPunishment;
    private Button buttonBackToHome;

    private IFromFailure iFromFailure;

    public Failure() {
        // Required empty public constructor
    }
    
    public static Failure newInstance(int points) {
        Failure fragment = new Failure();
        Bundle args = new Bundle();
        args.putInt(ARG_POINTS, points);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPoints = getArguments().getInt(ARG_POINTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_failure, container, false);

        this.textViewPunishment = view.findViewById(R.id.textViewFailurePetPunishment);
        this.buttonBackToHome = view.findViewById(R.id.buttonFailureBackToHome);

        String failureMSG = "You lose " + this.mPoints + " points";
        this.textViewPunishment.setText(failureMSG);

        buttonBackToHome.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonFailureBackToHome) {
            iFromFailure.goToHomeFromFailure(mPoints);
        }
    }

    public interface IFromFailure {
        void goToHomeFromFailure(int points);
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