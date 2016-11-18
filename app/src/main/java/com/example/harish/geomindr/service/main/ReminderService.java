package com.example.harish.geomindr.service.main;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.example.harish.geomindr.R;
import com.example.harish.geomindr.broadcast.alarm.AlarmDismissNotificationReceiver;
import com.example.harish.geomindr.broadcast.facebook.FacebookConfirmPostReceiver;
import com.example.harish.geomindr.broadcast.facebook.FacebookDeclinePostReceiver;
import com.example.harish.geomindr.broadcast.facebook.FacebookSelectAgainReceiver;
import com.example.harish.geomindr.broadcast.message.MessageConfirmSendReceiver;
import com.example.harish.geomindr.broadcast.message.MessageDeclineSendReceiver;
import com.example.harish.geomindr.broadcast.message.MessageSelectAgainReceiver;
import com.example.harish.geomindr.database.DatabaseHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class ReminderService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // to get the last known location
    // for most cases, last known location is equivalent to current location
    public static Location lastLocation;
    // flag to stop the service once we have reached the destination location
    // true = stop the service
    // false = continue executing the service
    public static boolean stopService;
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
        // Get device's last known location using google's FusedLocation API.
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (lastLocation != null) {
            // Get the device's current location's latitude.
            // Only for debugging purpose
            curLatitude = lastLocation.getLatitude();
            // Get the device's current location's longitude.
            // Only for debugging purpose.
            curLongitude = lastLocation.getLongitude();

            System.out.println(String.valueOf(curLatitude) + " : " + String.valueOf(curLongitude));

            // Access reminder objects from database and check if a reminder needs to be triggered.

            // Getting an instance of DatabaseHelper class.
            databaseHelper = DatabaseHelper.getInstance(ReminderService.this);
            // Retrieving all records from the database.
            Cursor res = databaseHelper.getAllRecords();

            // Check if there is something in the database
            if (res.getCount() > 0) {
                // Iterating through the retrieved records.
                while (res.moveToNext()) {
                    // If it is a location based facebook reminder, then task_id equals 1.
                    if (res.getInt(1) == 1) {
                        // Create a Location object for destination location
                        Location destLocation = new Location("dest_location");
                        destLocation.setLatitude(res.getDouble(9));
                        destLocation.setLongitude(res.getDouble(10));

                        // Calculate the distance between device's current location and destination location.
                        double distance = lastLocation.distanceTo(destLocation);

                        // If the distance is less than radius, then pop out the notification.
                        if (distance < res.getInt(11) && res.getInt(12) == 0) {
                            // Change the reminder status to 1 so that it does not get triggered again.
                            databaseHelper.updateStatus(res.getInt(0), 1);
                            sendFacebookNotification(res.getString(6), res.getString(8),
                                    String.valueOf(curLatitude), String.valueOf(curLongitude));
                        }
                        // If the reminder has already been triggered and user has
                        // left the location, then change the status back to 0 again.
                        else if (res.getInt(12) == 1 && distance > 2.5 * (res.getInt(11))) {
                            // Change the reminder status to 0 so that it can be triggered again.
                            databaseHelper.updateStatus(res.getInt(0), 0);
                        }
                    }
                    // If it is a location based message reminder (arrival type), then task_id equals 3.
                    // Also check the status of the reminder. If status is 0 then it means that
                    // app has not sent the arrival message. If status is 1 then it means that app has already
                    // sent the arrival message. The status will change from 1 to 0 when app sends departure
                    // message to the user.
                    else if (res.getInt(1) == 3 && res.getInt(12) == 0) {
                        // Create a Location object for destination location.
                        Location destLocation = new Location("dest_location");
                        destLocation.setLatitude(res.getDouble(9));
                        destLocation.setLongitude(res.getDouble(10));

                        // Calculate the distance between device's current location and destination location.
                        double distance = lastLocation.distanceTo(destLocation);
                        //Toast.makeText(this, String.valueOf(distance), Toast.LENGTH_SHORT).show();

                        // If the distance is less than radius, then pop out the notification.
                        if (distance < res.getInt(11)) {
                            // Update the status of reminder to 1.
                            databaseHelper.updateStatus(res.getInt(0), 1);
                            // Update the status of departure message task reminder to 1.
                            databaseHelper.updateStatus(res.getInt(0)+1, 1);
                            // Send appropriate notification to the user.
                            sendMessageNotification(3, res.getString(4), res.getString(5),
                                    res.getString(6), res.getString(8));
                        }
                    }
                    // If it is a location based message reminder (departure type), then task_id equals 5.
                    else if (res.getInt(1) == 5 && res.getInt(12) == 1) {
                        // Create a Location object for destination location.
                        Location destLocation = new Location("dest_location");
                        destLocation.setLatitude(res.getDouble(9));
                        destLocation.setLongitude(res.getDouble(10));

                        // Calculate the distance between device's current location and destination location.
                        double distance = lastLocation.distanceTo(destLocation);
                        //Toast.makeText(this, String.valueOf(distance), Toast.LENGTH_SHORT).show();

                        // If the distance is more than radius, then pop out the notification.
                        if (distance > res.getInt(11)) {
                            // Update the status of reminder to 0.
                            databaseHelper.updateStatus(res.getInt(0), 0);
                            // Update the status of arrival message task reminder to 0.
                            databaseHelper.updateStatus(res.getInt(0)-1, 0);
                            // Send appropriate notification to the user.
                            sendMessageNotification(5, res.getString(4), res.getString(5),
                                    res.getString(7), res.getString(8));
                        }
                    }
                    // If it is a location based alarm reminder, then task_id equals 2.
                    else if (res.getInt(1) == 2) {
                        // Create a Location object for destination location.
                        Location destLocation = new Location("dest_location");
                        destLocation.setLatitude(res.getDouble(9));
                        destLocation.setLongitude(res.getDouble(10));

                        // Calculate the distance between device's current location and destination location
                        double distance = lastLocation.distanceTo(destLocation);
                        //System.out.println(distance);

                        // If the distance is less than radius and reminder has not yet been triggered,
                        // then pop out the notification.
                        if (distance < res.getInt(11) && res.getInt(12) == 0)
                        {
                            // Update the status to 1.
                            databaseHelper.updateStatus(res.getInt(0), 1);
                            // Send appropriate notification to the user.
                            sendAlarmNotification(res.getString(3), res.getString(6), res.getString(8));
                        }
                        // If the reminder has already been triggered and user has
                        // left the location, then change the status back to 0 again.
                        else if (res.getInt(12) == 1 && distance > 2.5 * (res.getInt(11))) {
                            // Update the status of reminder back to 0.
                            databaseHelper.updateStatus(res.getInt(0), 0);
                        }
                    }
                }
            }

            // Close the cursor.
            res.close();
        }

        // Stop the current service and start again if the lastLocation is null.
        stopSelf();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // Send facebook task reminder notification.
    public void sendFacebookNotification(String msg, String location, String latitude, String longitude) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Intent passed to the Broadcast Receiver if user selects 'Yes'.
        Intent confirmIntent = new Intent(ReminderService.this, FacebookConfirmPostReceiver.class);
        // Pass message to post on facebook wall.
        confirmIntent.putExtra("msg", msg);
        // Pass location to post on facebook wall.
        confirmIntent.putExtra("location", location);
        // Pass Latitude and longitude of the location.
        confirmIntent.putExtra("latitude", latitude);
        confirmIntent.putExtra("longitude", longitude);

        // Intent passed to the Broadcast Receiver if user selects 'No'.
        Intent declineIntent = new Intent(ReminderService.this, FacebookDeclinePostReceiver.class);

        // Intent passed to the Broadcast Receiver if user does not select anything.
        Intent selectIntent = new Intent(ReminderService.this, FacebookSelectAgainReceiver.class);

        // If user selects yes.
        PendingIntent confirmPendingIntent = PendingIntent.getBroadcast
                (ReminderService.this, 0, confirmIntent, 0);
        // If user selects no.
        PendingIntent declinePendingIntent = PendingIntent.getBroadcast
                (ReminderService.this, 0, declineIntent, 0);
        // If user clicks on the notification.
        PendingIntent selectPendingIntent = PendingIntent.getBroadcast
                (ReminderService.this, 0, selectIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ReminderService.this)
                // Setting the title of the notification.
                .setContentTitle("Facebook Post Alert").setSmallIcon(R.drawable.ic_create_white_24dp)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Looks like you are at " + location + "."
                        + " Do you want to post \"" + msg + "\" to " + "your Facebook wall."))
                // Vibrate the device twice when notification pops out.
                .setDefaults(Notification.DEFAULT_VIBRATE)
                // Sound the system's default ringtone when notification pops out.
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                // Restrict user from swiping out the notification.
                .setOngoing(true)
                // Add Yes action to the notification.
                .addAction(R.drawable.ic_check_white_24dp, "Yes", confirmPendingIntent)
                // Add No action to the notification.
                .addAction(R.drawable.ic_close_white_24dp, "No", declinePendingIntent);

        // Set pending intent to the notification.
        notificationBuilder.setContentIntent(selectPendingIntent);
        // Finally, display the notification to the user.
        notificationManager.notify(1111, notificationBuilder.build());
    }

    // Send alarm task reminder to the user.
    private void sendAlarmNotification(String title, String msg, String locationName) {
        // Send notification to the user.
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        // Define sound URI.
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        // If user has never set an alarm, then soundUri will be null.
        if (soundUri == null) {
            // soundUri is null, using backup.
            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // I can't see this ever being null (as always have a default notification)
            // but just in case.
            if(soundUri == null) {
                // soundUri backup is null, using 2nd backup.
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

        // This intent will be passed to Broadcast Receiver as a PendingIntent when user dismisses the alarm.
        Intent dismissIntent = new Intent(ReminderService.this, AlarmDismissNotificationReceiver.class);

        // If user dismisses the alarm.
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast
                (ReminderService.this, 0, dismissIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ReminderService.this)
                // Setting the title of the notification.
                .setContentTitle(title).setSmallIcon(R.drawable.ic_alarm_white_24dp)
                // Setting the content of the notification.
                .setStyle(new NotificationCompat.BigTextStyle().bigText
                        ("Looks like you have reached " + locationName + ". This is to remind you to " + msg))
                // Restrict user from swiping out the notification.
                .setOngoing(true)
                // Cancel on touch.
                .setAutoCancel(true)
                // Set the sound played with the notification.
                .setSound(soundUri)
                // Set vibration pattern
                .setVibrate(new long[] {1000, 1000, 0, 0, 1000, 1000})
                // Add Yes action to the notification.
                .addAction(R.drawable.ic_alarm_off_white_24dp, "Dismiss", dismissPendingIntent);

        // Passing an empty Intent to PendingIntent because we don't want to open any activity when user
        // clicks on the notification.
        PendingIntent resultPendingIntent = PendingIntent.getActivity(ReminderService.this,  0, new Intent(), 0);
        // Setting the PendingIntent on notification.
        notificationBuilder.setContentIntent(resultPendingIntent);

        // Build Notification based on specifications defined in Notification.Builder.
        Notification notification = notificationBuilder.build();
        // Setting this flag so that notification sound continues to play till user responds to the notification.
        notification.flags = Notification.FLAG_INSISTENT;
        // Finally, display the notification to the user.
        notificationManager.notify(2222, notification);
    }

    // Send message task reminder notification to the user.
    public void sendMessageNotification(int taskId, String name, String number, String msg, String locationName) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Intent passed to the Broadcast Receiver if user selects 'Yes'.
        Intent confirmIntent = new Intent(ReminderService.this, MessageConfirmSendReceiver.class);
        // Pass the recipient number.
        confirmIntent.putExtra("number", number);
        // Pass the message for the recipient.
        confirmIntent.putExtra("msg", msg);

        // Intent passed to the Broadcast Receiver if user selects 'No'.
        Intent declineIntent = new Intent(ReminderService.this, MessageDeclineSendReceiver.class);

        // Intent passed to the Broadcast Receiver if user does not select anything.
        Intent selectIntent = new Intent(ReminderService.this, MessageSelectAgainReceiver.class);

        // If user selects yes.
        PendingIntent confirmPendingIntent = PendingIntent.getBroadcast
                (ReminderService.this, 0, confirmIntent, 0);
        // If user selects no.
        PendingIntent declinePendingIntent = PendingIntent.getBroadcast
                (ReminderService.this, 0, declineIntent, 0);
        // If user clicks on the notification , i.e, selects nothing.
        PendingIntent selectPendingIntent = PendingIntent.getBroadcast
                (ReminderService.this, 0, selectIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ReminderService.this)
                // Setting the title of the notification.
                .setContentTitle("Message Reminder Alert").setSmallIcon(R.drawable.ic_textsms_white_24dp)
                // Vibrate the device twice when notification pops out.
                .setDefaults(Notification.DEFAULT_VIBRATE)
                // Sound the system's default ringtone when notification pops out.
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                // Restrict user from swiping out the notification.
                .setOngoing(true)
                // Add Yes action to the notification.
                .addAction(R.drawable.ic_check_white_24dp, "Yes", confirmPendingIntent)
                // Add No action to the notification.
                .addAction(R.drawable.ic_close_white_24dp, "No", declinePendingIntent);

        // If it is an arrival message task reminder.
        if(taskId == 3) {
            if(name == null) {
                // Setting the content of the notification.
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText
                        ("Looks like you have arrived at " + locationName + "."
                                + " Do you want to send message \"" + msg + "\" to " + number + "."));
            }
            else {
                // Setting the content of the notification.
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText
                        ("Looks like you have arrived at " + locationName + "."
                                + " Do you want to send message \"" + msg + "\" to " + name + "."));
            }
        }
        // If it is a departure message task reminder.
        else {
            if(name == null) {
                // Setting the content of the notification.
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText
                        ("Looks like you are departing from " + locationName + "."
                                + " Do you want to send message \"" + msg + "\" to " + number + "."));
            }
            else {
                // Setting the content of the notification.
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText
                        ("Looks like you are departing from " + locationName + "."
                                + " Do you want to send message \"" + msg + "\" to " + name + "."));
            }
        }

        // Set pending intent to the notification.
        notificationBuilder.setContentIntent(selectPendingIntent);
        // Finally, display the notification to the user.
        notificationManager.notify(3333, notificationBuilder.build());
    }
}