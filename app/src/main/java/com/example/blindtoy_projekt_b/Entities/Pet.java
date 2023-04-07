package com.example.blindtoy_projekt_b.Entities;

public class Pet {

    public int pet_id;

    public String name;
    public String species;
    public String sounds;

    public Pet(int pet_id, String name, String species) { //Constructor with values is only used when new Pet is created by user, and in this case the sound settings are just set to default values and not given to the constructor
        this.pet_id = pet_id;
        this.name = name;
        this.species = species;
        sounds = "1,2,2,2,1"; //soundSettings are for a new pet set to default values (file1,file2,mediumBeepingSpeed,mediumTonePitch,single*beep*)
    }

    @Override
    public String toString() {
        return "Pet{" +
                "pet_id=" + pet_id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", soundSettings='" + sounds + '\'' +
                '}';
    }
}
