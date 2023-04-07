package com.example.blindtoy_projekt_b.ViewModels.Internal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.blindtoy_projekt_b.Repositories.PetsRepository;

public class AddPetViewModel extends AndroidViewModel {
    private static final String TAG = "L_AddPetViewModel";
    private PetsRepository petsRepository;

    public AddPetViewModel(@NonNull Application application) {
        super(application);
        petsRepository = PetsRepository.getInstance(application);
    }

    public void createPet(String name, String species){
        String defaultSounds = "1,2,2,2,1";
        petsRepository.saveNewPet(name,species,defaultSounds);
    }
}
