package space.zengk.finalproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import space.zengk.finalproject.R;
import space.zengk.finalproject.objects.Reward;

/*
 * Katherine Zeng, Rachel Li, Winston Chen
 * Final Project
 */
public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.ViewHolder> {

    private ArrayList<Reward> rewardList;
    private int totalPoints;
    private IFromRewardAdapter mListener;

    public RewardAdapter(ArrayList<Reward> rewardList, int totalPoints, Context context) {
        this.rewardList = rewardList;
        this.totalPoints = totalPoints;
        if (context instanceof IFromRewardAdapter) {
            mListener = (IFromRewardAdapter) context;
        } else {
            throw new RuntimeException("must implement IFromRewardAdapter");
        }
    }

    public ArrayList<Reward> getRewardList() {
        return this.rewardList;
    }

    public void setRewardList(ArrayList<Reward> rewardList) {
        this.rewardList.clear();
        this.rewardList = rewardList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final Button rewardOptionBtn;
        private final TextView pointsView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initialize view
            rewardOptionBtn = itemView.findViewById(R.id.rewardOptionBtn);
            pointsView = itemView.findViewById(R.id.points_reward);
        }

        public Button getRewardOptionBtn() {
            return rewardOptionBtn;
        }

        public TextView getPointsView() {
            return pointsView;
        }
    }

    @NonNull
    @Override
    public RewardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRecyclerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.reward_row,parent, false);
        return new ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardAdapter.ViewHolder holder, int position) {
        Reward reward = rewardList.get(position);
        holder.getRewardOptionBtn().setText(reward.getRewardOption());
        holder.getPointsView().setText(String.valueOf(reward.getPoints()));

        if (totalPoints < reward.getPoints()) {
            holder.getRewardOptionBtn().setEnabled(false);
        } else {
            holder.getRewardOptionBtn().setEnabled(true);
        }

        holder.getRewardOptionBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reward selectedReward = rewardList.get(holder.getAdapterPosition());
                mListener.goToHomeFromRewardAdapter(selectedReward.getRewardOption(), selectedReward.getPoints());
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.rewardList.size();
    }

    public interface IFromRewardAdapter {
        void goToHomeFromRewardAdapter(String reward, int pointsSpent);
    }
}
