package com.example.blindtoy_projekt_b.Views.Play;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.blindtoy_projekt_b.Bluetooth.BtConnectionService;
import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Play.SharedPlayViewModel;

import java.util.UUID;

public class PlayingFragment extends Fragment {
    private static final String TAG = "L_PlayingFragment";
    private SharedPlayViewModel sharedPlayViewModel;
    private View view;
    private SwitchCompat beepingButton;
    private Button mp3_1_Btn,mp3_2_Btn;

    private BtConnectionService btConnectionService; //self written class from tutorial

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothDevice bluetoothDevice;
    private EditText sendMessage;

    public PlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_playing, container, false);
        sharedPlayViewModel = new ViewModelProvider(requireActivity()).get(SharedPlayViewModel.class);
        initViews();
        initObserver();
        Log.d(TAG,"geöffnet!");
        return view;
    }


    private void initViews(){
        beepingButton = view.findViewById(R.id.beepingButton);
        beepingButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isBeeping) {
                if(isBeeping){
                    sharedPlayViewModel.activateBeeping();
                }
                else{
                    sharedPlayViewModel.deactivateBeeping();
                }
            }
        });

        mp3_1_Btn = view.findViewById(R.id.mp3_1_btn);
        mp3_1_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPlayViewModel.playFirstMP3();
            }
        });

        mp3_2_Btn = view.findViewById(R.id.mp3_2_btn);
        mp3_2_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPlayViewModel.playSecondMP3();
            }
        });
    }

    private void initObserver(){
        Observer<String> blindSightStatusObserver = new Observer<String>() {
            @Override
            public void onChanged(String blindSightStatus) {
                if(blindSightStatus.equals("connected")){
                    beepingButton.setEnabled(true);
                }
                else{
                    beepingButton.setEnabled(false);
                }
            }
        };
        sharedPlayViewModel.blindSightStatus.observe(getViewLifecycleOwner(),blindSightStatusObserver);
    }
}
