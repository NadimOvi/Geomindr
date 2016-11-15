package com.example.harish.geomindr.service.tbr.facebook;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

public class FacebookConfirmService extends Service {
    String msg;
    String latitude;
    String longitude;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        msg = intent.getStringExtra("msg");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        // post to facebook
        doPost();
        return START_STICKY;
    }

    public void doPost() {
        // make the API call
        // search for facebook page corresponding to user's location
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/search",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // found a place corresponding to user's latitude and longitude on facebook
                        boolean foundPlace = false;
                        // id of the page of the place on facebook
                        String placeID = null;

                        if (response.getError() == null) {
                            if (response.getJSONObject() != null) {
                                try {
                                    Log.d("place", response.getJSONObject().getJSONArray("data").getJSONObject(0).getString("id"));
                                    // placeID is the page 'id' of the first page in the response sent by the facebook for our search query
                                    placeID = "110819562407036";
                                    //response.getJSONObject().getJSONArray("data").getJSONObject(1).getString("id");
                                    //Log.d("place", response.getJSONObject().getJSONArray("data").toString());
                                    foundPlace = true;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        // bundle object to send request parameters to Graph API
                        Bundle params = new Bundle();

                        // generate custom (comma separated) address
                        // setting the message of the status
                        // this will be posted to user's facebook wall
                        params.putString("message", msg);
                        //Log.d("FBmessage", msg);

                        // if, found a place corresponding to user's latitude and longitude on facebook
                        // then, set the place field in the post
                        if (foundPlace) {
                            params.putString("place", placeID);
                        }

                        // make the API call, i.e, post the status to user's facebook wall
                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me/feed",
                                params,
                                HttpMethod.POST,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        Toast.makeText(FacebookConfirmService.this, "Posted", Toast.LENGTH_SHORT).show();
                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.cancel(1);
                                    }
                                }
                        ).executeAsync();
                    }
                });

        // bundle object to send request parameters to Graph API for searching the facebook page corresponding to user's location
        Bundle parameters = new Bundle();
        // type of search
        parameters.putString("type", "place");
        // coordinates corresponding to the place to be searched
        parameters.putString("center", String.valueOf(latitude) + "," + String.valueOf(longitude));
        // search for facebook pages within 100 metres of user's location
        parameters.putString("distance", "500");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
