package com.example.blindtoy_projekt_b.Repositories;

import android.app.Application;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.blindtoy_projekt_b.Entities.Pet;
import com.example.blindtoy_projekt_b.Data.LocalRoomsData.UserDatabase;
import com.example.blindtoy_projekt_b.Data.ServerData.ServerAPI;
import com.example.blindtoy_projekt_b.Entities.Credentials;
import com.example.blindtoy_projekt_b.Entities.ServerDbUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetsRepository {
//region *Attributes

    //static attributes
    private static final String TAG = "L_PetsRepository";
    private static PetsRepository myPetsRepository;

    //instance attributes
    private Application application;
    private ServerAPI serverAPI;
    private UserDatabase userDatabase;
    Credentials userDetails; //is package-private because it's set by the GeneralUserRepository


    //Repo-Updates for viewmodels
    private MutableLiveData<String> mutableRepoErrorMessage = new MutableLiveData<>();
    public LiveData<String> repoErrorMessage; // not yet observed

    MutableLiveData<String> mutableAsyncStatusUpdate = new MutableLiveData<>();
    public LiveData<String> asyncStatusUpdate; // not yet observed

    //Pets-data for viewmodels
    private MutableLiveData<ArrayList<Pet>> mutableUserPetsList = new MutableLiveData<>();
    public LiveData<ArrayList<Pet>> userPetsList;

    private Pet oneChosenPet;

//endregion

//region *Constructor & Co

    private PetsRepository(Application application){
        this.application = application;
        //Server-Api is implemented via retrofit:
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://blinddog.uber.space/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        setRepoErrorMessage("");
        setAsyncStatusUpdate("");
        setUserPetsList(new ArrayList<Pet>());
        this.serverAPI = retrofit.create(ServerAPI.class); //Retrofit implements the interface methods
        userDatabase = UserDatabase.getInstance(application);
    }

    //Get Instance Method
    public static PetsRepository getInstance(Application application){
        if(myPetsRepository == null){
            myPetsRepository = new PetsRepository(application);
        }
        return myPetsRepository;
    }

    public void setOneChosenPet(int index){
        ArrayList<Pet> petsList = mutableUserPetsList.getValue();
        if(petsList.size() > 0){
            oneChosenPet = petsList.get(index);
        }
        else{
            oneChosenPet = null;
        }

    }
    public Pet getOneChosenPet(){
        return this.oneChosenPet;
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

    void setUserPetsList(ArrayList<Pet> list){ //case 1: login - new petslist is saved
        mutableUserPetsList.setValue(list);
        userPetsList = mutableUserPetsList;
        Log.d(TAG,"put pets from server into userpetslist");
    }

    void setUserPetsList(Pet newPet){ //case 2: addPet - a single pet is added
        ArrayList<Pet> localList = mutableUserPetsList.getValue();
        localList.add(newPet);
        mutableUserPetsList.setValue(localList);
        userPetsList = mutableUserPetsList;
        Log.d(TAG,"put new pet from server into userpetslist");
    }

    private ArrayList<Pet> getUserPetsList(){
        ArrayList<Pet> localList = mutableUserPetsList.getValue();
        return localList;
    }

//endregion

//region *New Pet (External Sever)
    public void saveNewPet(String name, String species, String sounds){
        Call call = serverAPI.saveNewPet(name,species,sounds,userDetails.getApiToken(), userDetails.getUserId());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(!response.isSuccessful()){ //this if is important because the result object would be null if eg. there is error 404
                    Log.e(TAG,"Could access server but didn't receive result.");
                    return;
                }
                String responsePetId = response.body().toString();
                Log.d(TAG,responsePetId);
                try{
                    int petId = Integer.parseInt(responsePetId);
                    Pet newPet = new Pet(petId,name,species); //soundSettings are set to default
                    setUserPetsList(newPet); //add to userpetslist livedata
                    setAsyncStatusUpdate("newPetSuccessful");
                }
                catch (NumberFormatException ex){
                    setRepoErrorMessage(responsePetId);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                setRepoErrorMessage(t.toString());
            }
        });
    }
//endregion

// region *Delete one Pet

    public void deletePet(){
        Log.d(TAG,"method deletePet called");
        //Delete from server DB:
        deletePetFromServerDb();
    }

    private void deletePetFromServerDb(){
        Call call = serverAPI.deleteOnePet(String.valueOf(oneChosenPet.pet_id),userDetails.getApiToken(), userDetails.getUserId());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(!response.isSuccessful()){ //this if is important because the result object would be null if eg. there is error 404
                    Log.e(TAG,"Could access server but didn't receive result.");
                    return;
                }
                String responseString = response.body().toString();
                if(responseString.equals("success")){
                    deletePetLocally();
                    Log.d(TAG,"Got message 'success' from server");
                }
                else{
                    setRepoErrorMessage(responseString);
                    Log.e("TAG", responseString);
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                setRepoErrorMessage(t.toString());
                Log.e("TAG", t.toString());
            }
        });
    }

    private void deletePetLocally(){
        //Delete from local PetsList;
        ArrayList<Pet> petsList = getUserPetsList();
        int deleteId = oneChosenPet.pet_id;
        int deleteIndex = -1;
        for(int i = 0; i < petsList.size(); i++){
            if(petsList.get(i).pet_id == deleteId){
                deleteIndex = i;
                Log.d(TAG, "DeleteIndex " + deleteIndex);
                i = petsList.size();
            }
        }
        try{
            petsList.remove(deleteIndex);
            oneChosenPet = null;
            Log.d(TAG,"localdelete successful");
            setAsyncStatusUpdate("petDeletedSuccessful");
        }
        catch (IndexOutOfBoundsException ex){
            Log.d(TAG,"ChosenPet couldn't be deleted from local list.");
        }
        setAsyncStatusUpdate("");
    }

//endregion

}
