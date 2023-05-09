package com.example.blindtoy_projekt_b.Data.ServerData;
import com.example.blindtoy_projekt_b.Entities.ServerDbUser;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServerAPI {
    @FormUrlEncoded
    @POST("T5gV5gUmMA5sPh54scf.php") //checkUserLogin.php
    Call <ServerDbUser> checkUserLogin( //Method checkUserLogin checks if the username & password are within the database;
                                        @Field("email") String email,
                                        @Field("password") String password);

    @FormUrlEncoded
    @POST("T5gV5gUmMA5sPh54scf.php")
    Call <ServerDbUser> checkUserToken(
            @Field("token") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("6y2VV3dKMgZi6p7To6a.php")  //registerNewUser.php
    Call<String>  registerNewUser( //Posts new user credentials to the external database
                                        @Field("name") String username,
                                        @Field("email") String email,
                                        @Field("password") String password);

    @FormUrlEncoded
    @POST("St3t4t65ry7oky84vSR.php") //savePet.php
    Call<String>  saveNewPet(
            @Field("name") String name,
            @Field("species") String species,
            @Field("sounds") String sounds,
            @Field("token") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("Dz2vYU342pL88xVgw4M.php") //deletePet.php
    Call<String> deleteOnePet(
            @Field("pet_id") String petId,
            @Field("token") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("Ua2DyFuv7vt63wZ54Z4.php") //changePet.php
    Call<String> saveNewSounds(
            @Field("pet_id") String petId,
            @Field("sounds") String sounds,
            @Field("token") String token,
            @Field("user_id") String userId
    );
    }


    // authentification.php -> Ua2DyFuv7vt63wZ54Z3.php; db_settings_blinddog -> 9e7tz9666cEzTY5Fuay.php; db_script.php -> 7LKDo6vB8qVEF2y4yb2

