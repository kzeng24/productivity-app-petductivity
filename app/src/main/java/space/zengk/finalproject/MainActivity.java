package space.zengk.finalproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

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
import space.zengk.finalproject.fragments.EditHome;
import space.zengk.finalproject.fragments.Failure;
import space.zengk.finalproject.fragments.Home;
import space.zengk.finalproject.fragments.Login;
import space.zengk.finalproject.fragments.RewardOptions;
import space.zengk.finalproject.fragments.SetTimer;
import space.zengk.finalproject.fragments.Studying;
import space.zengk.finalproject.fragments.Success;
import space.zengk.finalproject.fragments.Welcome;
import space.zengk.finalproject.model.RewardAdapter;
import space.zengk.finalproject.objects.User;

/*
 * Katherine Zeng, Rachel Li, Winston Chen
 * Final Project
 */
public class MainActivity extends AppCompatActivity implements Login.IFromLogin, Welcome.IFromWelcome,
        CreateUserProfile.IFromCreateUser, ChoosePet.IfromChoosePet, Home.IFromHomeFragment,
        RewardOptions.IFromRewardOptions, SetTimer.IFromSetTimerFragment, Success.IFromSuccess, RewardAdapter.IFromRewardAdapter,
        Failure.IFromFailure, Studying.IFromStudying, EditHome.IFromEditHome {

    private User user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    private String usernameFromRegister, petNameFromRegister, emailFromRegister, passwordFromRegister, usernameFromEditHome, petNameFromEditHome;

    private static final String CHANNEL_ID = "0x00";
    private NotificationManager notificationManager;
    private int notificationCount;

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
        createNotificationChannel();
        notificationCount = 0;
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
        firebaseUser = mAuth.getCurrentUser();
        Log.d("demo", "getUserFromDB: " + firebaseUser.getEmail());
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

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragmentLayout, Home.newInstance(getUser, reward), "homeFragment")
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                Log.d("demo", "get failed with ", task.getException());
                            }
                        } else {
                            Log.d("demo", "get failed with ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void registerDone() {
        goToLoginPage();
    }

    private void goToChoosePetFragment(boolean alreadyRegistered) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, ChoosePet.newInstance(alreadyRegistered), "choosePetFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void choosePetAvatar(String username, String petName, String emailText, String password) {
        setTitle("Choose a Pet");
        this.usernameFromRegister = username;
        this.petNameFromRegister = petName;
        this.emailFromRegister = emailText;
        this.passwordFromRegister = password;
        goToChoosePetFragment(false);
    }

    @Override
    public void petAvatarChosen(String pet, boolean alreadyRegistered) {
        setTitle("Register an Account");

        // If choosing a new pet from edit home fragment
        if (alreadyRegistered) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout, EditHome.newInstance(usernameFromEditHome,
                            petNameFromEditHome,
                            pet), "createUserFragment")
                    .addToBackStack(null)
                    .commit();
        }
        // If choosing first pet from create user profile
        else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout, CreateUserProfile.newInstance(
                            usernameFromRegister,
                            petNameFromRegister,
                            emailFromRegister,
                            passwordFromRegister, pet), "createUserFragment")
                    .addToBackStack(null)
                    .commit();
        }
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
    public void goToEditHome() {
        setTitle("Edit Options");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, EditHome.newInstance(user.getUsername(), user.getPetName(), user.getPetType()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void logout() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        mAuth.signOut();
       // firebaseUser = null; PROBLEM
        goToWelcomePage();
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
        int numPoints;
        if (user.getPoints() < 0) {
                numPoints = 0;
        }
        else {
               numPoints = user.getPoints()/2;
        }
        setTitle("Failure!");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, Failure.newInstance(numPoints, user.getPetType()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToReward() {
        // user.setPoints(user.getPoints()+points);
        setTitle("Reward Options");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, RewardOptions.newInstance(user.getPoints()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToHomeFromRewardAdapter(String reward, int pointsSpent) {
        int numPointsLeft;
        if (user.getPoints()-pointsSpent < 0) {
            numPointsLeft = 0;
        }
        else {
            numPointsLeft = user.getPoints()-pointsSpent;
        }
        user.setPoints(numPointsLeft);
        // update points in firebase
        updateDBScore("points", numPointsLeft);
        goToHome(reward);
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

    private void updateDBString(String field, String value) {
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notification_channel";
            String description = "main channel";
            //int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private NotificationCompat.Builder createDisplayNotification(String title, String content, int drawableID) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(drawableID)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        return builder;
    }

    @Override
    public void sendSuccessNotification() {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = createDisplayNotification("Success!", "You finished studying :)", R.drawable.petductivity_icon)
                                                .setSound(sound).setOnlyAlertOnce(true);;

        notificationManager.notify(notificationCount++, builder.build());
    }

    @Override
    public void sendFailureNotification() {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        NotificationCompat.Builder builder = createDisplayNotification("Failure!", "You stopped studying :(", R.drawable.pomeranian_sad)
                                                .setSound(sound).setOnlyAlertOnce(true);;
        notificationManager.notify(notificationCount++, builder.build());
    }

    @Override
    public void goToChoosePetFromEditHome(String username, String petName) {
        setTitle("Choose a Pet");
        this.usernameFromEditHome = username;
        this.petNameFromEditHome = petName;
        goToChoosePetFragment(true);
    }

    @Override
    public void doneEditing(String newUsername, String newPetname, String petType) {
        if (!petType.equals(user.getPetType())) {
            toastMsg("Reset with new pet!");
           user.setPoints(0);
           user.setStreak(0);
           user.setPetType(petType);

           updateDBScore("points", 0);
           updateDBScore("streak", 0);
           updateDBString("petType", petType);
        }
        user.setUsername(newUsername);
        user.setPetName(newPetname);

        updateDBString("username", newUsername);
        updateDBString("petName", newPetname);
        goToHome(null);
    }

}