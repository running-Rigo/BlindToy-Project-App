package com.example.blindtoy_projekt_b.Views.Play;

import static androidx.core.content.ContextCompat.registerReceiver;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.blindtoy_projekt_b.Bluetooth.BtConnectionService;
import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Play.BluetoothViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Play.SharedPlayViewModel;
import com.example.blindtoy_projekt_b.Views.Bluetooth.BluetoothDeviceListActivity;


public class BluetoothStateFragment extends Fragment {
    private static final String TAG = "L_BluetoothStateFragment";
    private Button btActivityButton;
    private View view;
    private SharedPlayViewModel sharedPlayViewModel;
    private BluetoothViewModel bluetoothViewModel;
    public BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;

    private String connectedDeviceName = null;

    private BtConnectionService btConnectionService;

    public BluetoothStateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bluetooth_state, container, false);
        //sharedPlayViewModel = new ViewModelProvider(requireActivity()).get(SharedPlayViewModel.class);
        //bluetoothViewModel = new ViewModelProvider(this).get(BluetoothViewModel.class);
        initViews();
        initBluetooth();
        Log.d(TAG, "geöffnet!");
        return view;
    }


    private void initViews() {
        btActivityButton = view.findViewById(R.id.bt_choices_button);
        btActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBluetoothDeviceListActivity();
            }
        });
    }


    private void initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Log.d(TAG,"Your mobile phone doesn't support bluetooth.");
        }
        else if (!bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "Bluetooth not available");

            String permission = Manifest.permission.BLUETOOTH_CONNECT;
            permissionLauncher.launch(permission);
        }
        else if(bluetoothAdapter.isEnabled()){
            Log.d(TAG,"Bluetooth is available.");
            btActivityButton.setEnabled(true);
        }
    }

//region *enable Bluetooth (Permission, enable-Intent, broadcast-receiver for state-change)
    private ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    if (isGranted) {
                        Log.d(TAG, "permission granted");
                        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(enableBluetoothIntent);
                        //state changes like enable/disable bluetooth are registered:
                        IntentFilter btEnableFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                        getContext().registerReceiver(btEnableBroadCastReceiver,btEnableFilter);
                    } else {
                        Log.d(TAG, "permission denied");
                        //sharedPlayViewModel.setPlayIsActive(false);
                    }
                }
            }
    );

    private BroadcastReceiver btEnableBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(bluetoothAdapter.ACTION_STATE_CHANGED)){
                if(bluetoothAdapter.isEnabled()){
                    btActivityButton.setEnabled(true);
                }
                else if(!bluetoothAdapter.isEnabled()){
                    btActivityButton.setEnabled(false);
                }
            }
        }
    };

//endregion

//region *start DeviceListActivity (including broadcastreceiver vor connection
    private void startBluetoothDeviceListActivity(){
        Intent searchDevicesIntent = new Intent(getActivity(), BluetoothDeviceListActivity.class);
        startActivity(searchDevicesIntent);
    }
//endregion
}

