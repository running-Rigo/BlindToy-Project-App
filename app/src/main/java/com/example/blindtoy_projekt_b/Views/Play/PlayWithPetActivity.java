package com.example.blindtoy_projekt_b.Views.Play;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.blindtoy_projekt_b.R;
import com.example.blindtoy_projekt_b.Views.Internal.InternalMainActivity;
import com.example.blindtoy_projekt_b.Views.Login.MainActivity;

public class PlayWithPetActivity extends AppCompatActivity {
    private static final String TAG = "L_PlayWithPetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_pet);
    }

}