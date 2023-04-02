package com.example.blindtoy_projekt_b.Views.Internal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.blindtoy_projekt_b.Data.LocalRoomsData.Pet;
import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Internal.InternalSharedViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Internal.PetsListViewModel;
import com.example.blindtoy_projekt_b.Views.Login.RegistrationFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PetsListFragment extends Fragment {
    private static final String TAG = "PetsListFragment";
    View view;
    InternalSharedViewModel internalSharedViewModel;
    PetsListViewModel petsListViewModel;
    ArrayAdapter<String> adapter;
    ListView listView;

    ArrayList<Pet> localPetsList = new ArrayList<>();
    ArrayList<String> petsToRender = new ArrayList<>();
    FloatingActionButton addPetButton;


    public PetsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pets_list, container, false);
        internalSharedViewModel = new ViewModelProvider(requireActivity()).get(InternalSharedViewModel.class);
        // The ViewModel is scoped to `this` Fragment
        petsListViewModel = new ViewModelProvider(this).get(PetsListViewModel.class);
        initViews();
        initObservers();
        Log.d(TAG,"geöffnet!");
        return view;
    }
    private void initViews(){
        addPetButton = view.findViewById(R.id.addPetButton);
        addPetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                internalSharedViewModel.chooseAddPet();
            }
        });
        listView = view.findViewById(R.id.petsListView);
        adapter = new ArrayAdapter<String>(getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item,petsToRender);
        listView.setAdapter(adapter);
    }

    private void initObservers(){
        petsListViewModel.petsListFromRepo.observe(getViewLifecycleOwner(),new Observer<ArrayList<Pet>>() {
            @Override
            public void onChanged(ArrayList<Pet> pets) {
                localPetsList = pets;
                if(localPetsList != null){
                    for (Pet pet: localPetsList){
                        String petDescription = pet.name +" (" + pet.species +")";
                        petsToRender.add(petDescription);
                    }
                }
                else{
                    petsToRender.add("Lege dein erstes Haustier an!");
                }
            }
        });
    }
}