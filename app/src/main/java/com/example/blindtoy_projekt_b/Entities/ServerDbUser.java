package com.example.blindtoy_projekt_b.Entities;

import com.example.blindtoy_projekt_b.Data.LocalRoomsData.Pet;
import com.example.blindtoy_projekt_b.Data.LocalRoomsData.Pet;

import java.util.ArrayList;
import java.util.List;

public class ServerDbUser {
    private String user_id;
    private String name;
    private ArrayList<Pet> petsList;

    //Getter methods:
    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public List<Pet> getPetsList() {
        return petsList;
    }

    @Override
    public String toString() {
        return "ServerDbUser{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", petsList=" + petsList +
                '}';
    }
}


