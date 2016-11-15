package com.example.harish.geomindr.service.tbr.facebook;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

// Service that gets called when user selects YES
// on the facebook task notification.
public class FacebookConfirmService extends Service {
    String msg;
    String latitude;
    String longitude;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        msg = intent.getStringExtra("msg");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        // Post to facebook.
        doPost();
        return START_STICKY;
    }

    public void doPost() {
        // Make the API call.
        // Search for facebook page corresponding to user's location.
        GraphRequest request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/search",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Found a place corresponding to user's latitude and longitude on facebook.
                        boolean foundPlace = false;
                        // ID of the page of the place on facebook.
                        String placeID = null;

                        if (response.getError() == null) {
                            if (response.getJSONObject() != null) {
                                try {
                                    /*Log.d("place", response.getJSONObject().getJSONArray("data")
                                            .getJSONObject(0).getString("id"));*/
                                    // placeID is the page 'id' of the first page in the
                                    // response sent by the facebook for our search query
                                    placeID = response.getJSONObject().getJSONArray("data")
                                            .getJSONObject(0).getString("id");
                                    //Log.d("place", response.getJSONObject().getJSONArray("data").toString());
                                    foundPlace = true;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        // Bundle object to send request parameters to Graph API.
                        Bundle params = new Bundle();

                        // Generate custom (comma separated) address.
                        // Setting the message of the status.
                        // This will be posted to user's facebook wall.
                        params.putString("message", msg);

                        // If, found a place corresponding to user's latitude and longitude on facebook
                        // then, set the place field in the post.
                        if (foundPlace) {
                            params.putString("place", placeID);
                        }

                        // Make the API call, i.e, post the status to user's facebook wall.
                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me/feed",
                                params,
                                HttpMethod.POST,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                        Toast.makeText(FacebookConfirmService.this, "Posted",
                                                Toast.LENGTH_SHORT).show();
                                        NotificationManager notificationManager = (NotificationManager)
                                                getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.cancel(1);
                                    }
                                }
                        ).executeAsync();
                    }
                });

        // Bundle object to send request parameters to Graph API for
        // searching the facebook page corresponding to user's location.
        Bundle parameters = new Bundle();
        // Type of search.
        parameters.putString("type", "place");
        // Coordinates corresponding to the place to be searched.
        parameters.putString("center", String.valueOf(latitude) + "," + String.valueOf(longitude));
        // Search for facebook pages within 100 metres of user's location.
        parameters.putString("distance", "500");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
