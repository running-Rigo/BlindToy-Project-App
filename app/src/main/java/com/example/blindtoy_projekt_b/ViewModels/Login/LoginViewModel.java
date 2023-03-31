package com.example.blindtoy_projekt_b.ViewModels.Login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;

public class LoginViewModel extends AndroidViewModel {
    private GeneralUserRepository userRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = GeneralUserRepository.getInstance(application);
    }

    public void checkLogin(String email, String password) {
        userRepository.checkCredentialsInDb(email, password);
    }
}

