package com.example.harish.geomindr.activity.ebr;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {

    String googlePlacesData = null;

    String Entity,nameEntity;
    Context data;  //passing context from the service
    @Override
    protected String doInBackground(Object... inputObj) {
        try {
            String googlePlacesUrl = (String) inputObj[0];
            Entity = (String) inputObj[1];
            nameEntity = (String) inputObj[2];
            data = (Context) inputObj[3];
//            Log.d("forNoti",googlePlacesUrl+" googleplacesreadtask");
            Http http = new Http();
            googlePlacesData = http.read(googlePlacesUrl);
        } catch (Exception e) {
            Log.d("Google Place Read Task", e.toString());
        }
        Log.d("Locationssss",googlePlacesData);
        return googlePlacesData;
    }


    @Override
    protected void onPostExecute(String result) {
        PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
        Object[] toPass = new Object[4];
        toPass[0] = result;
        toPass[1] = Entity;
        toPass[2] = nameEntity;
        toPass[3] = data;
        placesDisplayTask.execute(toPass);
    }
}

