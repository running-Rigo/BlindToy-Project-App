package com.example.blindtoy_projekt_b.ViewModels.Game;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.blindtoy_projekt_b.Entities.Pet;
import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;
import com.example.blindtoy_projekt_b.Repositories.PetsRepository;

public class SharedOnePetViewModel extends AndroidViewModel {
    private static final String TAG = "L_SharedOnePetViewModel";
    private GeneralUserRepository userRepository;
    private PetsRepository petsRepository;
    public LiveData<Pet> chosenPet;
    private MutableLiveData<Pet> mutableChosenPet = new MutableLiveData<>();
    public LiveData<String> nextFragmentDecision;
    private MutableLiveData<String> nextUI = new MutableLiveData<>();
    public LiveData<String> repoErrorMessage;
    //
    private Observer<String> userAsyncStatusObserver;
    private Observer<String> petsAsyncStatusObserver;


    private boolean hasSettings = false;

    public SharedOnePetViewModel(@NonNull Application application) {
        super(application);
        userRepository = GeneralUserRepository.getInstance(application);
        petsRepository = PetsRepository.getInstance(application);
        setChosenPet(petsRepository.getOneChosenPet()); //takes at creation only once the chosenPet from userrepo
        setNextFragmentDecision("");
        repoErrorMessage = petsRepository.repoErrorMessage;
        initObserver();
    }

    @Override
    protected void onCleared(){
        userRepository.asyncStatusUpdate.removeObserver(userAsyncStatusObserver);
        petsRepository.asyncStatusUpdate.removeObserver(petsAsyncStatusObserver);
    }

    private void initObserver(){
        userAsyncStatusObserver = new Observer<String>() {
            @Override
            public void onChanged(String userRepoStatus) {
                if(userRepoStatus.equals("logoutSuccessful")){
                    chooseLogout();
                }
            }
        };
        userRepository.asyncStatusUpdate.observeForever(userAsyncStatusObserver);

        petsAsyncStatusObserver = new Observer<String>() {
            @Override
            public void onChanged(String petsRepoStatus) {
                if(petsRepoStatus.equals("petDeletedSuccessful")){
                    choosePetsList();
                }
            }
        };
        petsRepository.asyncStatusUpdate.observeForever(petsAsyncStatusObserver);
    }

    public void doLogout(){
        userRepository.logoutUser();
    }

    public void deletePet(){
        petsRepository.deletePet();
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

    private void choosePetsList(){
        setNextFragmentDecision("InternalMainActivity");
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
