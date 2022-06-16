package space.zengk.finalproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import space.zengk.finalproject.R;

public class Welcome extends Fragment implements View.OnClickListener {
    
    private Button buttonLogin, buttonRegister;
    private IFromWelcome iFromWelcome;

    public Welcome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        buttonLogin = view.findViewById(R.id.loginBtn);
        buttonRegister = view.findViewById(R.id.registerBtn);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginBtn) {
            iFromWelcome.goToLoginPage();
        }
        else if (v.getId() == R.id.registerBtn) {
            iFromWelcome.goToRegisterPage();
        }  
    }

    public interface IFromWelcome {
        void goToLoginPage();
        void goToRegisterPage();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFromWelcome) {
            iFromWelcome = (IFromWelcome) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IFromWelcome!");
        }
    }
}