package com.example.blindtoy_projekt_b.ViewModels.Internal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.blindtoy_projekt_b.Entities.Pet;
import com.example.blindtoy_projekt_b.Repositories.PetsRepository;

import java.util.ArrayList;

public class PetsListViewModel extends AndroidViewModel {
    private static final String TAG = "L_PetsListViewModel";
    private PetsRepository petsRepository;

    public MediatorLiveData<ArrayList<String>> stringPetsList = new MediatorLiveData<>();


    public PetsListViewModel(@NonNull Application application) {
        super(application);
        petsRepository = PetsRepository.getInstance(application);
        stringPetsList.addSource(petsRepository.userPetsList, new Observer<ArrayList<Pet>>() {
            @Override
            public void onChanged(ArrayList<Pet> pets) {
                ArrayList<Pet> localPetsList = pets;
                ArrayList<String> localPetsStrings = new ArrayList<>();
                if(pets.size()>0){
                    for ( int i = 0; i < localPetsList.size(); i++){
                        localPetsStrings.add(localPetsList.get(i).name + " (" + localPetsList.get(i).species + ")");
                    }
                }
                else{
                    localPetsStrings.add("Lege ein Haustier an!");
                }
                stringPetsList.setValue(localPetsStrings);
            }
        });
    }
}
