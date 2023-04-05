package com.example.blindtoy_projekt_b.Entities;

import java.util.ArrayList;

public class SoundSettings {
    private ArrayList<Integer> selectedMp3s = new ArrayList<>();
    private int beepingSpeed = 3; //beeps per minute;
    private int beepingTone = 1;


    @Override
    public String toString() {
        //build String to save in Database
        return "";
    }
}
