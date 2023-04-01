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


    //region LiveData Variables - always a private mutable version and the public immutable one
    private MutableLiveData<String> mutableRepoErrorMessage = new MutableLiveData<>();
    public LiveData<String> repoErrorMessage;
    private MutableLiveData<Boolean> mutableUserIsLoggedIn = new MutableLiveData<>();
    public LiveData<Boolean> userIsLoggedIn;

    private MutableLiveData<String> mutableAsyncStatusUpdate = new MutableLiveData<>();
    public LiveData<String> asyncStatusUpdate;

    //endregion

    private GeneralUserRepository(Application application){
        this.application = application;
        //Server-Api is implemented via retrofit:
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://blinddog.uber.space/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        setUserIsLoggedIn(false);
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

    //region Methods for LiveData setting
    private void setRepoErrorMessage(String errorString){
        mutableRepoErrorMessage.setValue(errorString);
        this.repoErrorMessage = mutableRepoErrorMessage;
    }

    private void setUserIsLoggedIn(Boolean isLoggedIn) {
        mutableUserIsLoggedIn.setValue(isLoggedIn);
        this.userIsLoggedIn = mutableUserIsLoggedIn;
    }

    private void setAsyncStatusUpdate(String status){
        mutableAsyncStatusUpdate.setValue(status);
        asyncStatusUpdate = mutableAsyncStatusUpdate;
    }
    //endregion


    //region Methods for operations with external Database

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


    public void checkCredentialsInDb(String email, String password){
        setUserIsLoggedIn(true);
    }
    //endregion


}


