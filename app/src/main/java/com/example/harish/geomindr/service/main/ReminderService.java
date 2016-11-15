package com.example.harish.geomindr.service.main;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.example.harish.geomindr.R;
import com.example.harish.geomindr.database.DatabaseHelper;
import com.example.harish.geomindr.service.tbr.alarm.DismissAlarmService;
import com.example.harish.geomindr.service.tbr.facebook.FacebookConfirmService;
import com.example.harish.geomindr.service.tbr.facebook.FacebookDeclineService;
import com.example.harish.geomindr.service.tbr.facebook.FacebookSelectAgainService;
import com.example.harish.geomindr.service.tbr.message.MessageConfirmService;
import com.example.harish.geomindr.service.tbr.message.MessageDeclineService;
import com.example.harish.geomindr.service.tbr.message.MessageSelectAgainService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;

public class ReminderService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // to get the last known location
    // for most cases, last known location is equivalent to current location
    public static Location lastLocation;
    // flag to stop the service once we have reached the destination location
    // true = stop the service
    // false = continue executing the service
    public static boolean stopService;
    public static AudioManager audioManager;
    public static MediaPlayer mediaPlayer;
    public static Ringtone ringtone;
    public static int userVolume;
    // instance of GoogleApiClient
    // used to access google's FusedLocationApi (bette alternative of android's LocationListener)
    GoogleApiClient googleApiClient;
    // device's current location's latitude
    double curLatitude;
    // device's current location's longitude
    double curLongitude;
    // creating an object of DatabaseHelper class
    DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        // instantiating the object of DatabaseHelper class
        databaseHelper = DatabaseHelper.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // build the instance of GoogleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // connect to the desired GoogleApiClient service
        googleApiClient.connect();

        // restart the service if it is killed by the android os
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // continue executing the service until stopService flag is true, i.e, until we have not reached our destination location
        if (!stopService) {
            Intent intent = new Intent(this, ReminderService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            // again start the service after 10 seconds
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    // Right now i am assuming that user will not explicitly revoke location permission manually from settings
    @SuppressWarnings({"MissingPermission"})
    public void onConnected(Bundle bundle) {
        // get device's last known location using google's FusedLocation API
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (lastLocation != null) {
            // get the device's current location's latitude
            // only for debugging purpose
            curLatitude = lastLocation.getLatitude();
            // get the device's current location's longitude
            // only for debugging purpose
            curLongitude = lastLocation.getLongitude();

            System.out.println(String.valueOf(curLatitude) + " : " + String.valueOf(curLongitude));
            //Toast.makeText(this, String.valueOf(curLatitude) + " : " + String.valueOf(curLongitude), Toast.LENGTH_SHORT).show();

            /*
             Access reminder objects from database and check if some reminder needs to be triggered
             */

            // retrieving all records from the database
            Cursor res = databaseHelper.getAllRecords();

            // check if there is something in the database
            if (res.getCount() > 0) {
                // iterating through the retrieved records
                while (res.moveToNext()) {
                    // if it is a location based facebook reminder, then task_id equals 1
                    if (res.getInt(1) == 1) {
                        // create a Location object for destination location
                        Location destLocation = new Location("dest_location");
                        destLocation.setLatitude(res.getDouble(8));
                        destLocation.setLongitude(res.getDouble(9));

                        // calculate the distance between device's current location and destination location
                        double distance = lastLocation.distanceTo(destLocation);
                        //Toast.makeText(this, String.valueOf(distance), Toast.LENGTH_SHORT).show();

                        // if the distance is less than a threshold value, then pop out the notification
                        if (distance < 400.0 && res.getInt(10) == 0) {
                            databaseHelper.updateStatus(res.getInt(0), 1);
                            sendFacebookNotification(res.getString(5), res.getString(7),
                                    String.valueOf(curLatitude), String.valueOf(curLongitude));
                        } else if (distance > 600.0 && res.getInt(10) == 1) {
                            databaseHelper.updateStatus(res.getInt(0), 0);
                            stopSelf();
                        } else {
                            stopSelf();
                        }
                    }
                    // if it is a location based message reminder and user has arrived at the destination location, then task_id equals 3
                    else if (res.getInt(1) == 3 && res.getInt(10) == 0) {
                        // create a Location object for destination location
                        Location destLocation = new Location("dest_location");
                        destLocation.setLatitude(res.getDouble(8));
                        destLocation.setLongitude(res.getDouble(9));

                        // calculate the distance between device's current location and destination location
                        double distance = lastLocation.distanceTo(destLocation);
                        //Toast.makeText(this, String.valueOf(distance), Toast.LENGTH_SHORT).show();

                        // if the distance is less than a threshold value and reminder status is 0, then pop out the notification
                        if (distance < 400.0) {
                            databaseHelper.updateTaskId(res.getInt(0), 5);
                            databaseHelper.updateStatus(res.getInt(0), 1);
                            sendMessageNotification(3, res.getString(3), res.getString(4), res.getString(5), res.getString(7));
                        } else {
                            stopSelf();
                        }
                    }
                    // if it is a location based message reminder and user has departed from the destination location, then task_id equals 5
                    else if (res.getInt(1) == 5) {
                        // create a Location object for destination location
                        Location destLocation = new Location("dest_location");
                        destLocation.setLatitude(res.getDouble(8));
                        destLocation.setLongitude(res.getDouble(9));

                        // calculate the distance between device's current location and destination location
                        double distance = lastLocation.distanceTo(destLocation);
                        //Toast.makeText(this, String.valueOf(distance), Toast.LENGTH_SHORT).show();

                        // if the distance is more than a threshold value, then pop out the notification
                        if (distance > 600.0) {
                            databaseHelper.updateTaskId(res.getInt(0), 3);
                            databaseHelper.updateStatus(res.getInt(0), 0);
                            sendMessageNotification(5, res.getString(3), res.getString(4), res.getString(6), res.getString(7));
                        } else {
                            stopSelf();
                        }
                    }
                    // if it is a location based alarm reminder, then task_id equals 2
                    else if (res.getInt(1) == 2) {
                        // create a Location object for destination location
                        Location destLocation = new Location("dest_location");
                        destLocation.setLatitude(res.getDouble(8));
                        destLocation.setLongitude(res.getDouble(9));

                        // calculate the distance between device's current location and destination location
                        double distance = lastLocation.distanceTo(destLocation);
                        //Toast.makeText(this, String.valueOf(distance), Toast.LENGTH_SHORT).show();

                        // if the distance is less than a threshold value, then pop out the notification
                        if (distance < 400.0 && res.getInt(10) == 0) {
                            databaseHelper.updateStatus(res.getInt(0), 1);
                            sendAlarmNotification(res.getString(5), res.getString(7));
                        } else if (distance > 600.0 && res.getInt(10) == 1) {
                            databaseHelper.updateStatus(res.getInt(0), 0);
                            stopSelf();
                        } else {
                            stopSelf();
                        }
                    } else {
                        stopSelf();
                    }
                }
            }
            else {
                stopSelf();
            }
        }
        else {
            // stop the current service and start again if the lastLocation is null
            stopSelf();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void sendMessageNotification(int taskId, String name, String number, String msg, String locationName) {
        // send notification to the user
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent confirmIntent = new Intent(ReminderService.this, MessageConfirmService.class);
        confirmIntent.putExtra("number", number);
        confirmIntent.putExtra("msg", msg);
        Intent declineIntent = new Intent(ReminderService.this, MessageDeclineService.class);
        Intent selectIntent = new Intent(ReminderService.this, MessageSelectAgainService.class);

        // if user selects yes
        PendingIntent confirmPendingIntent = PendingIntent.getService
                (ReminderService.this, 0, confirmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // if user selects no
        PendingIntent declinePendingIntent = PendingIntent.getService
                (ReminderService.this, 0, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // if user clicks on the notification
        PendingIntent selectPendingIntent = PendingIntent.getService
                (ReminderService.this, 0, selectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ReminderService.this)
                // setting the title of the notification
                .setContentTitle("Message Reminder Alert").setSmallIcon(R.drawable.ic_textsms_white_24dp)
                // vibrate the device twice when notification pops out
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                // sound the system's default ringtone when notification pops out
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                // restrict user from swiping out the notification
                .setOngoing(true)
                // add Yes action to the notification
                .addAction(R.drawable.ic_check_white_24dp, "Yes", confirmPendingIntent)
                // add No action to the notification
                .addAction(R.drawable.ic_close_white_24dp, "No", declinePendingIntent);

        if(taskId == 3) {
            if(name == null) {
                // setting the content of the notification
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText
                        ("Looks like you have arrived at " + locationName + "."
                        + " Do you want to send message \"" + msg + "\" to " + number + "."));
            }
            else {
                // setting the content of the notification
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText
                        ("Looks like you have arrived at " + locationName + "."
                        + " Do you want to send message \"" + msg + "\" to " + name + "."));
            }
        }
        else {
            if(name == null) {
                // setting the content of the notification
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText
                        ("Looks like you are departing from " + locationName + "."
                        + " Do you want to send message \"" + msg + "\" to " + number + "."));
            }
            else {
                // setting the content of the notification
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText
                        ("Looks like you are departing from " + locationName + "."
                        + " Do you want to send message \"" + msg + "\" to " + name + "."));
            }
        }

        // set pending intent to the notification
        notificationBuilder.setContentIntent(selectPendingIntent);
        // finally, display the notification to the user
        notificationManager.notify(3, notificationBuilder.build());

        // stop the current service
        stopSelf();
    }

    private void sendAlarmNotification(String msg, String locationName) {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //remember what the user's volume was set to before we change it.
        userVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        try {
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT
                || audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
            mediaPlayer = new MediaPlayer();

            try {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.setDataSource(this, notification);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume
                    (AudioManager.STREAM_ALARM), AudioManager.FLAG_PLAY_SOUND);
        }

        // send notification to the user
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        Intent dismissIntent = new Intent(ReminderService.this, DismissAlarmService.class);

        // if user dismisses the alarm
        PendingIntent dismissPendingIntent = PendingIntent.getService
                (ReminderService.this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ReminderService.this)
                // setting the title of the notification
                .setContentTitle("Alarm").setSmallIcon(R.drawable.ic_alarm_white_24dp)
                // setting the content of the notification
                .setStyle(new NotificationCompat.BigTextStyle().bigText
                        ("You have reached " + locationName + ". This is to remind you to " + msg))
                // restrict user from swiping out the notification
                .setOngoing(true)
                // add Yes action to the notification
                .addAction(R.drawable.ic_alarm_off_white_24dp, "Dismiss", dismissPendingIntent);

        // set pending intent to the notification
        notificationBuilder.setContentIntent(dismissPendingIntent);
        // finally, display the notification to the user
        notificationManager.notify(2, notificationBuilder.build());
        stopSelf();
    }

    public void sendFacebookNotification(String msg, String location, String latitude, String longitude) {
        // send notification to the user
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent confirmIntent = new Intent(ReminderService.this, FacebookConfirmService.class);
        //Log.d("Inserted Message", msg);
        confirmIntent.putExtra("msg", msg);
        confirmIntent.putExtra("location", location);
        confirmIntent.putExtra("latitude", latitude);
        confirmIntent.putExtra("longitude", longitude);
        Intent declineIntent = new Intent(ReminderService.this, FacebookDeclineService.class);
        Intent selectIntent = new Intent(ReminderService.this, FacebookSelectAgainService.class);

        // if user selects yes
        PendingIntent confirmPendingIntent = PendingIntent.getService
                (ReminderService.this, 0, confirmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // if user selects no
        PendingIntent declinePendingIntent = PendingIntent.getService
                (ReminderService.this, 0, declineIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // if user clicks on the notification
        PendingIntent selectPendingIntent = PendingIntent.getService
                (ReminderService.this, 0, selectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ReminderService.this)
                // setting the title of the notification
                .setContentTitle("Facebook Post Alert").setSmallIcon(R.drawable.ic_create_white_24dp)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Looks like you are at " + location + "."
                + " Do you want to post \"" + msg + "\" to " + "your facebook wall."))
                // vibrate the device twice when notification pops out
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                // sound the system's default ringtone when notification pops out
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                // restrict user from swiping out the notification
                .setOngoing(true)
                // add Yes action to the notification
                .addAction(R.drawable.ic_check_white_24dp, "Yes", confirmPendingIntent)
                // add No action to the notification
                .addAction(R.drawable.ic_close_white_24dp, "No", declinePendingIntent);

        // set pending intent to the notification
        notificationBuilder.setContentIntent(selectPendingIntent);
        // finally, display the notification to the user
        notificationManager.notify(1, notificationBuilder.build());

        // stop the current service
        stopSelf();
    }
}