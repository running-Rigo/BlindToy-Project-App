package com.example.blindtoy_projekt_b.Views.Game;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blindtoy_projekt_b.Data.LocalRoomsData.Pet;
import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Internal.InternalSharedViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Game.SharedOnePetViewModel;

public class OnePetFragment extends Fragment {
    private static final String TAG = "OnePetFragment";
    private View view;
    private SharedOnePetViewModel sharedOnePetViewModel;
    private Button settingsButton, playButton;
    private TextView petNameText;
    private ImageView speciesImg;


    public OnePetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_one_pet, container, false);
        sharedOnePetViewModel = new ViewModelProvider(requireActivity()).get(SharedOnePetViewModel.class);
        initViews();
        initObserver();
        Log.d(TAG,"geöffnet!");
        return view;
    }

    private void initViews(){
        settingsButton = view.findViewById(R.id.settingsBtn);
        playButton = view.findViewById(R.id.playGameBtn);
        speciesImg = view.findViewById(R.id.speciesImg);
        petNameText = view.findViewById(R.id.petsListHeader);
        if(!sharedOnePetViewModel.getHasSettings()){
            playButton.setEnabled(false);
        }

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedOnePetViewModel.chooseSoundSettings();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedOnePetViewModel.choosePlay();
            }
        });
    }

    private void initObserver(){
        sharedOnePetViewModel.chosenPet.observe(getViewLifecycleOwner(), new Observer<Pet>() {
            @Override
            public void onChanged(Pet pet) {
                if(pet.species.toLowerCase().equals("hund") || pet.species.toLowerCase().equals("dog")){
                    speciesImg.setBackgroundResource(R.drawable.hotdog_btn_pic);
                }
                else if(pet.species.toLowerCase().equals("katze") || pet.species.toLowerCase().equals("cat")){
                    speciesImg.setBackgroundResource(R.drawable.cat_btn_pic);
                }
                else if(pet.species.toLowerCase().equals("pferd") || pet.species.toLowerCase().equals("horse")){
                    speciesImg.setBackgroundResource(R.drawable.horse_btn_pic);
                }
                petNameText.setText("Dein Tier: " +pet.name);
            }
        });
    }
}
