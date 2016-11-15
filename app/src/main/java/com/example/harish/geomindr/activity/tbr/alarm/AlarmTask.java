package com.example.harish.geomindr.activity.tbr.alarm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.harish.geomindr.MainActivity;
import com.example.harish.geomindr.R;
import com.example.harish.geomindr.activity.map.TaskMapActivity;
import com.example.harish.geomindr.database.DatabaseHelper;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.IOException;
import java.util.List;

import static android.widget.Toast.makeText;

public class AlarmTask extends AppCompatActivity {
    private final static int ALARM_REQUEST = 1;
    String locationName;
    double latitude, longitude;
    int radius;
    String alarmTitle, alarmDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_task);
        setTitle("Add Alarm Task Reminder");

        final EditText mAlarmTitle = (EditText) findViewById(R.id.alarmTitle);
        final EditText mAlarmDesc = (EditText) findViewById(R.id.alarmDesc);
        final EditText mAlarmLocation = (EditText) findViewById(R.id.alarmLocation);
        Button mBtnAlarmSave = (Button) findViewById(R.id.btnAlarmSave);
        Button mBtnAlarmDiscard = (Button) findViewById(R.id.btnAlarmDiscard);
        FloatingActionButton mFABAddLocation = (FloatingActionButton) findViewById(R.id.fabAddLocation);
        final DiscreteSeekBar mRadius = (DiscreteSeekBar) findViewById(R.id.radius);

        mBtnAlarmSave.getBackground().setColorFilter(
                ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null),
                PorterDuff.Mode.MULTIPLY);
        mBtnAlarmDiscard.getBackground().setColorFilter(
                ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null),
                PorterDuff.Mode.MULTIPLY);

        mFABAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First check if the device is connected to the internet.
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    Intent a = new Intent(AlarmTask.this, TaskMapActivity.class);
                    a.putExtra("taskId", 2);
                    startActivityForResult(a, ALARM_REQUEST);
                } else {
                    Toast.makeText(AlarmTask.this, "Please connect to the internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnAlarmSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmTitle = mAlarmTitle.getText().toString();
                alarmDesc = mAlarmDesc.getText().toString();
                locationName = mAlarmLocation.getText().toString();

                if (alarmTitle.isEmpty()) {
                    Toast.makeText(AlarmTask.this, "Please enter alarm task title.", Toast.LENGTH_SHORT).show();
                }
                else if (alarmDesc.isEmpty()) {
                    Toast.makeText(AlarmTask.this, "Please enter alarm task description.", Toast.LENGTH_SHORT).show();
                }
                else if (locationName.isEmpty()) {
                    Toast.makeText(AlarmTask.this, "Please select a location.", Toast.LENGTH_SHORT).show();
                }
                else {
                    radius = mRadius.getProgress();
                    // Check if the location set by the user is a valid location.
                    if (ifLocationValid(locationName)) {
                        showConfirmDialogBox();
                    }
                    else {
                        Toast.makeText(AlarmTask.this, "Invalid location. Please enter a valid location.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mBtnAlarmDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiscardDialogBox();
            }
        });
    }

    private void showConfirmDialogBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title.
        alertDialog.setTitle("SAVE REMINDER");

        // Setting Dialog Message.
        alertDialog.setMessage("Do you want to save the reminder?");

        // To prevent dismiss dialog box on back key pressed.
        alertDialog.setCancelable(false);

        // On pressing Yes button, save data in the database
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Close the dialog box
                dialog.cancel();

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(AlarmTask.this);

                // getting SharedPreference instance
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("COUNTER", Context.MODE_PRIVATE);
                // getting SharedPreferences.Editor instance
                // SharedPreferences.Editor instance is required to edit the SharedPreference file
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // insert the record into the database
                long isInserted = databaseHelper.insertRecord(sharedPreferences.getInt("counter", -1), 2, 1,
                        alarmTitle, null, null, alarmDesc, null, locationName, latitude, longitude, radius);

                // check whether the record is successfully inserted or not
                if(isInserted >= 0) {
                    makeText(AlarmTask.this, "Reminder created.", Toast.LENGTH_LONG).show();
                    editor.putInt("counter", sharedPreferences.getInt("counter", -1) + 1);
                    editor.apply();
                }
                else {
                    makeText(AlarmTask.this, "Reminder not created. Please try again.", Toast.LENGTH_LONG).show();
                }

                // start 'HomeFragment' fragment
                startHomeFragment();
            }
        });

        // On pressing No button, dismiss the dialog box
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void showDiscardDialogBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title.
        alertDialog.setTitle("ALERT");

        // Setting Dialog Message.
        alertDialog.setMessage("Do you want to discard the reminder?");

        // To prevent dismiss dialog box on back key pressed.
        alertDialog.setCancelable(false);

        // On pressing Yes button, discard the reminder and take user to home activity
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startHomeFragment();
            }
        });

        // On pressing No button, dismiss the dialog box
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    // Check if location entered by the user is valid or not.
    private boolean ifLocationValid(String location) {
        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder(AlarmTask.this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //If size is 0 then wrong item is entered.
        return addressList!= null && addressList.size() > 0;
    }

    private void startHomeFragment() {
        Intent intent = new Intent(AlarmTask.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ALARM_REQUEST) {
            if(resultCode == RESULT_OK) {
                locationName = data.getStringExtra("locationName");
                latitude = data.getDoubleExtra("latitude", 0.0);
                longitude = data.getDoubleExtra("longitude", 0.0);
                EditText mAlarmLocation = (EditText) findViewById(R.id.alarmLocation);
                mAlarmLocation.setText(locationName);
            }
        }
    }

    @Override
    public void onBackPressed() {
        showDiscardDialogBox();
    }
}