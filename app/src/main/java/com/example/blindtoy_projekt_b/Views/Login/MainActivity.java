package com.example.blindtoy_projekt_b.Views.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Login.SharedViewModel;
import com.example.blindtoy_projekt_b.Views.Internal.InternalMainActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        initObservers();
        Log.d(TAG,"geöffnet!");
    }

    private void initObservers(){
        //Observer for UI-Decisions
        sharedViewModel.nextFragmentDecision.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String nextUI) {
                Log.d(TAG, "registered change in nextFragmentDecision");
                if(nextUI.equals("InternalMainActivity")){
                    //start InternalMainActivity
                    Intent loginIntent = new Intent(getApplicationContext(), InternalMainActivity.class);
                    startActivity(loginIntent);
                }
                else if(nextUI.equals("LoginFragment")){
                    //render LoginFragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, LoginFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // name can be null
                            .commit();
                }
                else if(nextUI.equals("RegistrationFragment")){
                    //render RegistrationFragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, RegistrationFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // name can be null
                            .commit();
                }
            }
        });

        //Observer for Error-Toasts
        sharedViewModel.repoErrorMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if(!errorMessage.equals("")){
                    Toast registrationError = Toast.makeText(getApplicationContext(),errorMessage, Toast.LENGTH_SHORT);
                    registrationError.show();
                }
            }
        });
    }
}