package com.example.blindtoy_projekt_b.Views.Play;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Game.SharedOnePetViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Game.SoundSettingsViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Play.SharedPlayViewModel;

public class PlayingFragment extends Fragment {
    private static final String TAG = "L_PlayingFragment";
    private SharedPlayViewModel sharedPlayViewModel;
    private View view;

    public PlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_playing, container, false);
        sharedPlayViewModel = new ViewModelProvider(requireActivity()).get(SharedPlayViewModel.class);
        //initViews();
        //initObserver();

        Log.d(TAG,"geöffnet!");
        return view;
    }
}