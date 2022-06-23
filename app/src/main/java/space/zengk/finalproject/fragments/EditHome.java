package space.zengk.finalproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import space.zengk.finalproject.R;

/*
 * Katherine Zeng, Rachel Li, Winston Chen
 * Final Project
 */
public class EditHome extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_PETNAME = "petname";
    private static final String ARG_PETTYPE = "pettype";

    private String username;
    private String petName;
    private String petType;
    private EditText editNewUsername;
    private EditText editNewPetname;
    private ImageView petImg;
    private Button doneBtn;

    private IFromEditHome mListener;

    public EditHome() {
        // Required empty public constructor
    }

    public static EditHome newInstance(String username, String petName, String petType) {
        EditHome fragment = new EditHome();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PETNAME, petName);
        args.putString(ARG_PETTYPE, petType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            petName = getArguments().getString(ARG_PETNAME);
            petType = getArguments().getString(ARG_PETTYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_home, container, false);

        editNewUsername = view.findViewById(R.id.editNewUsername);
        editNewPetname = view.findViewById(R.id.editNewPetname);
        petImg = view.findViewById(R.id.petImg_editHome);
        doneBtn = view.findViewById(R.id.doneEditingBtn);

        editNewUsername.setText(username);
        editNewPetname.setText(petName);

        switch(petType) {
            case "beagle":
                petImg.setImageResource(R.drawable.beagle_profile);
                break;
            case "corgi":
                petImg.setImageResource(R.drawable.corgi_profile);
                break;
            case "germanShepherd":
                petImg.setImageResource(R.drawable.germanshepherd_profile);
                break;
            case "husky":
                petImg.setImageResource(R.drawable.husky_profile);
                break;
            case "pomeranian":
                petImg.setImageResource(R.drawable.pomeranian_profile);
                break;
            case "pug":
                petImg.setImageResource(R.drawable.pug_profile);
                break;
            default:
                break;
        }

        petImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToChoosePetFromEditHome(String.valueOf(editNewUsername.getText()), String.valueOf(editNewPetname.getText()));
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = editNewUsername.getText().toString();
                String newPetname = editNewPetname.getText().toString();

                if (newUsername == "")  {
                    toastMsg("Please enter new username!");
                }
                else if (newPetname == "") {
                    toastMsg("Please enter new pet name!");
                }
                else {
                    mListener.doneEditing(newUsername, newPetname, petType);
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
        if (context instanceof IFromEditHome) {
            mListener = (IFromEditHome) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IFromEditHome");
        }
    }

    public interface IFromEditHome {
        void goToChoosePetFromEditHome(String username, String petName);
        void doneEditing(String newUsername, String newPetname, String petType);
    }
}