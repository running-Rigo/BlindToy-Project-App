package com.example.blindtoy_projekt_b.Repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeneralUserRepository {
    //static variables
    private static final String TAG = "GeneralUserRepository";
    private static GeneralUserRepository myUserRepository;

    //instance variables
    private Application application;
    public LiveData<String> repoErrorMessage;
    private MutableLiveData<String> mutableRepoErrorMessage = new MutableLiveData<>();
    public LiveData<Boolean> userIsLoggedIn;
    private MutableLiveData<Boolean> mutableUserIsLoggedIn = new MutableLiveData<>();

    private GeneralUserRepository(Application application){
        this.application = application;
        //Server-Api is implemented via retrofit:
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://blinddog.uber.space/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        setUserIsLoggedIn(false);
        setRepoErrorMessage("");
        /*
        this.serverAPI = retrofit.create(ServerAPI.class); //Retrofit implements the interface methods
        userDatabase = UserDatabase.getInstance(application);
        queryUserFromRooms();

         */
    }


    //Get Instance Method
    public static GeneralUserRepository getInstance(Application application){
        if(myUserRepository == null){
            myUserRepository = new GeneralUserRepository(application);
        }
        return myUserRepository;
    }

    //region Methods for LiveData repoErrorMessage
    private void setRepoErrorMessage(String errorString){
        mutableRepoErrorMessage.setValue(errorString);
        this.repoErrorMessage = mutableRepoErrorMessage;
    }
    //endregion
    //region Methods for LiveData userIsLoggedIn
    private void setUserIsLoggedIn(Boolean isLoggedIn) {
        mutableUserIsLoggedIn.setValue(isLoggedIn);
        this.userIsLoggedIn = mutableUserIsLoggedIn;
    }
    //endregion


    //region Methods for operations with external Database
    public void registerNewUserInDb(String username, String email, String password) {
        Log.d(TAG,"Method registerUserInDb aufgerufen");
    }
    public void checkCredentialsInDb(String email, String password){
        setUserIsLoggedIn(true);
    }
    //endregion
}


