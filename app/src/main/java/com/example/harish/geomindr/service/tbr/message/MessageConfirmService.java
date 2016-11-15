package com.example.harish.geomindr.service.tbr.message;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

// Service that gets called when user selects YES
// on the message task notification.
public class MessageConfirmService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Send SMS to the specified user.
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(intent.getStringExtra("number"), null,
                intent.getStringExtra("msg"), null, null);
        // Set message sent success toast.
        Toast.makeText(this, "Message sent.", Toast.LENGTH_LONG).show();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(3);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}