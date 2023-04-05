package com.example.blindtoy_projekt_b.Entities;

public class ServerDbPet {

    public String name;
    public String species;
    public String token;
    public String user_id;

    public ServerDbPet(String name, String species, String ownerKey, String ownerId) {
        this.name = name;
        this.species = species;
        this.token = ownerKey;
        this.user_id = ownerId;
    }
}
