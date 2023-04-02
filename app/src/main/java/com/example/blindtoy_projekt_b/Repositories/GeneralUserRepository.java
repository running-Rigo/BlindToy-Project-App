package com.example.blindtoy_projekt_b.Repositories;

import android.app.Application;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.blindtoy_projekt_b.Data.LocalRoomsData.Pet;
import com.example.blindtoy_projekt_b.Data.LocalRoomsData.User;
import com.example.blindtoy_projekt_b.Data.LocalRoomsData.UserDatabase;
import com.example.blindtoy_projekt_b.Data.ServerData.ServerAPI;
import com.example.blindtoy_projekt_b.Entities.ServerDbUser;

import java.util.ArrayList;
import java.util.List;

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

    private MutableLiveData<String> mutableAsyncStatusUpdate = new MutableLiveData<>();
    public LiveData<String> asyncStatusUpdate;

    private MutableLiveData<ArrayList<Pet>> mutableUserPetsList = new MutableLiveData<>();
    public LiveData<ArrayList<Pet>> userPetsList;

    //endregion

    private GeneralUserRepository(Application application){
        this.application = application;
        //Server-Api is implemented via retrofit:
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://blinddog.uber.space/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        setRepoErrorMessage("");
        setAsyncStatusUpdate("");
        setUserPetsList(null);
        this.serverAPI = retrofit.create(ServerAPI.class); //Retrofit implements the interface methods
        userDatabase = UserDatabase.getInstance(application);
        //At appstart when userrepo is instantiated:
        checkForKnownUser();
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

    private void setAsyncStatusUpdate(String status){
        mutableAsyncStatusUpdate.setValue(status);
        asyncStatusUpdate = mutableAsyncStatusUpdate;
    }

    private void setUserPetsList(ArrayList<Pet> list){
        mutableUserPetsList.setValue(list);
        userPetsList = mutableUserPetsList;
    }
//endregion

//region Loading KnownUser
    public void checkForKnownUser(){
        Log.d(TAG, "methode checkforknownusers aufgerufen");
        queryUserFromRooms();
        //if a User is saved in Rooms users Table, then queryPetsFromRooms is called
    }

    public void queryUserFromRooms() {
        QueryUserAsyncTask queryUserAsyncTask = new QueryUserAsyncTask();
        queryUserAsyncTask.execute();
        //if queryUserAsyncTask results in a valid resultUser-Object, pets are automatically queried from Rooms and saved to LiveData userPetsList
    }

    private class QueryUserAsyncTask extends AsyncTask<Void,Void,User>{
        @Override
        protected User doInBackground(Void... voids) {
            List<User> knownUsers = null;
            User knownUser = null;
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
                knownUser = new User();
                knownUser.userID = "invalid";
                Log.d(TAG,"Mehrere User in Rooms gefunden" );
            }
            else{
                Log.d(TAG,"keine User in Rooms gefunden" ); //print to console for testing
            }
            return knownUser;
        }

        @Override
        protected void onPostExecute(User knownUser){
            if(knownUser != null){
                if(knownUser.userID.equals("invalid")){
                    setRepoErrorMessage("Invalid state of local userdata in Rooms - logged out.");
                    logoutUser();
                }
                else{
                    setAsyncStatusUpdate("loginSuccessful");
                    queryPetsFromRooms(knownUser);
                }
            }
        }
    }

    //Query Pets is only Called when a knownUser-Object was created from Rooms-Data:
    public void queryPetsFromRooms(User knownUser){
        Log.d(TAG, "methode queryPetsFromRooms aufgerufen");
        QueryPetsAsyncTask queryPetsAsyncTask = new QueryPetsAsyncTask();
        queryPetsAsyncTask.execute();
    }
    class QueryPetsAsyncTask extends AsyncTask<Void,Void,ArrayList<Pet>> {
        @Override
        protected ArrayList<Pet> doInBackground(Void... voids) {
            try{
                ArrayList<Pet> knownPets = (ArrayList<Pet>) UserDatabase.getInstance(application).petsDao().getAll();
                if(knownPets.size()>0){
                    for(int i = 0; i < knownPets.size(); i++){
                        Log.d(TAG,"knownPets gefunden: " + knownPets.get(i).toString()); //print to console for testing
                    }
                }
                else{
                    Log.d(TAG,"knownPets: scheinbar keine Pets in Rooms-DB..." ); //print to console for testing
                }
                return knownPets;
            }
            catch (SQLiteException e){
                return null;
            }
        }
        @Override
        protected void onPostExecute(ArrayList<Pet> knownPets){
            setUserPetsList(knownPets);
        }
    }
//endregion

//region Registration
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

//region Login
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
                    savePetsToRoomsAsync(loginResultUser);
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
        }
    }

    //3.save pets from external DB to rooms
    public void savePetsToRoomsAsync(ServerDbUser loginResultUser){
        Log.d(TAG, "Methode saveUserToRooms aufgerufen");
        SavePetsAsyncTask savePetsAsyncTask = new SavePetsAsyncTask();
        savePetsAsyncTask.execute(loginResultUser.getPetsList());
    }
    private class SavePetsAsyncTask extends AsyncTask<List<Pet>,Void,String> {
        @Override
        protected String doInBackground(List<Pet>...petsList) {
            String result;
            try{
                userDatabase.petsDao().insertAllPets(petsList[0]);
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
                setRepoErrorMessage("Haustiere konnte nicht lokal gespeichert werden: "+ result);
            }
        }
    }
//endregion

//region Logout
    public void logoutUser(){
        setUserPetsList(null);
        DeleteAsyncTask deleteAsyncTask = new DeleteAsyncTask();
        deleteAsyncTask.execute();
    }
    class DeleteAsyncTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void...voids) {
            try{
                userDatabase.clearAllTables();
                return "success";
            }
            catch (SQLiteException e){
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result){
            if(result.equals("success")){
                setAsyncStatusUpdate("logoutSuccessful");
                Log.d("userrepository","backgroundtask finished");
            }
            else{
                setRepoErrorMessage(result);
            }
        }
    }
    //endregion
}


