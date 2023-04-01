package com.example.blindtoy_projekt_b.ViewModels.Login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;

public class RegistrationViewModel extends AndroidViewModel {
    GeneralUserRepository userRepository;
    Application application;

    //Constructor with Application as Context
    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        userRepository = GeneralUserRepository.getInstance(application);
    }

    public void createUser(String username, String email, String password){
        userRepository.registerNewUserInDb(username,email,password);
    }

}
