package com.example.blindtoy_projekt_b.ViewModels.Internal;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;

public class InternalSharedViewModel extends AndroidViewModel {
    private static final String TAG = "SharedViewModel";
    private GeneralUserRepository userRepository;
    public LiveData<String> nextFragmentDecision;
    private MutableLiveData<String> nextUI = new MutableLiveData<>();
    public LiveData<String> repoErrorMessage;
    private Observer<String> asyncStatusObserver;

    public InternalSharedViewModel(@NonNull Application application) {
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
                if(repoStatus.equals("logoutSuccessful")){
                    chooseLogout();
                }
                else if(repoStatus.equals("newPetSuccessful")){
                    choosePetsOverview();
                }
            }
        };
        userRepository.asyncStatusUpdate.observeForever(asyncStatusObserver);
    }

    public void doLogout(){
        userRepository.logoutUser();
    }

    public void chooseAddPet(){
        setNextFragmentDecision("AddPetFragment");
    }


    private void setNextFragmentDecision(String nextUIChoice) {
        nextUI.setValue(nextUIChoice);
        this.nextFragmentDecision = nextUI;
        Log.d(TAG,"Method setNextFragmentDecision aufgerufen: " + nextFragmentDecision.getValue().toString());
    }

    private void chooseLogout(){
        setNextFragmentDecision("MainActivity");
    }

    public void choosePetsOverview(){
        setNextFragmentDecision("PetsListFragment");
    }
}
