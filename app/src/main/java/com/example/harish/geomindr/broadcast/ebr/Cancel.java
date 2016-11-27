package com.example.harish.geomindr.broadcast.ebr;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.harish.geomindr.database.DatabaseHelper;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Cancel extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Cancel the notification.
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);

        System.out.println(intent.getExtras().getInt("notId"));
        notificationManager.cancel(intent.getExtras().getInt("notId"));

        String toDelete = intent.getStringExtra("entity");

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        // Delete the Entity Based Reminder record from database.
        databaseHelper.deleteData(toDelete);

        Toast.makeText(context, "Reminder cancelled.", Toast.LENGTH_SHORT).show();
    }
}