package com.example.blindtoy_projekt_b.ViewModels.Play;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.blindtoy_projekt_b.Bluetooth.BtConnectionService;
import com.example.blindtoy_projekt_b.Repositories.Entities.Pet;
import com.example.blindtoy_projekt_b.Repositories.PetsRepository;

public class SharedPlayViewModel extends AndroidViewModel {
    private static final String TAG = "L_SharedPlayViewModel";
    private PetsRepository petsRepository;
    private Context context;
    public LiveData<String> nextFragmentDecision;
    public LiveData<Pet> chosenPet;
    private MutableLiveData<Pet> mutableChosenPet = new MutableLiveData<>();
    private MutableLiveData<String> nextUI = new MutableLiveData<>();

    private BtConnectionService btConnectionService;

    public LiveData<String> blindSightStatus;

    private String soundSettingsString;
    private String[] soundsArray;


    public SharedPlayViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        btConnectionService = new BtConnectionService(context);
        blindSightStatus = btConnectionService.connectionStatus;
        btConnectionService.checkForBlindDog();
        petsRepository = PetsRepository.getInstance(application);
        setChosenPet(petsRepository.getOneChosenPet()); //takes at creation only once the chosenPet from userrepo
        soundSettingsString = petsRepository.getOneChosenPet().sounds;
        soundsArray = soundSettingsString.split(",");
        setNextFragmentDecision("");
    }

    private void setNextFragmentDecision(String nextUIChoice) {
        nextUI.setValue(nextUIChoice);
        this.nextFragmentDecision = nextUI;
        Log.d(TAG,"Method setNextFragmentDecision aufgerufen: " + nextFragmentDecision.getValue().toString());
    }

    public void connectToBlindSight() {
        btConnectionService.setStatusConnecting();
        btConnectionService.connectToBlindSight();
    }


    private void setChosenPet(Pet repoPet){
        mutableChosenPet.setValue(repoPet);
        chosenPet = mutableChosenPet;
    }

    public void activateBeeping(){
        char pitch = 'b'; //default medium pitch
        if(soundsArray[2].equals("1")){ //deep sound
            pitch = 'a';
        }
        else if(soundsArray[2].equals("3")){ //high sound
            pitch = 'c';
        }


        char speed = 'e'; //default medium speed
        if(soundsArray[3].equals("1")){ //slow
            speed = 'd';
        }
        else if(soundsArray[3].equals("3")){ //fast
            speed = 'f';
        }


        char beat = '1'; //default single beep
        if(soundsArray[4].equals("2")){
            beat = '2';
        }
        else if(soundsArray[4].equals("3")){
            beat = '3';
        }
        else if(soundsArray[4].equals("4")){
            beat = '4';
        }

        btConnectionService.sendChar(pitch);
        btConnectionService.sendChar(speed);
        btConnectionService.sendChar(beat);
    }

    //for instance sounds = "1,2,2,2,1"; soundSettings are for a new pet set to default values (file1,file2,mediumTonePitch,mediumBeepingSpeed,single*beep*)

    public void deactivateBeeping(){
        //x
        for(int i = 0; i < 10; i++){
            btConnectionService.sendChar('x');
        }
    }

    public void playFirstMP3(){
        //how to handle this?
    }

    public void playSecondMP3(){
        //how to handle this?
    }


    public void stopPlaying(){
        setNextFragmentDecision("OnePetActivity");
    }


}
