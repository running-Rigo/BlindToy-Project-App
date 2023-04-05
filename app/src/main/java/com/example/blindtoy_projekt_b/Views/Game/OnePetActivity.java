package com.example.blindtoy_projekt_b.Views.Game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Game.SharedOnePetViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Internal.InternalSharedViewModel;
import com.example.blindtoy_projekt_b.Views.Login.MainActivity;
import com.example.blindtoy_projekt_b.Views.Play.PlayWithPetActivity;

public class OnePetActivity extends AppCompatActivity {
    private static final String TAG = "OnePetActivity";
    private SharedOnePetViewModel sharedOnePetViewModel;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_pet);
        sharedOnePetViewModel = new ViewModelProvider(this).get(SharedOnePetViewModel.class);
        initViews();
        initObservers();
        Log.d(TAG,"geöffnet!");
    }

    private void initViews(){
        logoutBtn = findViewById(R.id.internalLogoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedOnePetViewModel.doLogout();
            }
        });
    }

    private void initObservers(){
        sharedOnePetViewModel.nextFragmentDecision.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String nextUI) {
                if(nextUI.equals("PlayWithPetActivity")){
                    //start PlayWithPetActivity
                    Intent playIntent = new Intent(getApplicationContext(), PlayWithPetActivity.class);
                    startActivity(playIntent);
                }
                else if(nextUI.equals("MainActivity")){
                    //start MainActivity
                    Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(logoutIntent);
                }
                else if(nextUI.equals("SoundSettingsFragment")){
                    //render SoundSettingsFragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.onepet_fragment_container_view, SoundSettingsFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // name can be null
                            .commit();
                }
                else if(nextUI.equals("OnePetFragment")){
                    // render OnePetFragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.onepet_fragment_container_view, OnePetFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // name can be null
                            .commit();
                }
            }
        });
    }
}