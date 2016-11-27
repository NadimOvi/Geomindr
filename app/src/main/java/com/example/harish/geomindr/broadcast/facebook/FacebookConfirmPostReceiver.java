package com.example.harish.geomindr.broadcast.facebook;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

public class FacebookConfirmPostReceiver extends BroadcastReceiver {
    String msg;
    String latitude;
    String longitude;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        msg = intent.getStringExtra("msg");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        this.context = context;

        // Post to facebook.
        doPost();
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
                                        Toast.makeText(context, "Posted.",
                                                Toast.LENGTH_SHORT).show();
                                        NotificationManager notificationManager = (NotificationManager)
                                                context.getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.cancel(1111);
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
        // Search for facebook pages within 500 metres of user's location.
        parameters.putString("distance", "500");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
