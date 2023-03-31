package com.example.blindtoy_projekt_b.ViewModels.Login;

import android.app.Application;
import android.database.Observable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;

public class SharedViewModel extends AndroidViewModel {
    private static final String TAG = "SharedViewModel";
    private GeneralUserRepository userRepository;
    public LiveData<String> nextFragmentDecision;
    private MutableLiveData<String> nextUI = new MutableLiveData<>();
    public LiveData<String> repoErrorMessage;
    private Observer<Boolean> loginStatusObserver;


    public SharedViewModel(@NonNull Application application) {
        super(application);
        userRepository = GeneralUserRepository.getInstance(application);
        repoErrorMessage = userRepository.repoErrorMessage;
        setNextFragmentDecision("");
        initObserver();
    }

    private void initObserver(){
        loginStatusObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoggedIn) {
                if(isLoggedIn){
                    logUserIn();
                }
            }
        };
        userRepository.userIsLoggedIn.observeForever(loginStatusObserver);
    }

//region Methods for LiveData nextFragmentDecision

    private void setNextFragmentDecision(String nextUIChoice) {
        nextUI.setValue(nextUIChoice);
        this.nextFragmentDecision = nextUI;
        Log.d(TAG,"Method setNextFragmentDecision aufgerufen: " + nextFragmentDecision.getValue().toString());
    }

    public void chooseLogin(){
        Log.d(TAG,"Method chooseLogin aufgerufen");
        setNextFragmentDecision("LoginFragment");
    }

    public void chooseRegistration(){
        setNextFragmentDecision("RegistrationFragment");
    }

    private void logUserIn(){
        //Method for Checking login...mh.
        setNextFragmentDecision("InternalMainActivity");
    }
    //endregion

}
