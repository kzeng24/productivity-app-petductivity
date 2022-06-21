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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Success#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Success extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POINTS = "points";

    // TODO: Rename and change types of parameters
    private int mPoints;

    private IFromSuccess iFromSuccess;

    private TextView textViewPointsRewarded;
    private Button buttonReward;

    public Success() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param points Parameter 1.
     * @return A new instance of fragment Success.
     */
    // TODO: Rename and change types and number of parameters
    public static Success newInstance(int points) {
        Success fragment = new Success();
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
        View view = inflater.inflate(R.layout.fragment_success, container, false);

        textViewPointsRewarded = view.findViewById(R.id.textViewSuccessPointsRewarded);
        buttonReward = view.findViewById(R.id.buttonReward);


        String pointsRewarded = "You earned " + mPoints + " points";

        textViewPointsRewarded.setText(pointsRewarded);
        buttonReward.setOnClickListener(this);

        return view;
}

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonReward) {
            iFromSuccess.goToReward(mPoints);
        }
    }

    public interface IFromSuccess {
        void goToReward(int points);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFromSuccess) {
            iFromSuccess = (IFromSuccess) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IFromSuccess!");
        }
    }
}