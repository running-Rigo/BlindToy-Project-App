package com.example.blindtoy_projekt_b.ViewModels.Play;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BluetoothViewModel extends AndroidViewModel {
    private static final String TAG = "L_BluetoothStateViewModel";
    private MutableLiveData<Boolean> mutableBtIsEnabled = new MutableLiveData<>();
    public LiveData<Boolean> btIsEnabled;

    public BluetoothViewModel(@NonNull Application application) {
        super(application);
        setBtIsEnabled(false);
    }

    public void setBtIsEnabled(boolean isEnabled){
        mutableBtIsEnabled.setValue(isEnabled);
        btIsEnabled = mutableBtIsEnabled;
    }

}
