package com.example.blindtoy_projekt_b.Views.Game;

import android.app.Activity;
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
import android.widget.Spinner;
import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Game.SharedOnePetViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Game.SoundSettingsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


public class SoundSettingsFragment extends Fragment {

    //region Variables
    private static final String TAG = "L_SoundSettingsFragment";
    private SharedOnePetViewModel sharedOnePetViewModel;
    private SoundSettingsViewModel soundSettingsViewModel;
    private View view;
    private String[] files1Array = {"Da komm her", "Such such", "Miau","*schnalz-Geräusch*"};
    private String[] files2Array = {"Da komm her", "Such such", "Miau","*schnalz-Geräusch*"};
    private String[] pitchesArray = {"tiefe Tonlage", "mittlere Tonlage", "hohe Tonlage"};
    private String[] speedArray = {"langsam", "mittel", "schnell"};
    private String[] beatsArray = {"beep", "beep-beep", "beep-beeeeep","beeeep-beeeep"};
    private Spinner spinnerFile1,spinnerFile2,spinnerPitch,spinnerSpeed,spinnerBeat;
    private ArrayAdapter<String> adapterFile1,adapterFile2,adapterPitch,adapterSpeed,adapterBeat;
    private FloatingActionButton saveSettings;

    private ArrayList<String> soundSettingsOfPet;

    //endregion


    public SoundSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sound_settings, container, false);
        sharedOnePetViewModel = new ViewModelProvider(requireActivity()).get(SharedOnePetViewModel.class);
        soundSettingsViewModel = new ViewModelProvider(this).get(SoundSettingsViewModel.class);
        initViews();
        initObserver();

        Log.d(TAG,"geöffnet!");
        return view;
    }

    private void initObserver(){ // the actual settings for the pet are set as selected in the dropdown menu;
        soundSettingsViewModel.chosenSoundSettings.observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> settings) {
                soundSettingsOfPet = settings;
                for(int i = 0; i < soundSettingsOfPet.size(); i++){
                    if(i == 0){
                        spinnerFile1.setSelection(Integer.parseInt(soundSettingsOfPet.get(i))-1);
                    }
                    else if(i == 1){
                        spinnerFile2.setSelection(Integer.parseInt(soundSettingsOfPet.get(i))-1);
                    }
                    else if(i == 2){
                        spinnerPitch.setSelection(Integer.parseInt(soundSettingsOfPet.get(i))-1);
                    }
                    else if(i == 3){
                        spinnerSpeed.setSelection(Integer.parseInt(soundSettingsOfPet.get(i))-1);
                    }
                    else if(i == 4){
                        spinnerBeat.setSelection(Integer.parseInt(soundSettingsOfPet.get(i))-1);
                    }
                }
            }
        });
    }

    private void initViews() {
        spinnerFile1 = view.findViewById(R.id.dropdown_file1);
        initSpinner(spinnerFile1,adapterFile1,files1Array);
        spinnerFile2 = view.findViewById(R.id.dropdown_file2);
        initSpinner(spinnerFile2,adapterFile2,files2Array);
        spinnerPitch = view.findViewById(R.id.dropdown_pitch);
        initSpinner(spinnerPitch,adapterPitch,pitchesArray);
        spinnerSpeed = view.findViewById(R.id.dropdown_speed);
        initSpinner(spinnerSpeed,adapterSpeed,speedArray);
        spinnerBeat = view.findViewById(R.id.dropdown_beat);
        initSpinner(spinnerBeat,adapterBeat,beatsArray);

        saveSettings = view.findViewById(R.id.save_settings_button);
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundSettingsViewModel.saveSettings();
            }
        });
    }

    private void initSpinner(Spinner spinner, ArrayAdapter adapter, String[] items){
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SpinnerActivity());
    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(adapterView == spinnerFile1){
                String selectedItem = String.valueOf(i+1);
                soundSettingsViewModel.soundSettings[0] = selectedItem;
                Log.d(TAG, soundSettingsViewModel.soundSettings[0]);
            }
            else if(adapterView == spinnerFile2){
                String selectedItem = String.valueOf(i+1);
                soundSettingsViewModel.soundSettings[1] = selectedItem;
                Log.d(TAG, soundSettingsViewModel.soundSettings[1]);
            }
            else if(adapterView == spinnerPitch){
                String selectedItem = String.valueOf(i+1);
                soundSettingsViewModel.soundSettings[2] = selectedItem;
                Log.d(TAG, soundSettingsViewModel.soundSettings[2]);
            }
            else if(adapterView == spinnerSpeed){
                String selectedItem = String.valueOf(i+1);
                soundSettingsViewModel.soundSettings[3] = selectedItem;
                Log.d(TAG, soundSettingsViewModel.soundSettings[3]);
            }
            else if(adapterView == spinnerBeat){
                String selectedItem = String.valueOf(i+1);
                soundSettingsViewModel.soundSettings[4] = selectedItem;
                Log.d(TAG, soundSettingsViewModel.soundSettings[4]);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Log.d(TAG, "nothing selected");
        }
    }


}