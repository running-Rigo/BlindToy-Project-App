package com.example.blindtoy_projekt_b.ViewModels.Game;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.blindtoy_projekt_b.Repositories.PetsRepository;

public class SoundSettingsViewModel extends AndroidViewModel {
    private static final String TAG = "L_SoundSettingsViewModel";
    private String chosenPetSoundString;
    private PetsRepository petsRepository;

    public SoundSettingsViewModel(@NonNull Application application) {
        super(application);
        petsRepository = PetsRepository.getInstance(application);
        chosenPetSoundString = petsRepository.getOneChosenPet().sounds; //takes at creation the soundSettings of the chosenPet
    }

    public void saveSettings(int file1index, int file2index ){

    }


}
