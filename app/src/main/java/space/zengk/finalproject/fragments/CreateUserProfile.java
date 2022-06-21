package space.zengk.finalproject.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.regex.Pattern;

import space.zengk.finalproject.R;
import space.zengk.finalproject.objects.User;

public class CreateUserProfile extends Fragment {
    private static final String TAG = "demo" ;
    private static final String ARG_USER = "username" ;
    private static final String ARG_PETNAME = "petname" ;
    private static final String ARG_EMAIL = "email" ;
    private static final String ARG_PASS = "password" ;
    private static final String ARG_CHOSENPET = "chosenpet" ;

//    private static final String ARG_URI = "imageUri";
//
//    private Uri imageUri;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private EditText email, password, petNameInput, username;
    private ImageView petAvatar;
    private String chosenPet;
    private Button registerBtn;
    private IFromCreateUser mListener;

    private String userText;
    private String petNameText;
    private String emailText;
    private String passwordText;
    private String chosenPetText;

    public CreateUserProfile() {
    }

    public static CreateUserProfile newInstance(String userText, String petNameText, String emailText, String passwordText, String chosenPetText) {
        CreateUserProfile fragment = new CreateUserProfile();
        Bundle args = new Bundle();
        args.putString(ARG_USER, userText);
        args.putString(ARG_PETNAME, petNameText);
        args.putString(ARG_EMAIL, emailText);
        args.putString(ARG_PASS, passwordText);
        args.putString(ARG_CHOSENPET, chosenPetText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userText = getArguments().getString(ARG_USER);
            petNameText = getArguments().getString(ARG_PETNAME);
            emailText = getArguments().getString(ARG_EMAIL);
            passwordText = getArguments().getString(ARG_PASS);
            chosenPetText = getArguments().getString(ARG_CHOSENPET);
        }
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFromCreateUser){
            this.mListener = (IFromCreateUser) context;
        }
        else{
            throw new RuntimeException(context.toString()
                    + "must implement IFromCreateUser");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_user_profile, container, false);

        email = view.findViewById(R.id.emailRegister);
        password = view.findViewById(R.id.registerPassword);
        petNameInput = view.findViewById(R.id.petNameRegister);
        registerBtn = view.findViewById(R.id.bTNCreateUserProfileDone);
        username = view.findViewById(R.id.usernameRegister);
        petAvatar = view.findViewById(R.id.imgViewPetAvatar);
        chosenPet = "pug";

        if (userText != null) {
            username.setText(userText);
        }
        if (petNameText != null) {
            petNameInput.setText(petNameText);
        }
        if (emailText != null) {
            email.setText(emailText);
        }
        if (passwordText != null) {
            password.setText(passwordText);
        }
        if (chosenPetText != null) {
            chosenPet = chosenPetText;
        }

        setPet(chosenPet);


        petAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendUsernameText = username.getText().toString();
                String sendPetNameText = petNameInput.getText().toString();
                String sendEmailText = email.getText().toString();
                String sendPasswordText = password.getText().toString();

                mListener.choosePetAvatar(sendUsernameText, sendPetNameText, sendEmailText,
                        sendPasswordText);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailField = email.getText().toString();
                String passwordField = password.getText().toString();
                String petName = petNameInput.getText().toString();
                String usernameField = username.getText().toString();

                if (emailField.isEmpty()) {
                    email.setError("Must input email!");
                }
                else if (!isValidEmail(emailField)) {
                    email.setError("Must input valid email!");
                }
                else if (petName.isEmpty()) {
                    petNameInput.setError("Must input pet name!");
                }
                else if (passwordField.isEmpty()) {
                    password.setError("Must input password!");
                }
                else if (!isValidPassword(passwordField)) {
                    password.setError("Must input valid password!");
                }
                else if (usernameField.isEmpty()) {
                    username.setError("Must input username!");
                }
                else {
                    mAuth.createUserWithEmailAndPassword(emailField, passwordField)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        toastMsg("Created User successfully!");
                                        currentUser = task.getResult().getUser();

                                        // Adding display name to the FirebaseUser...
                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(usernameField)
                                            .build();

                                        currentUser.updateProfile(profileChangeRequest)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        toastMsg("Profile changed successfully!");
                                                    }
                                                }
                                            });
                                        // Add user to Firestore users collection
                                        addUserToDatabase(petName, usernameField, emailField, chosenPet);
                                    }
                                    else {
                                         Log.d("demo", "createUserWithEmail:failure", task.getException());
                                    }
                                }
                            });
                }
            }
        });

        return view;
    }

    private void addUserToDatabase(String petName, String username, String email, String chosenPet) {
        User user = new User(petName, username, email, chosenPet, 0, 0);
        ArrayList<String> userEmails = new ArrayList<>();
        db.collection("users")
            .document(email)
            .set(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    toastMsg("User added!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toastMsg("Failed to add user! Try again!");
                }
            });
        // return to login page after after is created
        mListener.registerDone();
    }

    // MainActivity will call this method when a pet is chosen
    // sets pet image and type
    private void setPet(String pet) {
        Log.d(TAG, "setPet: " + pet);
        chosenPet = pet;
        switch (chosenPet) {
            case "beagle":
                petAvatar.setImageResource(R.drawable.beagle_profile);
                break;
            case "corgi":
                petAvatar.setImageResource(R.drawable.corgi_profile);
                break;
            case "germanShepherd":
                petAvatar.setImageResource(R.drawable.germanshepherd_profile);
                break;
            case "husky":
                petAvatar.setImageResource(R.drawable.husky_profile);
                break;
            case "pomeranian":
                petAvatar.setImageResource(R.drawable.pomeranian_profile);
                break;
            case "pug":
                petAvatar.setImageResource(R.drawable.pug_profile);
                break;
            default:
                break;
        }
    }

    private boolean isValidEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    public interface IFromCreateUser {
        void registerDone();
        void choosePetAvatar(String username, String petName, String emailText, String password);
    }

    private void toastMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}