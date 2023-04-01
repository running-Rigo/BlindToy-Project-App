package com.example.blindtoy_projekt_b.Data.LocalRoomsData;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class,Pet.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase{ //Singleton pattern!
    public abstract UserDao userDao();
    public abstract PetsDao petsDao();
    public static volatile UserDatabase INSTANCE;

    public static UserDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (UserDatabase.class){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),UserDatabase.class, "UserData").build();
            }
        }
        return INSTANCE;
    }
}