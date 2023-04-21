package com.example.blindtoy_projekt_b.Views.Bluetooth;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Play.SharedPlayViewModel;
import com.example.blindtoy_projekt_b.Views.Game.OnePet.OnePetActivity;
import com.example.blindtoy_projekt_b.Views.Play.PlayWithPetActivity;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothDeviceListActivity extends AppCompatActivity {
    private static final String TAG = "L_BluetoothDeviceListActivity";

    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> newDevicesArrayAdapter;
    private ArrayAdapter<String> pairedDevicesArrayAdapter;
    //private SharedPlayViewModel sharedPlayViewModel;
    private Button scanButton;
    private AdapterView.OnItemClickListener deviceClickListener;
    private ArrayList<BluetoothDevice> availableDevicesList = new ArrayList<>();
    private ArrayList<BluetoothDevice> pairedDevices = new ArrayList<>();
    private ListView pairedListView;
    private ListView newDevicesListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device_list);
        //sharedPlayViewModel = new ViewModelProvider(this).get(SharedPlayViewModel.class);
        checkPermissions();
        // Get the local Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        setBroadCastFilters();
        initViews();
        checkForPairedDevices();
        Log.d(TAG, "geöffnet!");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (bluetoothAdapter != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            bluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(foundDeviceBroadCastReceiver);
        this.unregisterReceiver(deviceConnectedBroadCastReceiver);
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            String scanPermission = Manifest.permission.BLUETOOTH_SCAN;
            String connectPermission = Manifest.permission.BLUETOOTH_CONNECT;
            permissionLauncher.launch(scanPermission);
            permissionLauncher.launch(connectPermission);
        }
    }

    private void initViews() {
        //Scan-Button onclick-Method
        scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doDiscovery();
                view.setVisibility(View.GONE);
            }
        });

        //Initialize ArrayAdapter for paired devices and for new devices
        pairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
        newDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        //Device-List itemClickListener:
        deviceClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index = i;
                Log.d(TAG,"clicked index: " + index);
                //if click comes from pairedDevicesList:
                if (adapterView == pairedListView)
                {
                    connectToDevice(index, false);
                }
                //if click comes from newDevicesList:
                else if (view == newDevicesListView){
                    connectToDevice(index,true);
                }
            }
        };

        // Find and set up the ListView for paired devices
        pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(deviceClickListener);

        // Find and set up the ListView for newly discovered devices
        newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(newDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(deviceClickListener);
    }


    private void setBroadCastFilters() {
        // Register for broadcasts when a device is discovered
        IntentFilter foundDeviceFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(foundDeviceBroadCastReceiver, foundDeviceFilter);

        // Register for broadcasts when discovery has finished
        IntentFilter discoveryFinishedFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(foundDeviceBroadCastReceiver, discoveryFinishedFilter);

        //Register for broadcasts when a device was bonded successfully
        IntentFilter deviceConnectedFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(deviceConnectedBroadCastReceiver, deviceConnectedFilter);
    }

    private void checkForPairedDevices() {

        // Get a set of currently paired devices
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Set<BluetoothDevice> pairedDevicesSet = bluetoothAdapter.getBondedDevices();
        pairedDevices = new ArrayList<>(pairedDevicesSet);

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE); //change this to header.... from xml file
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            pairedDevicesArrayAdapter.add(noDevices);
        }
    }


    private void connectToDevice(int index, boolean isNew) { //called via onItemClickListener in Devices-List
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG,"connect to device was called by click");
        bluetoothAdapter.cancelDiscovery();
        BluetoothDevice chosenDevice;
        if(isNew){
            chosenDevice = availableDevicesList.get(index);
        }
        else{
             chosenDevice = pairedDevices.get(index);
        }
        String address = chosenDevice.getAddress();
        try {
            chosenDevice.createBond();
            Log.d(TAG, "successfully bonded with device");
            //important: if device is already bonded, there won't be a broadcast for action_state_changed, so the intent has to be triggered directly:
            if (chosenDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                Log.d(TAG,"Bond-state: Device already bonded!");
                Intent backToPlayIntent = new Intent(getApplicationContext(), PlayWithPetActivity.class);
                startActivity(backToPlayIntent);
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.toString());
        }
    }


    private ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    if (isGranted) {
                        Log.d(TAG, "permission granted");
                    } else {
                        Log.d(TAG, "permission denied");
                        Intent closeSearchIntent = new Intent(getApplicationContext(), PlayWithPetActivity.class);
                        startActivity(closeSearchIntent);
                    }
                }
            }
    );

    //region *Device discovery (and broadcastfilter for action_found and action_discovery finished)
    private void doDiscovery() {
        Log.d(TAG, "doDiscovery()");
        // Indicate scanning in the title
        // setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);
        // Turn on sub-title for new devices
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        bluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver foundDeviceBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                availableDevicesList.add(device);

                // If it's already paired, skip it, because it's been listed already
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    newDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setTitle(R.string.select_device);
                if (newDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    newDevicesArrayAdapter.add(noDevices);
                } else {
                    for (BluetoothDevice device : availableDevicesList) {
                        Log.d(TAG, device.toString());
                    }
                }
            }
        }
    };
//endregion

    private BroadcastReceiver deviceConnectedBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice myDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //Check permissions...
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Log.d(TAG, "Bonding-State changed for device:" + myDevice.getName());

                //3 cases:
                //case 1: bonded already
                if (myDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG,"Bond-state: A device is bonded.");
                    Intent backToPlayIntent = new Intent(getApplicationContext(), PlayWithPetActivity.class);
                    startActivity(backToPlayIntent);
                }
                //case 2: just creating a bond
                else if(myDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.d(TAG,"Bond-state: A device is just trying to bond");

                }
                //case 3: bond is broken
                else if(myDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.d(TAG,"Bond-state: No devices bonded.");
                }
            }
        }
    };
}