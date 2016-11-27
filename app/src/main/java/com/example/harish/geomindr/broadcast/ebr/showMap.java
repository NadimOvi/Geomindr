package com.example.harish.geomindr.broadcast.ebr;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.harish.geomindr.database.DatabaseHelper;

import static android.content.Context.NOTIFICATION_SERVICE;

public class showMap extends BroadcastReceiver {
    // Device's current lat and lng.
    Double currLat, currLong;
    // Type of entity.
    String entity;
    // Database instance;
    DatabaseHelper databaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Cancel the notification.
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(4444);

        entity = intent.getStringExtra("entity");
        currLat = intent.getDoubleExtra("curLat", -9.99);
        currLong = intent.getDoubleExtra("curLng", -9.99);
    }
}
