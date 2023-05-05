package com.example.blindtoy_projekt_b.ViewModels.Game;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.blindtoy_projekt_b.Repositories.Entities.Pet;
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
    private Context context;
    //
    private Observer<String> userAsyncStatusObserver;
    private Observer<String> petsAsyncStatusObserver;

    public SharedOnePetViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
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
                    Toast.makeText(context,"Tier gelöscht!", Toast.LENGTH_SHORT).show();
                    choosePetsList();
                }
                else if(petsRepoStatus.equals("settingsSavedSuccessfully")){
                    Toast.makeText(context,"Änderungen erfolgreich", Toast.LENGTH_SHORT).show();
                    chooseOnePetOverview();
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
