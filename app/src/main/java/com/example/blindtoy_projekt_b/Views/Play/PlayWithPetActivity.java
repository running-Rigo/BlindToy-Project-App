package com.example.blindtoy_projekt_b.Views.Play;

import static androidx.core.content.ContextCompat.getSystemService;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.companion.BluetoothDeviceFilter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Play.SharedPlayViewModel;
import com.example.blindtoy_projekt_b.Views.Game.OnePet.OnePetActivity;
import com.example.blindtoy_projekt_b.Views.Game.OnePet.OnePetFragment;
import com.example.blindtoy_projekt_b.Views.Game.SoundSettingsFragment;
import com.example.blindtoy_projekt_b.Views.Internal.InternalMainActivity;
import com.example.blindtoy_projekt_b.Views.Login.LoadingFragment;
import com.example.blindtoy_projekt_b.Views.Login.MainActivity;

import java.util.regex.Pattern;

public class PlayWithPetActivity extends AppCompatActivity {
    private static final String TAG = "L_PlayWithPetActivity";
    private final int REQUEST_ENABLE_BT = 0;
    private Button stopBtn;
    private SharedPlayViewModel sharedPlayViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_pet);
        sharedPlayViewModel = new ViewModelProvider(this).get(SharedPlayViewModel.class);
        initViews();
        initObservers();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) { //Discovery has found a device
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); //MAC-Address
            }
        }
    };

    private void initViews() {
        stopBtn = findViewById(R.id.stop_btn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPlayViewModel.stopPlaying();
            }
        });
    }

    private void initObservers() {
        sharedPlayViewModel.nextFragmentDecision.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String nextUI) {
                if (nextUI.equals("OnePetActivity")) {
                    //stop playing and go to OnePetActivity
                    Intent stopIntent = new Intent(getApplicationContext(), OnePetActivity.class);
                    startActivity(stopIntent);
                }
                /*else if (nextUI.equals("BluetoothActivity")) {
                    //stop playing and go to OnePetActivity
                    Intent btIntent = new Intent(getApplicationContext(), BluetoothActivity.class);
                    startActivity(btIntent);
                }
                 */
            }
        });

        sharedPlayViewModel.playIsActive.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isActive) {
                if(!isActive){
                    Log.d(TAG,"playFragment should not be clickable!");
                }
                else{
                    Log.d(TAG,"playFragment should be active");
                }
            }
        });
    }
}