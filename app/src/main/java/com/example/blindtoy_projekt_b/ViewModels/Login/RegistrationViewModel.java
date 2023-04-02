package com.example.blindtoy_projekt_b.ViewModels.Login;

import android.app.Application;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;

public class RegistrationViewModel extends AndroidViewModel {
    GeneralUserRepository userRepository;
    Application application;
    private MutableLiveData<Boolean> mutableInProgress = new MediatorLiveData<>();
    public LiveData<Boolean> inProgress;

    //Constructor with Application as Context
    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        userRepository = GeneralUserRepository.getInstance(application);
        setInProgress(false);
    }

    public void createUser(String username, String email, String password){
        setInProgress(true);
        userRepository.registerNewUserInDb(username,email,password);
        //after 1 second the progressbar disappears (in case there was an error)
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        setInProgress(false);
                    }
                },
                1000);
    }

    private void setInProgress(Boolean hasProgress){
        mutableInProgress.setValue(hasProgress);
        inProgress = mutableInProgress;
    }

}
