package space.zengk.finalproject.objects;

public class Reward {
    private String rewardOption;
    private int points;

    public Reward(String rewardOption, int points) {
        this.rewardOption = rewardOption;
        this.points = points;
    }

    public String getRewardOption() {
        return rewardOption;
    }

    public void setRewardOption(String rewardOption) {
        this.rewardOption = rewardOption;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "rewardOption='" + rewardOption + '\'' +
                ", points=" + points +
                '}';
    }
}
