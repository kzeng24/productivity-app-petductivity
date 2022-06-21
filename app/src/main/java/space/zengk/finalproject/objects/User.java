package space.zengk.finalproject.objects;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Objects;

/*
 * Katherine Zeng, Rachel Li, Winston Chen
 * Final Project
 */
@Keep
public class User implements Serializable {
    private String petName;
    private String username;
    private String email;
    private String petType;
    private int points;
    private int streak;

    public User(){
    }

    public User(String petName, String username, String email, String petType, int points, int streak)  {
        this.petName = petName;
        this.username = username;
        this.email = email;
        this.petType = petType;
        this.points = points;
        this.streak = streak;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return points == user.points && streak == user.streak && petName.equals(user.petName) && username.equals(user.username) && email.equals(user.email) && petType.equals(user.petType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petName, username, email, petType, points, streak);
    }

    @Override
    public String toString() {
        return "User{" +
                "petName='" + petName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", petType='" + petType + '\'' +
                ", points=" + points +
                ", streak=" + streak +
                '}';
    }
}

