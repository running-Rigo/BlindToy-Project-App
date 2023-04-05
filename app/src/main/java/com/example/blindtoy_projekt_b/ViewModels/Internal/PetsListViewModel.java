package com.example.blindtoy_projekt_b.ViewModels.Internal;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.blindtoy_projekt_b.Data.LocalRoomsData.Pet;
import com.example.blindtoy_projekt_b.Repositories.GeneralUserRepository;

import java.util.ArrayList;

public class PetsListViewModel extends AndroidViewModel {
    private static final String TAG = "PetsListViewModel";
    private GeneralUserRepository userRepository;
    public LiveData<ArrayList<Pet>> petsListFromRepo;

    public PetsListViewModel(@NonNull Application application) {
        super(application);
        userRepository = GeneralUserRepository.getInstance(application);
        petsListFromRepo = userRepository.userPetsList;
    }
}
