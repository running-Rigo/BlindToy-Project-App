package com.example.blindtoy_projekt_b.ViewModels.Game;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.blindtoy_projekt_b.Repositories.PetsRepository;

import java.lang.reflect.Array;
import java.nio.channels.MulticastChannel;
import java.util.ArrayList;

public class SoundSettingsViewModel extends AndroidViewModel {
    private static final String TAG = "L_SoundSettingsViewModel";
    private String chosenPetSoundString;
    private MutableLiveData<ArrayList<String>> mutableChosenSoundSettings = new MutableLiveData();
    public LiveData<ArrayList<String>> chosenSoundSettings;
    private PetsRepository petsRepository;

    public String[] soundSettings = new String[5];

    public SoundSettingsViewModel(@NonNull Application application) {
        super(application);
        petsRepository = PetsRepository.getInstance(application);
        chosenPetSoundString = petsRepository.getOneChosenPet().sounds; //takes at creation the soundSettings of the chosenPet
    }

    private void initChosenSoundSettings(){
        soundSettings = chosenPetSoundString.split(",");
        ArrayList<String> soundSettingsList = new ArrayList<>();
        for(String sound : soundSettings){
            soundSettingsList.add(sound);
        }
        mutableChosenSoundSettings.setValue(soundSettingsList);
        chosenSoundSettings = mutableChosenSoundSettings;
    }

    public void saveSettings (){



    }


}
