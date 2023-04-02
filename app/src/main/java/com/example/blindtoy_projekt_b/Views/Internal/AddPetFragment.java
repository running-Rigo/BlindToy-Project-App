package com.example.blindtoy_projekt_b.Views.Internal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Internal.AddPetViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Internal.InternalSharedViewModel;

public class AddPetFragment extends Fragment {
    private static final String TAG = "NewPetFragment";
    private EditText petName, petSpecies;
    private Button submitPetButton;
    private View view;
    private InternalSharedViewModel internalSharedViewModel;
    private AddPetViewModel addPetViewmodel;

    public AddPetFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_add_pet, container, false);
        internalSharedViewModel = new ViewModelProvider(requireActivity()).get(InternalSharedViewModel.class);
        // The ViewModel is scoped to `this` Fragment
        addPetViewmodel = new ViewModelProvider(this).get(AddPetViewModel.class);
        initViews();
        Log.d(TAG,"geöffnet!");
        return view;
    }

    private void initViews(){
        petName = view.findViewById(R.id.pet_name);
        petSpecies = view.findViewById(R.id.pet_species);
        submitPetButton = view.findViewById(R.id.add_pet_now);

        submitPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredPetName = petName.getText().toString();
                String enteredPetSpecies = petSpecies.getText().toString();
                addPetViewmodel.createPet(enteredPetName,enteredPetSpecies);
            }
        });
    }
}