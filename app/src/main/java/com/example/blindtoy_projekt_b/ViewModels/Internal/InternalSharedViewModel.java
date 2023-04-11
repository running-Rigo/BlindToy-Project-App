package com.example.blindtoy_projekt_b.ViewModels.Internal;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;
import com.example.blindtoy_projekt_b.Repositories.PetsRepository;

public class InternalSharedViewModel extends AndroidViewModel {
    private static final String TAG = "L_InternalSharedViewModel";
    private GeneralUserRepository userRepository;
    private PetsRepository petsRepository;
    public LiveData<String> nextFragmentDecision;
    private MutableLiveData<String> nextUI = new MutableLiveData<>();

    public MediatorLiveData<String>reposErrorMessage = new MediatorLiveData<>();

    private Observer<String> userAsyncStatusObserver;
    private Observer<String> petsAsyncStatusObserver;

    public InternalSharedViewModel(@NonNull Application application) {
        super(application);
        userRepository = GeneralUserRepository.getInstance(application);
        petsRepository = PetsRepository.getInstance(application);
        reposErrorMessage.addSource(userRepository.repoErrorMessage, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                reposErrorMessage.setValue(s);
            }
        });
        reposErrorMessage.addSource(petsRepository.repoErrorMessage, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                reposErrorMessage.setValue(s);
            }
        });
        setNextFragmentDecision("");
        setChosenPet(0);
        initObserver();
    }
    @Override
    protected void onCleared(){
        userRepository.asyncStatusUpdate.removeObserver(userAsyncStatusObserver);
        petsRepository.asyncStatusUpdate.removeObserver(petsAsyncStatusObserver);
    }

    public void setChosenPet(int index){
        Log.d(TAG, "index clicked: " + String.valueOf(index));
        petsRepository.setOneChosenPet(index);
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
                if(petsRepoStatus.equals("newPetSuccessful")){
                    choosePetsOverview();
                }
            }
        };
        petsRepository.asyncStatusUpdate.observeForever(petsAsyncStatusObserver);
    }

    public void doLogout(){
        userRepository.logoutUser();
    }


    private void setNextFragmentDecision(String nextUIChoice) {
        nextUI.setValue(nextUIChoice);
        this.nextFragmentDecision = nextUI;
        Log.d(TAG,"Method setNextFragmentDecision aufgerufen: " + nextFragmentDecision.getValue().toString());
    }

//region Layout-Changes

    public void chooseAddPet(){
        setNextFragmentDecision("AddPetFragment");
    }
    private void chooseLogout(){
        setNextFragmentDecision("MainActivity");
    }

    public void choosePetsOverview(){
        setNextFragmentDecision("PetsListFragment");
    }
    public void chooseOnePet(){
        setNextFragmentDecision("OnePetActivity");
    }
    //endregion

}
