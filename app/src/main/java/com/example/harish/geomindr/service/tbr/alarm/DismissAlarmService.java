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

public class DismissAlarmService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT ||
                audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, userVolume, AudioManager.FLAG_PLAY_SOUND);
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
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
