package com.example.harish.geomindr.service.tbr.facebook;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

// Service that gets called when user does not select either YES or NO
// on the facebook task notification.
public class FacebookSelectAgainService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Please select either YES or NO", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
