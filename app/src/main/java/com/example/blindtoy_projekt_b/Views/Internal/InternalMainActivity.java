package com.example.blindtoy_projekt_b.Views.Internal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.ViewModels.Internal.InternalSharedViewModel;
import com.example.blindtoy_projekt_b.ViewModels.Login.SharedViewModel;
import com.example.blindtoy_projekt_b.Views.Login.LoginFragment;
import com.example.blindtoy_projekt_b.Views.Login.MainActivity;
import com.example.blindtoy_projekt_b.Views.Login.RegistrationFragment;

public class InternalMainActivity extends AppCompatActivity {
    private static final String TAG = "InternalMainActivity";
    private InternalSharedViewModel internalSharedViewModel;

    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_main);
        internalSharedViewModel = new ViewModelProvider(this).get(InternalSharedViewModel.class);
        initViews();
        initObservers();
        Log.d(TAG,"geöffnet!");
    }

    private void initViews(){
        logoutBtn = findViewById(R.id.internalLogoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internalSharedViewModel.doLogout();
            }
        });
    }

    private void initObservers(){
        //Observer for UI-Decisions
        internalSharedViewModel.nextFragmentDecision.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String nextUI) {
                Log.d(TAG, "registered change in nextFragmentDecision");
                if(nextUI.equals("MainActivity")){
                    //start MainActivity
                    Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(logoutIntent);
                }

                else if(nextUI.equals("PetsListFragment")){
                    //render LoginFragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.internal_fragment_container_view, PetsListFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // name can be null
                            .commit();
                }
                else if(nextUI.equals("AddPetFragment")){
                    //render LoginFragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.internal_fragment_container_view, AddPetFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // name can be null
                            .commit();
                }
                /*
                else if(nextUI.equals("RegistrationFragment")){
                    //render RegistrationFragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, RegistrationFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // name can be null
                            .commit();
                }

                 */
            }
        });

        //Observer for Error-Toasts
        internalSharedViewModel.repoErrorMessage.observe(this, new Observer<String>() {
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