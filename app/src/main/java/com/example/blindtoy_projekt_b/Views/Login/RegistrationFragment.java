package com.example.blindtoy_projekt_b.Views.Login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Login.RegistrationViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Login.SharedViewModel;

public class RegistrationFragment extends Fragment {
    private static final String TAG = "L_RegistrationFragment";

    View view;
    public SharedViewModel sharedViewModel;
    public RegistrationViewModel registrationViewModel;
    //LoginViewModel loginViewModel;

    EditText username, userEmail, password;
    Button registrationBtn;
    private ProgressBar spinner;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registration, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // The ViewModel is scoped to `this` Fragment
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        initViews();
        initObserver();
        Log.d(TAG, "geöffnet!");
        return view;
    }

    private void initViews() {
        username = view.findViewById(R.id.newusername);
        userEmail = view.findViewById(R.id.newuseremail);
        password = view.findViewById(R.id.newuserpassword);
        registrationBtn = view.findViewById(R.id.registernow);
        spinner = view.findViewById(R.id.registrationProgressBar);
        spinner.setVisibility(View.GONE);

        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUserName = username.getText().toString();
                String enteredEmail = userEmail.getText().toString();
                String enteredPassword = password.getText().toString();
                registrationViewModel.createUser(enteredUserName, enteredEmail, enteredPassword);
            }
        });
    }

    private void initObserver() {
        registrationViewModel.inProgress.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    spinner.setVisibility(View.VISIBLE);
                    registrationBtn.setEnabled(false);
                } else {
                    spinner.setVisibility(View.GONE);
                    registrationBtn.setEnabled(true);
                }
            }
        });
    }
}