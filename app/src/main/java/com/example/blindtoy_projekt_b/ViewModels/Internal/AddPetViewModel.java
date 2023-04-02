package com.example.blindtoy_projekt_b.ViewModels.Internal;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.blindtoy_projekt_b.Data.LocalRoomsData.Pet;
import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;

public class AddPetViewModel extends AndroidViewModel {
    private static final String TAG = "AddPetViewModel";
    private GeneralUserRepository userRepository;

    public AddPetViewModel(@NonNull Application application) {
        super(application);
        userRepository = GeneralUserRepository.getInstance(application);
    }

    public void createPet(String name, String species){
        userRepository.saveNewPet(name,species);
    }
}
