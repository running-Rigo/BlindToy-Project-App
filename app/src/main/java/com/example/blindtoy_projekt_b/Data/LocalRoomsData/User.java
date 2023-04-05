package com.example.blindtoy_projekt_b.Data.LocalRoomsData;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class User {
    @PrimaryKey @NotNull
    public String userID;
    public String apiKey;
    public String userName;

/*
    public User(@NotNull String userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

 */

    @NotNull
    public String getUserID() {
        return userID;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
