package com.example.blindtoy_projekt_b.Views.Internal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.blindtoy_projekt_b.Entities.Pet;
import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Internal.InternalSharedViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Internal.PetsListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PetsListFragment extends Fragment {
    private static final String TAG = "L_PetsListFragment";
    View view;
    InternalSharedViewModel internalSharedViewModel;
    PetsListViewModel petsListViewModel;
    ArrayAdapter<String> adapter;
    ListView listView;
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { // i is position of item in list
                Toast.makeText(getContext(),"Pet clicked: " + adapter.getItem(i), Toast.LENGTH_SHORT).show();
                internalSharedViewModel.setChosenPet(i);
                internalSharedViewModel.chooseOnePet();
            }
        });
    }

    private void initObservers(){
        petsListViewModel.stringPetsList.observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> petsStrings) {
                Log.d(TAG,"change in petslist noticed!");
                adapter = new ArrayAdapter<String>(getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item,petsStrings);
                listView.setAdapter(adapter);
            }
        });
    }
}