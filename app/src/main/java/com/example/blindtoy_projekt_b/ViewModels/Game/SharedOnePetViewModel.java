package com.example.blindtoy_projekt_b.ViewModels.Game;

import android.app.Application;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.blindtoy_projekt_b.Data.LocalRoomsData.Pet;
import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;

import java.sql.BatchUpdateException;

public class SharedOnePetViewModel extends AndroidViewModel {
    private static final String TAG = "SharedOnePetViewModel";
    private GeneralUserRepository userRepository;
    public LiveData<Pet> chosenPet;
    private MutableLiveData<Pet> mutableChosenPet = new MutableLiveData<>();
    public LiveData<String> nextFragmentDecision;
    private MutableLiveData<String> nextUI = new MutableLiveData<>();
    public LiveData<String> repoErrorMessage;
    private Observer<String> asyncStatusObserver;

    private boolean hasSettings = false;

    public SharedOnePetViewModel(@NonNull Application application) {
        super(application);
        userRepository = GeneralUserRepository.getInstance(application);
        setChosenPet(userRepository.getOneChosenPet()); //takes at creation only once the chosenPet from userrepo
        setNextFragmentDecision("");
        repoErrorMessage = userRepository.repoErrorMessage;
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
            }
        };
        userRepository.asyncStatusUpdate.observeForever(asyncStatusObserver);
    }


    public void doLogout(){
        userRepository.logoutUser();
    }


    private void setChosenPet(Pet repoPet){
        mutableChosenPet.setValue(repoPet);
        chosenPet = mutableChosenPet;
    }

    public boolean getHasSettings() {
        return hasSettings;
    }

    public void setHasSettings(boolean hasSettings) {
        this.hasSettings = hasSettings;
    }


    public void chooseSoundSettings(){
        setNextFragmentDecision("SoundSettingsFragment");
    }

    public void chooseOnePetOverview(){
        setNextFragmentDecision("OnePetFragment");
    }

    public void choosePlay(){
        setNextFragmentDecision("PlayWithPetActivity");
    }

    public void chooseLogout(){
        setNextFragmentDecision("MainActivity");
    }

    private void setNextFragmentDecision(String nextUIChoice) {
        nextUI.setValue(nextUIChoice);
        this.nextFragmentDecision = nextUI;
        Log.d(TAG,"Method setNextFragmentDecision aufgerufen: " + nextFragmentDecision.getValue().toString());
    }



}
