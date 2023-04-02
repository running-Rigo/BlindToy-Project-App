package com.example.blindtoy_projekt_b.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.blindtoy_projekt_b.Data.LocalRoomsData.User;
import com.example.blindtoy_projekt_b.Data.LocalRoomsData.UserDatabase;
import com.example.blindtoy_projekt_b.Data.ServerData.ServerAPI;
import com.example.blindtoy_projekt_b.Entities.ServerDbUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeneralUserRepository {
    //static variables
    private static final String TAG = "GeneralUserRepository";
    private static GeneralUserRepository myUserRepository;

    //instance variables
    private Application application;
    private ServerAPI serverAPI;
    private UserDatabase userDatabase;
    private ServerDbUser loginResultUser;


    //region LiveData Variables - always a private mutable version and the public immutable one
    private MutableLiveData<String> mutableRepoErrorMessage = new MutableLiveData<>();
    public LiveData<String> repoErrorMessage;

    private MutableLiveData<String> mutableAsyncStatusUpdate = new MutableLiveData<>();
    public LiveData<String> asyncStatusUpdate;

    //endregion

    private GeneralUserRepository(Application application){
        this.application = application;
        //Server-Api is implemented via retrofit:
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://blinddog.uber.space/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        setRepoErrorMessage("");
        setAsyncStatusUpdate("");
        this.serverAPI = retrofit.create(ServerAPI.class); //Retrofit implements the interface methods
        userDatabase = UserDatabase.getInstance(application);
        //queryUserFromRooms();
    }


    //Get Instance Method
    public static GeneralUserRepository getInstance(Application application){
        if(myUserRepository == null){
            myUserRepository = new GeneralUserRepository(application);
        }
        return myUserRepository;
    }

    //region *Methods for LiveData setting
    private void setRepoErrorMessage(String errorString){
        mutableRepoErrorMessage.setValue(errorString);
        this.repoErrorMessage = mutableRepoErrorMessage;
    }

    private void setAsyncStatusUpdate(String status){
        mutableAsyncStatusUpdate.setValue(status);
        asyncStatusUpdate = mutableAsyncStatusUpdate;
    }
    //endregion

    //region *Registration of new User in external DB

    public void registerNewUserInDb(String username, String email, String password){
        Call call = serverAPI.registerNewUser(username,email,password);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(!response.isSuccessful()){ //this if is important because the result object would be null if eg. there is error 404
                    Log.e(TAG,"Could access server but didn't receive result.");
                    return;
                }
                String registrationResponse = response.body().toString();
                Log.d(TAG,registrationResponse);
                if(registrationResponse.toLowerCase().equals("success")){
                    setAsyncStatusUpdate("registrationSuccessful");
                }
                else{
                    setRepoErrorMessage(registrationResponse);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                setRepoErrorMessage(t.toString());
            }
        });
    }

    //endregion

    //region *login validation with external server
    public void checkCredentialsInDb(String email, String password){
        Call<ServerDbUser> call = serverAPI.checkUserLogin(email,password);
        call.enqueue(new Callback<ServerDbUser>() {
            @Override
            public void onResponse(Call<ServerDbUser> call, Response<ServerDbUser> response) {
                if(!response.isSuccessful()){ //this if is important because the result object would be null if eg. there is error 404
                    Log.e(TAG,"Could access server but didn't receive result.");
                    setRepoErrorMessage("Problem beim Abfragen des Users vom Server.");
                    return;
                }
                loginResultUser = response.body();
                if(loginResultUser != null){
                    Log.d(TAG, "succes, got user-details from server: "+ loginResultUser.toString());
                    //now the newly checked user has to be saved to the local rooms database only with username & id;
                    //saveUserToRooms();
                    //savePetsToRooms();
                    setAsyncStatusUpdate("loginSuccessful");
                }
                else{
                    Log.d(TAG, "responseUser ist null");
                    setRepoErrorMessage("Anmeldung fehlgeschlagen - Email oder Passwort falsch");
                }
            }
            @Override
            public void onFailure(Call<ServerDbUser> call, Throwable t) {
                setRepoErrorMessage(t.toString());
            }
        });
    }
    //endregion
}


