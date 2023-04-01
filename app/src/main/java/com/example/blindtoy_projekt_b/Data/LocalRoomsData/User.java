package com.example.blindtoy_projekt_b.Data.LocalRoomsData;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class User {
    @PrimaryKey @NotNull
    public String userID;

    public String userName;

/*
    public User(@NotNull String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

 */


    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
