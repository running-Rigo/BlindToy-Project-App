package com.example.blindtoy_projekt_b.Data.LocalRoomsData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertValidUser(User... users);

/*
    @Insert
    void insertAll(User...users);

    @Update
    void updateUser(User user);

     */

    @Query("Select * from user")
    List<User> getAll();

    @Delete
    void delete(User user);
}
