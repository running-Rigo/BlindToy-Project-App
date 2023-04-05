package com.example.blindtoy_projekt_b.Data.LocalRoomsData;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PetsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPets(List<Pet> petsList);

    @Query("Select * from pet")
    List<Pet> getAll();
}
