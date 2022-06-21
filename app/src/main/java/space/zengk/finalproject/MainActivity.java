package space.zengk.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import space.zengk.finalproject.fragments.ChoosePet;
import space.zengk.finalproject.fragments.CreateUserProfile;
import space.zengk.finalproject.fragments.Failure;
import space.zengk.finalproject.fragments.RewardOptions;
import space.zengk.finalproject.fragments.SetTimer;
import space.zengk.finalproject.fragments.Studying;
import space.zengk.finalproject.fragments.Home;
import space.zengk.finalproject.fragments.Login;
import space.zengk.finalproject.fragments.Success;
import space.zengk.finalproject.fragments.Welcome;
import space.zengk.finalproject.model.RewardAdapter;
import space.zengk.finalproject.objects.User;

public class MainActivity extends AppCompatActivity implements Login.IFromLogin, Welcome.IFromWelcome,
        CreateUserProfile.IFromCreateUser, ChoosePet.IfromChoosePet, Home.IFromHomeFragment,
        RewardOptions.IFromRewardOptions, SetTimer.IFromSetTimerFragment, Success.IFromSuccess, RewardAdapter.IFromRewardAdapter,
        Failure.IFromFailure, Studying.IFromStudying {

    private User user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    private String usernameFromRegister, petNameFromRegister, emailFromRegister, passwordFromRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initializing Firebase Authentication...
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check internet access to start program
        if (isNetworkAvailable()) {
            firebaseUser = mAuth.getCurrentUser();
            // start on welcome page
            goToWelcomePage();
        } else {
            toastMsg("Not connected to internet! Unable to proceed");
        }
    }

    private void toastMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setUser(User user) {
        this.user = user;
    }

    // retrieves logged-in user from database and stores info as User object
    private void getUserFromDB() {

        db.collection("users")
                .document(firebaseUser.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String username = document.getString("username");
                                String petName = document.getString("petName");
                                String email = document.getString("email");
                                String petType = document.getString("petType");
                                int points = Math.toIntExact((Long) document.get("points"));
                                int streak = Math.toIntExact((Long) document.get("streak"));
                                User getUser = new User(petName, username, email, petType, points, streak);
                                setUser(getUser);
                            } else {
                                Log.d("demo", "get failed with ", task.getException());
                            }
                        } else {
                            Log.d("demo", "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void goToWelcomePage() {
        setTitle("Welcome");
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragmentLayout, new Welcome(), "welcomeFragment")
            .addToBackStack(null)
            .commit();
    }

    @Override
    public void goToLoginPage() {
        setTitle("Login");
        firebaseUser = mAuth.getCurrentUser();
        getUserFromDB();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, new Login())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToRegisterPage() {
        setTitle("Register an Account");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout,  new CreateUserProfile(), "createUserFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToHome(String reward) {
        setTitle("Home");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, Home.newInstance(user, reward), "homeFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void registerDone() {
        goToLoginPage();
    }

    @Override
    public void choosePetAvatar(String username, String petName, String emailText, String password) {
        setTitle("Choose a Pet");
        this.usernameFromRegister = username;
        this.petNameFromRegister = petName;
        this.emailFromRegister = emailText;
        this.passwordFromRegister = password;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, new ChoosePet(), "choosePetFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void petAvatarChosen(String pet) {
        setTitle("Register an Account");

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragmentLayout, CreateUserProfile.newInstance(usernameFromRegister,
            petNameFromRegister,
            emailFromRegister,
            passwordFromRegister,
            pet), "createUserFragment")
            .addToBackStack(null)
            .commit();
    }

    @Override
    public void goToSetTimer() {
        setTitle("Set Timer for Study Session");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, new SetTimer())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToRewardPet() {
        setTitle("Reward Your Pet");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, RewardOptions.newInstance(user.getPoints()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToHomeFromRewards() {
        goToHome(null);
    }

    @Override
    public void startStudying(int hours, int minutes, int seconds) {
        setTitle("Start your Study Session!");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, Studying.newInstance(hours, minutes, seconds, user.getPetName(), user.getPetType()))
                .addToBackStack(null)
                .commit();
    }

    // add points from User based on calculation
    /* Each 30 minutes of studying you get 10 points
    If you have 1 streak -> you get 1 more bonus points added
    If you have 2 streaks -> you get 1* 2 more bonus points added */
    public int calculateRewardPoints(int minutes, int streak) {
        double points = minutes * 5 + streak;
        //updateDBScore("points", 100 + user.getPoints());
        //return ;
        return (int) points;
    }

    @Override
    public void goToSuccess(int minutes) {
        int points = this.calculateRewardPoints(minutes, user.getStreak());
        user.setPoints(points+user.getPoints());
        user.setStreak(user.getStreak()+1);
        updateDBScore("points", user.getPoints());
        updateDBScore("streak", user.getStreak());
        setTitle("Success!");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, Success.newInstance(points))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToFailure() {
        setTitle("Failure!");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, Failure.newInstance(user.getPoints()/2))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToReward(int points) {
        setTitle("Reward Options");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, RewardOptions.newInstance(points))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToHomeFromRewardAdapter(String reward, int pointsSpent) {
        user.setPoints(user.getPoints()-pointsSpent);
        // update points in firebase
        updateDBScore("points", user.getPoints()-pointsSpent);
        goToHome(reward);
//        Home homeFragment = (Home) getSupportFragmentManager()
//                .findFragmentByTag("homeFragment");
       // homeFragment.setRewardPetImage(reward);
    }

    @Override
    public void goToHomeFromFailure(int points) {
        // subtract/divide by the amount of points lost
        // note: given points is already divided by 2
        this.user.setPoints(points);
        this.user.setStreak(0);
        // update points and streak in firebase
        updateDBScore("points", points);
        updateDBScore("streak", 0);
        goToHome(null);
    }

    private void updateDBScore(String field, int value) {
        db.collection("users")
                .document(user.getEmail())
                .update(field, value)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("demo", "firebase successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("demo", "Error updating document", e);
                    }
                });
    }

}