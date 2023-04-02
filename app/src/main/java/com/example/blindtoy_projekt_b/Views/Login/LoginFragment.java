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

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Login.LoginViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Login.SharedViewModel;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    View view;
    public SharedViewModel sharedViewModel;
    public LoginViewModel loginViewModel;

    private EditText useremail,password;
    private Button loginBtn;
    private ProgressBar spinner;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // The ViewModel is scoped to `this` Fragment
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        initViews();
        initObserver();
        Log.d(TAG,"geöffnet!");
        return view;
    }

    private void initViews(){
        useremail = view.findViewById(R.id.useremail);
        password = view.findViewById(R.id.password);
        loginBtn = view.findViewById(R.id.login);
        spinner = view.findViewById(R.id.loginProgressBar);
        spinner.setVisibility(View.GONE);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredEmail = useremail.getText().toString();
                String enteredPassword = password.getText().toString();
                loginViewModel.checkLogin(enteredEmail,enteredPassword);
                //??? sharedViewModel.checkIfLoggedIn();
            }
        });
    }

    private void initObserver(){
        loginViewModel.inProgress.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading){
                    spinner.setVisibility(View.VISIBLE);
                    loginBtn.setEnabled(false);
                }
                else{
                    spinner.setVisibility(View.GONE);
                    loginBtn.setEnabled(true);
                }
            }
        });
    }
}