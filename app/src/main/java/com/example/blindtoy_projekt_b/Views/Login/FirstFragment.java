package com.example.blindtoy_projekt_b.Views.Login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Login.SharedViewModel;

public class FirstFragment extends Fragment {
    private static final String TAG = "L_FirstFragment";
    SharedViewModel sharedViewModel;
    Button loginButton;
    Button registrationButton;
    View view;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        initViews();
        Log.d(TAG,"geöffnet!");
        return view;
    }

    private void initViews(){
        loginButton = view.findViewById(R.id.loginBtn);
        registrationButton = view.findViewById(R.id.registrationBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedViewModel.chooseLogin();
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedViewModel.chooseRegistration();
            }
        });
    }
}