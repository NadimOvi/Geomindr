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
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(4444);

        String toDelete = intent.getStringExtra("cancel1");
        intent.removeExtra("cancel");
        intent.removeExtra("cancel1");

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        Integer deleted = databaseHelper.deleteData(toDelete);

        if(deleted<=0)
            Toast.makeText(context, "Nothing is deleted", Toast.LENGTH_SHORT).show();

        Toast.makeText(context, "Reminder cancelled.", Toast.LENGTH_SHORT).show();
    }
}