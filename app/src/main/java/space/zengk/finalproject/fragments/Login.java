package space.zengk.finalproject.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import space.zengk.finalproject.R;

public class Login extends Fragment {

    private EditText emailLogin, passwordLogin;
    private Button loginBtn;
    private IFromLogin mListener;
    private FirebaseAuth mAuth;

    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        
        emailLogin = view.findViewById(R.id.emailLogin);
        passwordLogin = view.findViewById(R.id.passwordLogin);
        loginBtn = view.findViewById(R.id.loginToHomeBtn);
        
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = String.valueOf(emailLogin.getText());
                String passwordText = String.valueOf(passwordLogin.getText());
                
                if (emailText.isEmpty() || passwordText.isEmpty()) {
                    toastMsg("Email or password input is empty!");
                } else {
                    mAuth.signInWithEmailAndPassword(emailText, passwordText)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    toastMsg("Login successful!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    toastMsg("Login failed! " + e.getMessage());
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mListener.goToHome(null);
                                    }
                                }
                            });
                }
            }
        });
        return view;
    }

    private void toastMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFromLogin) {
            mListener = (IFromLogin) context;
        } else {
            throw new RuntimeException(context + " must implement IFromLogin interface");
        }
    }

    public interface IFromLogin {
        void goToHome(String reward);
    }
}