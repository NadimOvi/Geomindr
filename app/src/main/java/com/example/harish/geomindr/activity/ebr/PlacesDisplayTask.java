package com.example.harish.geomindr.activity.ebr;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.example.harish.geomindr.database.DatabaseHelper;
import com.example.harish.geomindr.service.main.ReminderService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

    JSONObject googlePlacesJson;

    DatabaseHelper mydb;

    String Entity,nameEntity;
    Context data;  //passing context from service

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            googlePlacesJson = new JSONObject((String) inputObj[0]);
            Entity = (String) inputObj[1];
            nameEntity = (String) inputObj[2];
            data = (Context) inputObj[3];
            mydb = DatabaseHelper.getInstance(data);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }

        if(googlePlacesList.size()==0)
        {        }

        //passing entity values to service
        else
        {
            // when user enters particular entity
            if(!nameEntity.equals("")){
                for (int i = 0; i < googlePlacesList.size(); i++) {
                    HashMap<String, String> googlePlace = googlePlacesList.get(i);
                    String lower = googlePlace.get("place_name").toLowerCase();
                    if (lower.contains(nameEntity.toLowerCase())) {

                        Boolean isUpdatelat = mydb.updatelat(Entity, googlePlace.get("lat"));
                        if (!isUpdatelat)
                            Log.d("Notifications", "Not Updated");
                        Boolean isUpdatelong = mydb.updatelng(Entity, googlePlace.get("lng"));
                        if (!isUpdatelong)
                            Log.d("Notifications", "Not Updated");
                        Boolean isUpdate1 = mydb.updateName(Entity, googlePlace.get("place_name"));
                        if (!isUpdate1)
                            Log.d("Notifications", "Not Updated");
                        break;
                    }
                }
            }
            // when user want any nearest entity
            else {
                String place = null;
                float distance = 20000;
                for (int i = 0; i < googlePlacesList.size(); i++) {
                    HashMap<String, String> googlePlace = googlePlacesList.get(i);
                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));
                    Location loc1 = new Location("");
                    loc1.setLatitude(lat);
                    loc1.setLongitude(lng);
                    Location loc2 = new Location("");
                    loc2.setLatitude(ReminderService.lastLocation.getLatitude());
                    loc2.setLongitude(ReminderService.lastLocation.getLongitude());
                    if (loc1.distanceTo(loc2) < distance) {
                        distance = loc1.distanceTo(loc2);
                        place = googlePlace.get("place_name");
                    }
                }

                for (int i = 0; i < googlePlacesList.size(); i++) {
                    HashMap<String, String> googlePlace = googlePlacesList.get(i);
                    if (place.equals(googlePlace.get("place_name"))) {

                        Boolean isUpdatelat = mydb.updatelat(Entity, googlePlace.get("lat"));
                        if (!isUpdatelat)
                            Log.d("Notifications", "Not Updated");
                        Boolean isUpdatelong = mydb.updatelng(Entity, googlePlace.get("lng"));
                        if (!isUpdatelong)
                            Log.d("Notifications", "Not Updated");
                        Boolean isUpdate1 = mydb.updateName(Entity, place);
                        if (!isUpdate1)
                            Log.d("Notifications", "Not Updated");
                        break;
                    }
                }
            }
        }

        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {

    }
}




