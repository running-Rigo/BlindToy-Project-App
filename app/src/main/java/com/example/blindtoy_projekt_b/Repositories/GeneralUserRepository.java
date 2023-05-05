package com.example.blindtoy_projekt_b.Repositories;

import android.app.Application;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.blindtoy_projekt_b.Repositories.Entities.Pet;
import com.example.blindtoy_projekt_b.Data.LocalRoomsData.User;
import com.example.blindtoy_projekt_b.Data.LocalRoomsData.UserDatabase;
import com.example.blindtoy_projekt_b.Data.ServerData.ServerAPI;
import com.example.blindtoy_projekt_b.Repositories.Entities.Credentials;
import com.example.blindtoy_projekt_b.Repositories.Entities.ServerDbUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeneralUserRepository {
//region *Attributes
    //static attributes
    private static final String TAG = "L_GeneralUserRepository";
    private static GeneralUserRepository myUserRepository;

    //instance attributes
    private Application application;
    private ServerAPI serverAPI;
    private UserDatabase userDatabase;
    private PetsRepository petsRepository;

    Credentials userDetails;

    //repo-updates for viewmodels
    private MutableLiveData<String> mutableRepoErrorMessage = new MutableLiveData<>();
    public LiveData<String> repoErrorMessage;

    private MutableLiveData<String> mutableAsyncStatusUpdate = new MutableLiveData<>();
    public LiveData<String> asyncStatusUpdate;
//endregion

//region *Constructor & Co
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
        petsRepository = PetsRepository.getInstance(application);
        //At appstart when userrepo is instantiated:
        queryUserFromRooms();
    }

    //Get Instance Method
    public static GeneralUserRepository getInstance(Application application){
        if(myUserRepository == null){
            myUserRepository = new GeneralUserRepository(application);
        }
        return myUserRepository;
    }

//endregion

//region *LiveData Setting
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
                    setRepoErrorMessage("");
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                setRepoErrorMessage(t.toString());
            }
        });
    }
//endregion

//region *Querying known User from Rooms
    public void queryUserFromRooms() {
        setAsyncStatusUpdate("isCheckingUser");
        Log.d(TAG, "methode queryUserFromRooms aufgerufen");
        QueryUserAsyncTask queryUserAsyncTask = new QueryUserAsyncTask();
        queryUserAsyncTask.execute();
        //if queryUserAsyncTask results in a valid resultUser-Object, a request to the server is sent with api-token and user_id; server returns DbUser-Object
    }

    private class QueryUserAsyncTask extends AsyncTask<Void,Void,Credentials>{
        @Override
        protected Credentials doInBackground(Void... voids) {
            List<User> knownUsers = null;
            User knownUser = new User();
            try{
                knownUsers = UserDatabase.getInstance(application).userDao().getAll();
            }
            catch (SQLiteException ex){
                setRepoErrorMessage(ex.toString());
            }

            if(knownUsers.size()==1){
                knownUser = knownUsers.get(0);
                Log.d(TAG,"angemeldeter User gefunden: " + knownUser.toString()); //print to console for testing
            }
            else if(knownUsers.size()>1){
                knownUser.userID = "invalid";
                Log.d(TAG,"Mehrere User in Rooms gefunden" );
            }
            else{
                Log.d(TAG,"keine User in Rooms gefunden" ); //print to console for testing
            }
            Credentials userData = new Credentials(knownUser.getApiKey(), knownUser.getUserID());
            Log.d(TAG, userData.toString());
            return userData;
        }

        @Override
        protected void onPostExecute(Credentials userData){
                if(userData.getUserId() == "invalid"){
                    setRepoErrorMessage("Invalid state of local userdata in Rooms - logged out.");
                    setAsyncStatusUpdate("noUserFound");
                    logoutUser();
                }
                else if(userData.getUserId() == null){
                    Log.d(TAG, "kein User gespeichert");
                    setAsyncStatusUpdate("noUserFound");
                }
                else{ // user-object with normal valid data found:
                    userDetails = userData; //Token and User_id are stored in userRepo.
                    petsRepository.userDetails = userData; //Token and User_id are now available in PetsRepo
                    getUserDataFromServer();
                }
        }
    }
//endregion

