package com.example.harish.geomindr.activity.ebr;

import android.content.Context;
import android.os.AsyncTask;

public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {

    private String googlePlacesData = null;
    private String Entity, nameEntity;
    // passing context from the service
    protected Context context;

    @Override
    protected String doInBackground(Object... inputObj) {
        try {
            String googlePlacesUrl = (String) inputObj[0];
            Entity = (String) inputObj[1];
            nameEntity = (String) inputObj[2];
            context = (Context) inputObj[3];
            Http http = new Http();
            googlePlacesData = http.read(googlePlacesUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
        Object[] toPass = new Object[4];
        toPass[0] = result;
        toPass[1] = Entity;
        toPass[2] = nameEntity;
        toPass[3] = context;
        placesDisplayTask.execute(toPass);
    }
}