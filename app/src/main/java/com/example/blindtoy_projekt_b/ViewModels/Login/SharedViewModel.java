package com.example.blindtoy_projekt_b.ViewModels.Login;

import android.app.Application;
import android.database.Observable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.blindtoy_projekt_b.Data.LocalRoomsData.User;
import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;

public class SharedViewModel extends AndroidViewModel {
    private static final String TAG = "L_SharedViewModel";
    private GeneralUserRepository userRepository;
    public LiveData<String> nextFragmentDecision;
    private MutableLiveData<String> nextUI = new MutableLiveData<>();
    public LiveData<String> repoErrorMessage;
    private Observer<String> asyncStatusObserver;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        userRepository = GeneralUserRepository.getInstance(application);
        repoErrorMessage = userRepository.repoErrorMessage;
        setNextFragmentDecision("");
        initObserver();
    }

    @Override
    protected void onCleared(){
        userRepository.asyncStatusUpdate.removeObserver(asyncStatusObserver);
    }

    private void initObserver(){
        asyncStatusObserver = new Observer<String>() {
            @Override
            public void onChanged(String repoStatus) {
                if(repoStatus.equals("registrationSuccessful")){
                    chooseLogin();
                }
                else if(repoStatus.equals("loginSuccessful")){
                    logUserIn();
                }
                else if(repoStatus.equals("noUserFound")){
                    chooseFirstFragment();
                }
                else if(repoStatus.equals("isCheckingUser")){
                    chooseLoadingFragment();
                }
            }
        };
        userRepository.asyncStatusUpdate.observeForever(asyncStatusObserver);
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

    private void chooseLoadingFragment(){
        Log.d(TAG,"repo is checking user - Method chooseLoadingFragment aufgerufen");
        setNextFragmentDecision("LoadingFragment");
    }

    private void chooseFirstFragment(){
        Log.d(TAG,"repo found no user -Method chooseFirstFragment aufgerufen");
        setNextFragmentDecision("FirstFragment");
    }

    public void chooseRegistration(){
        setNextFragmentDecision("RegistrationFragment");
    }

    private void logUserIn(){
        setNextFragmentDecision("InternalMainActivity");
    }
    //endregion

}
