package com.example.blindtoy_projekt_b.ViewModels.Login;

import android.app.Application;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;

public class LoginViewModel extends AndroidViewModel {
    private GeneralUserRepository userRepository;
    private MutableLiveData<Boolean> mutableInProgress = new MediatorLiveData<>();
    public LiveData<Boolean> inProgress;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        userRepository = GeneralUserRepository.getInstance(application);
        setInProgress(false);
    }

    public void checkLogin(String email, String password) {
        setInProgress(true);
        userRepository.checkCredentialsInDb(email, password);
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

