package com.example.blindtoy_projekt_b.Views.Game;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Game.SharedOnePetViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Game.SoundSettingsViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Internal.PetsListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SoundSettingsFragment extends Fragment {

    //region Variables
    private static final String TAG = "L_SoundSettingsFragment";
    private SharedOnePetViewModel sharedOnePetViewModel;
    private SoundSettingsViewModel soundSettingsViewModel;
    private View view;
    private String[] files1Array = {"Da komm her", "Such such", "Miau","*schnalz-Geräusch*"};
    private String[] files2Array = {"Da komm her", "Such such", "Miau","*schnalz-Geräusch*"};
    private String[] pitchesArray = {"tiefe Tonlage", "mittlere Tonlage", "hoche Tonlage"};
    private String[] speedArray = {"langsam", "mittel", "schnell"};
    private String[] beatsArray = {"beep", "beep-beep", "beep-beeeeep","beeeep"};
    private Spinner spinnerFile1,spinnerFile2,spinnerPitch,spinnerSpeed,spinnerBeat;
    private ArrayAdapter<String> adapterFile1,adapterFile2,adapterPitch,adapterSpeed,adapterBeat;
    private FloatingActionButton saveSettings;
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
        Log.d(TAG,"geöffnet!");
        return view;
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
                String selectedItem = files1Array[i];
                soundSettingsViewModel.soundSettings[i] = selectedItem;
            }
            else if(adapterView == spinnerFile2){
                String selectedItem = files2Array[i];
                soundSettingsViewModel.soundSettings[i] = selectedItem;
            }
            else if(adapterView == spinnerPitch){
                String selectedItem = pitchesArray[i];
                soundSettingsViewModel.soundSettings[i] = selectedItem;
            }
            else if(adapterView == spinnerSpeed){
                String selectedItem = speedArray[i];
                soundSettingsViewModel.soundSettings[i] = selectedItem;
            }
            else if(adapterView == spinnerBeat){
                String selectedItem = beatsArray[i];
                soundSettingsViewModel.soundSettings[i] = selectedItem;
            }
            Log.d(TAG, soundSettingsViewModel.soundSettings[i]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Log.d(TAG, "nothing selected");
        }
    }


}