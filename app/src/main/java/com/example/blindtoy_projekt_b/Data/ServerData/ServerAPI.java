package com.example.blindtoy_projekt_b.Data.ServerData;

import com.example.blindtoy_projekt_b.Entities.ServerDbPet;
import com.example.blindtoy_projekt_b.Entities.ServerDbUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServerAPI {
    @FormUrlEncoded
    @POST("checkUserLogin.php")
    Call <ServerDbUser> checkUserLogin( //Method checkUserLogin checks if the username & password are within the database;
                                        @Field("email") String email,
                                        @Field("password") String password);

    @FormUrlEncoded
    @POST("checkUserLogin.php")
    Call <ServerDbUser> checkUserToken(
            @Field("token") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("registerNewUser.php")
    Call<String>  registerNewUser( //Posts new user credentials to the external database
                                        @Field("name") String username,
                                        @Field("email") String email,
                                        @Field("password") String password);

    @FormUrlEncoded
    @POST("savePet.php")
    Call<String>  saveNewPet(
            @Field("name") String name,
            @Field("species") String species,
            @Field("sounds") String sounds,
            @Field("token") String token,
            @Field("user_id") String userId
    );

    }
