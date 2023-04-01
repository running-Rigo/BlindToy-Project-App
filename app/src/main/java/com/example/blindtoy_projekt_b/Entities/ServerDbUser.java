package com.example.blindtoy_projekt_b.Entities;

import com.example.blindtoy_projekt_b.Data.LocalRoomsData.Pet;
import com.example.blindtoy_projekt_b.Data.LocalRoomsData.Pet;

import java.util.ArrayList;

public class ServerDbUser {
    private String user_id;
    private String name;

    private ArrayList<Pet> petsList;

    //private String email;
    //private String password;


    //Getter methods:
    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Pet> getPetsList() {
        return petsList;
    }

}


