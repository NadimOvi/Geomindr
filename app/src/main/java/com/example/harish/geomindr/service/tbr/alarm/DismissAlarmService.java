package com.example.harish.geomindr.service.tbr.alarm;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.Toast;

import static com.example.harish.geomindr.service.main.ReminderService.audioManager;
import static com.example.harish.geomindr.service.main.ReminderService.mediaPlayer;
import static com.example.harish.geomindr.service.main.ReminderService.ringtone;
import static com.example.harish.geomindr.service.main.ReminderService.userVolume;

// Service to dismiss the triggered alarm.
public class DismissAlarmService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // If the user's device is in silent or vibration mode, then
        // set the user's device volume back to as it was before the
        // alarm was triggered.
        if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT ||
                audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, userVolume, AudioManager.FLAG_PLAY_SOUND);
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        // Stop the alarm ringtone.
        ringtone.stop();
        Toast.makeText(this, "Alarm dismissed.", Toast.LENGTH_LONG).show();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(2);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}