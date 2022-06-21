package space.zengk.finalproject.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import space.zengk.finalproject.R;
import space.zengk.finalproject.model.RewardAdapter;
import space.zengk.finalproject.objects.Reward;

public class RewardOptions extends Fragment {

    private static final String ARG_POINTS = "points";

    private int points;
    private TextView displayPoints;
    private Button homeBtn;
    private IFromRewardOptions mListener;

    private ArrayList<Reward> rewardList;
    private RecyclerView recyclerViewRewardsList;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private RewardAdapter rewardAdapter;

    public RewardOptions() {
    }

    public static RewardOptions newInstance(int points) {
        RewardOptions fragment = new RewardOptions();
        Bundle args = new Bundle();
        args.putInt(ARG_POINTS, points);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            points = getArguments().getInt(ARG_POINTS);
        }
        rewardList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward_options, container, false);

        // initialize view
        displayPoints = view.findViewById(R.id.reward_points);
        homeBtn = view.findViewById(R.id.buttonBackToHomeFromRewards);

        // setting up RecyclerView
        recyclerViewRewardsList = view.findViewById(R.id.recyclerViewRewards);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        rewardAdapter = new RewardAdapter(rewardList, points, getContext());
        recyclerViewRewardsList.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewRewardsList.setAdapter(rewardAdapter);

        // set events
        displayPoints.setText(String.valueOf(points));

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToHomeFromRewards();
            }
        });

        // list of reward options
        rewardList.add(new Reward("Feed", 10));
        rewardList.add(new Reward("Play", 20));
        rewardList.add(new Reward("Walk", 30));

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFromRewardOptions) {
            mListener = (IFromRewardOptions) context;
        } else {
            throw new RuntimeException("must implement IFromRewardOptions");
        }
    }

    public interface IFromRewardOptions {
        void goToHomeFromRewards();
    }
}