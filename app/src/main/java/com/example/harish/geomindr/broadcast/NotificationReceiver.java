package com.example.harish.geomindr.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import static com.example.harish.geomindr.service.main.ReminderService.audioManager;
import static com.example.harish.geomindr.service.main.ReminderService.mediaPlayer;
import static com.example.harish.geomindr.service.main.ReminderService.ringtone;
import static com.example.harish.geomindr.service.main.ReminderService.userVolume;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt("notifiactionId");

        /*// If the user's device is in silent or vibration mode, then
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

        System.out.println("hola");
*/
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);
    }
}