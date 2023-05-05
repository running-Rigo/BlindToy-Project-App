package com.example.blindtoy_projekt_b.Repositories.Entities;

import java.util.ArrayList;
import java.util.List;

public class ServerDbUser {
    private String user_id;
    private String name;
    private ArrayList<Pet> petsList;
    private String apiKey;

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

    public String getApiKey() {
        return apiKey;
    }

    public ServerDbUser(ArrayList<Pet> petsList) {
        this.petsList = petsList;
    }

    @Override
    public String toString() {
        return "ServerDbUser{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", petsList=" + petsList +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}


