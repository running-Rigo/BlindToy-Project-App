package com.example.blindtoy_projekt_b.Data.LocalRoomsData;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Pet {
    @PrimaryKey
    @NotNull
    public int pet_id;
    public String name;
    public String species;

    public Pet(int pet_id, String name, String species) {
        this.pet_id = pet_id;
        this.name = name;
        this.species = species;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "pet_id=" + pet_id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                '}';
    }
}
