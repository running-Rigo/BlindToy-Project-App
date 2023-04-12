package com.example.blindtoy_projekt_b.ViewModels.Play;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.blindtoy_projekt_b.Entities.Pet;
import com.example.blindtoy_projekt_b.Repositories.PetsRepository;

public class SharedPlayViewModel extends AndroidViewModel {
    private static final String TAG = "L_SharedPlayViewModel";
    private PetsRepository petsRepository;
    private Context context;
    public LiveData<String> nextFragmentDecision;
    public LiveData<Pet> chosenPet;
    private MutableLiveData<Pet> mutableChosenPet = new MutableLiveData<>();
    private MutableLiveData<String> nextUI = new MutableLiveData<>();

    public LiveData<Boolean> playIsActive;
    private MutableLiveData<Boolean> mutablePlayIsActive = new MutableLiveData<>();



    public SharedPlayViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        petsRepository = PetsRepository.getInstance(application);
        setChosenPet(petsRepository.getOneChosenPet()); //takes at creation only once the chosenPet from userrepo
        setNextFragmentDecision("");
        setPlayIsActive(false);

    }

    private void setNextFragmentDecision(String nextUIChoice) {
        nextUI.setValue(nextUIChoice);
        this.nextFragmentDecision = nextUI;
        Log.d(TAG,"Method setNextFragmentDecision aufgerufen: " + nextFragmentDecision.getValue().toString());
    }



    private void setChosenPet(Pet repoPet){
        mutableChosenPet.setValue(repoPet);
        chosenPet = mutableChosenPet;
    }


    public void setPlayIsActive(boolean isActive){
        mutablePlayIsActive.setValue(isActive);
        playIsActive = mutablePlayIsActive;
    }

    public void stopPlaying(){
        setNextFragmentDecision("OnePetActivity");
    }


}
