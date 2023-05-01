package com.example.blindtoy_projekt_b.Views.Play;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.blindtoy_projekt_b.Bluetooth.BtConnectionService;
import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Game.SharedOnePetViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Game.SoundSettingsViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Play.SharedPlayViewModel;

import java.util.UUID;

public class PlayingFragment extends Fragment {
    private static final String TAG = "L_PlayingFragment";
    private SharedPlayViewModel sharedPlayViewModel;
    private View view;
    private Button sendButton, connectButton;

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
        //initObserver();

        Log.d(TAG,"geöffnet!");
        return view;
    }


    private void initViews(){
        sendButton = view.findViewById(R.id.send_button);
        connectButton= view.findViewById(R.id.connectBtn);
        sendMessage = view.findViewById(R.id.btInput);
    }
}

