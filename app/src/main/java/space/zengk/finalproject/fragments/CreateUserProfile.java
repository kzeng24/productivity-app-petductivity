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

import space.zengk.finalproject.R;
import space.zengk.finalproject.objects.User;

public class CreateUserProfile extends Fragment {

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

    public CreateUserProfile() {
    }

    public static CreateUserProfile newInstance() {
        CreateUserProfile fragment = new CreateUserProfile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        petAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.choosePetAvatar();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = CreateUserProfile.this.email.getText().toString();
                String password = CreateUserProfile.this.password.getText().toString();
                String petName = petNameInput.getText().toString();
                String username = CreateUserProfile.this.username.getText().toString();

                if (email.isEmpty()) {
                    CreateUserProfile.this.email.setError("Must input email!");
                }
                if (petName.isEmpty()) {
                    petNameInput.setError("Must input pet name!");
                }
                if (password.isEmpty()) {
                    CreateUserProfile.this.password.setError("Must input password!");
                }
                if (username.isEmpty()) {
                    CreateUserProfile.this.username.setError("Must input username!");
                }
                if (!email.isEmpty() && !petName.isEmpty() && !password.isEmpty() && !username.isEmpty()) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        toastMsg("Created User successfully!");
                                        currentUser = task.getResult().getUser();

                                        // Adding display name to the FirebaseUser...
                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
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
                                        addUserToDatabase(petName, username, email, chosenPet);
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
        User user = new User(petName, username, email, chosenPet, 0);
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
    public void setPet(String pet) {
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


    public interface IFromCreateUser {
        void registerDone();
        void choosePetAvatar();
    }

    private void toastMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}