//region *Usertoken found - query other userdata from server
    private void getUserDataFromServer(){
        Call<ServerDbUser> call = serverAPI.checkUserToken(userDetails.getApiToken(), userDetails.getUserId());
        call.enqueue(new Callback<ServerDbUser>() {
            @Override
            public void onResponse(Call<ServerDbUser> call, Response<ServerDbUser> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG,"Could access server but didn't receive result.");
                    setRepoErrorMessage("Problem beim Abfragen des Users vom Server.");
                    return;
                }
                ServerDbUser loginResultUser = response.body();
                if(loginResultUser != null){
                    Log.d(TAG, "succes, got user-details from server: "+ loginResultUser.toString());
                    //now the newly checked user has to be saved to the local rooms database only with username & id;
                    setAsyncStatusUpdate("loginSuccessful");
                    saveUserToRoomsAsync(loginResultUser); //replace old user in rooms by new user (probably the same, but maybe username changed
                    if(loginResultUser.getPetsList().size()>0) {
                        petsRepository.setUserPetsList((ArrayList<Pet>) loginResultUser.getPetsList());
                    }
                }
                else{
                    Log.d(TAG, "responseUser ist null");
                    setRepoErrorMessage("Anmeldung fehlgeschlagen - Token ungültig");
                }
            }

            @Override
            public void onFailure(Call<ServerDbUser> call, Throwable t) {
                setRepoErrorMessage(t.toString());
            }
        });
    }
//endregion

//region *Login (1.validation of credentials by external server | 2.saving returned ServerDBUser Object as User to Rooms | 3.if petsList > 0, petsRepository.savePetsToRoomsAsync(loginResultUser) is called

    //1.login validation with external server
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

                ServerDbUser loginResultUser = response.body();
                if(loginResultUser != null){
                    Log.d(TAG, "succes, got user-details from server: "+ loginResultUser.toString());
                    //now the newly checked user has to be saved to the local rooms database only with username & id;
                    setAsyncStatusUpdate("loginSuccessful");
                    saveUserToRoomsAsync(loginResultUser);
                    if(loginResultUser.getPetsList().size()>0) {
                        petsRepository.setUserPetsList((ArrayList<Pet>) loginResultUser.getPetsList());
                    }

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
    //2.save userData from external DB to rooms
    public void saveUserToRoomsAsync(ServerDbUser loginResultUser){
        Log.d(TAG, "Methode saveUserToRooms aufgerufen");
        User newUser = new User();
        newUser.userName = loginResultUser.getName();
        newUser.userID = loginResultUser.getUser_id();
        newUser.apiKey = loginResultUser.getApiKey();
        SaveUserAsyncTask saveUserAsyncTask = new SaveUserAsyncTask();
        saveUserAsyncTask.execute(newUser);
    }
    private class SaveUserAsyncTask extends AsyncTask<User,Void,String> {
        @Override
        protected String doInBackground(User...user) {
            String result;
            try{
                userDatabase.userDao().insertValidUser(user);
                Log.d(TAG, "In Rooms gespeichert:" + userDatabase.userDao().getAll().get(0).toString());
                result = "successful";
            }
            catch(SQLiteException ex){
                result = ex.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            if(!result.equals("successful")){
                setRepoErrorMessage("User konnte nicht lokal gespeichert werden: "+ result);
            }
            else{
                Log.d(TAG,"user logged in and saved to rooms for staying logged in.");
            }
        }
    }

//endregion

//region *Logout
    public void logoutUser(){
        petsRepository.setUserPetsList(new ArrayList<Pet>());
        petsRepository.setOneChosenPet(0);
        DeleteAsyncTask deleteAsyncTask = new DeleteAsyncTask();
        deleteAsyncTask.execute();
    }
    class DeleteAsyncTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void...voids) {
            try{
                userDatabase.clearAllTables();
                Log.d(TAG,"LogoutUser() - Rooms tables cleared");
                return "success";
            }
            catch (SQLiteException e){
                Log.d(TAG,"LogoutUser() - tables couldnt be cleared");
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result){
            if(result.equals("success")){
                setAsyncStatusUpdate("logoutSuccessful");
                Log.d(TAG,"backgroundtask finished");
            }
            else{
                setRepoErrorMessage(result);
            }
        }
    }
    //endregion
}


