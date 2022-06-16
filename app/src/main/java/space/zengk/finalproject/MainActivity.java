package space.zengk.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import space.zengk.finalproject.fragments.ChoosePet;
import space.zengk.finalproject.fragments.CreateUserProfile;
import space.zengk.finalproject.fragments.Home;
import space.zengk.finalproject.fragments.Login;
import space.zengk.finalproject.fragments.Welcome;

public class MainActivity extends AppCompatActivity implements Login.IFromLogin, Welcome.IFromWelcome,
        CreateUserProfile.IFromCreateUser, ChoosePet.IfromChoosePet  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToWelcomePage();
    }

    public void goToWelcomePage() {
            // start on welcome page
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
                .replace(R.id.fragmentLayout,  CreateUserProfile.newInstance(), "createUserFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToHome() {
        setTitle("Home");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, new Home())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void registerDone() {
        goToLoginPage();
    }

    @Override
    public void choosePetAvatar() {
        setTitle("Choose a Pet");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, new ChoosePet())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void petAvatarChosen(String pet) {
        CreateUserProfile createUserFragment = (CreateUserProfile) getSupportFragmentManager()
                .findFragmentByTag("createUserFragment");
        createUserFragment.setPet(pet);
        goToRegisterPage();
    }
}