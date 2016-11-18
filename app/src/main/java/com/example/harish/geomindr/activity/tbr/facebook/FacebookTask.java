package com.example.harish.geomindr.activity.tbr.facebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.harish.geomindr.MainActivity;
import com.example.harish.geomindr.R;
import com.example.harish.geomindr.database.DatabaseHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Collections;

import static android.widget.Toast.makeText;

public class FacebookTask extends AppCompatActivity {
    // Facebook's login button.
    LoginButton loginButton;
    // Callback manager to manage the result of logging in user via facebook.
    CallbackManager callbackManager;

    // Check in which layout ser is currently present and pop out message accordingly on back button press.
    int exitCode;
    // Type of the location where reminder needs to be triggered.
    String locationType;
    // Radius around location within which reminder needs to be triggered.
    int triggerRadius;
    // Message to be posted on facebook wall.
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initializing facebook's SDK.
        // NOTE: Do this before displaying the layout of the activity.
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        System.out.println(AccessToken.getCurrentAccessToken());
        // If the access token is not present, then it means that user has not given our app
        // required permissions to post status update on its facebook wall.
        if (AccessToken.getCurrentAccessToken() == null || AccessToken.getCurrentAccessToken().isExpired()) {
            setTitle("Log in with Facebook");
            setContentView(R.layout.activity_facebook_login);

            // Set exit code.
            exitCode = 1;
            // Creating the callback manager.
            callbackManager = CallbackManager.Factory.create();
            // Facebook's default login button.
            loginButton = (LoginButton) findViewById(R.id.login_button);
            // Setting 'publish_actions' permission.
            // This permission will allow our app to post to user's facebook wall.
            loginButton.setPublishPermissions(Collections.singletonList("publish_actions"));

            // Registering the callback manager.
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                // If the login via facebook is successful.
                // We will get the access token with requested permissions, if the login is successful.
                public void onSuccess(LoginResult loginResult) {
                    Toast.makeText(getApplicationContext(), "Logged in successfully.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FacebookTask.this, FacebookTask.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                // If the user cancels the login.
                @Override
                public void onCancel() {
                    Toast.makeText(FacebookTask.this, "Login cancelled.", Toast.LENGTH_LONG).show();
                }

                // If there is some error logging in via facebook (maybe internet connectivity issue).
                @Override
                public void onError(FacebookException e) {
                    Toast.makeText(FacebookTask.this, "Error logging in with facebook. Please try again.",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            System.out.println(AccessToken.getCurrentAccessToken().toString());
            setTitle("Add Facebook Task Reminder");
            setContentView(R.layout.activity_facebook_task);

            // Set exit code.
            exitCode = 2;

            // Initializing View objects.
            final EditText facebookMsg = (EditText) findViewById(R.id.facebook_msg);
            Button btnFacebookSave = (Button) findViewById(R.id.btn_facebook_save);
            Button btnFacebookDiscard = (Button) findViewById(R.id.btn_facebook_discard);
            final DiscreteSeekBar radius = (DiscreteSeekBar) findViewById(R.id.radius);

            // Setting background color for 'Save Reminder' button.
            btnFacebookSave.getBackground().setColorFilter(
                    ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null),
                    PorterDuff.Mode.MULTIPLY);
            // Setting background color for 'Discard Reminder' button.
            btnFacebookDiscard.getBackground().setColorFilter(
                    ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null),
                    PorterDuff.Mode.MULTIPLY);

            // Click listener on 'Save Reminder' button.
            // It validates all fields before saving the reminder.
            btnFacebookSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msg = facebookMsg.getText().toString();
                    locationType = "Shopping Complex";

                    if (msg.isEmpty()) {
                        Toast.makeText(FacebookTask.this, "Please enter message to be posted on your facebook wall.",
                                Toast.LENGTH_LONG).show();
                    }
                    else if (locationType.isEmpty()) {
                        Toast.makeText(FacebookTask.this, "Please choose a location.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        triggerRadius = radius.getProgress();
                        showConfirmDialogBox();
                    }
                }
            });

            // Click listener for 'Discard Reminder' button.
            btnFacebookDiscard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDiscardDialogBox();
                }
            });
        }
    }

    // Dialog box to confirm that user wants to add the reminder.
    // If user confirms, we add the reminder to the database.
    private void showConfirmDialogBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title.
        alertDialog.setTitle("SAVE REMINDER");

        // Setting Dialog Message.
        alertDialog.setMessage("Do you want to save the reminder?");

        // To prevent dismiss dialog box on back key pressed.
        alertDialog.setCancelable(false);

        // On pressing Yes button, save data in the database.
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Close the dialog box.
                dialog.cancel();

                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(FacebookTask.this);

                // Getting SharedPreference instance.
                SharedPreferences sharedPreferences = getApplicationContext().
                        getSharedPreferences("COUNTER", Context.MODE_PRIVATE);
                // Getting SharedPreferences.Editor instance.
                // SharedPreferences.Editor instance is required to edit the SharedPreference file.
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Insert the record into the database.
                long isInserted = databaseHelper.insertRecord(sharedPreferences.getInt("counter", -1), 1, 1,
                        null, null, null, msg, null, locationType, 28.6269401, 77.2978436, triggerRadius);

                // Check whether the record is successfully inserted or not.
                if(isInserted >= 0) {
                    makeText(FacebookTask.this, "Reminder created.", Toast.LENGTH_LONG).show();
                    editor.putInt("counter", sharedPreferences.getInt("counter", -1) + 1);
                    editor.apply();
                }
                else {
                    makeText(FacebookTask.this, "Reminder not created. Please try again.", Toast.LENGTH_LONG).show();
                }

                // Start 'HomeFragment' fragment.
                startHomeFragment();
            }
        });

        // On pressing No button, dismiss the dialog box.
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message.
        alertDialog.show();
    }

    // Dialog box to confirm that user wants to discard the reminder.
    // If user confirms, we go back to 'HomeFragment' fragment.
    private void showDiscardDialogBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title.
        alertDialog.setTitle("ALERT");

        // Setting Dialog Message.
        alertDialog.setMessage("Do you want to discard the reminder?");

        // To prevent dismiss dialog box on back key pressed.
        alertDialog.setCancelable(false);

        // On pressing Yes button, discard the reminder and take user to home activity.
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startHomeFragment();
            }
        });

        // On pressing No button, dismiss the dialog box.
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message.
        alertDialog.show();
    }

    // Dialog box to confirm that user wants to exit the activity without logging in with Facebook.
    // If user confirms, we go back to 'HomeFragment' fragment.
    private void showExitDialogBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title.
        alertDialog.setTitle("ALERT");

        // Setting Dialog Message.
        alertDialog.setMessage("You won't be able to set a Facebook Task Reminder unless you log in" +
                " with Facebook. Do you want to exit?");

        // To prevent dismiss dialog box on back key pressed.
        alertDialog.setCancelable(false);

        // On pressing Yes button, exit nd take user to home activity.
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startHomeFragment();
            }
        });

        // On pressing No button, dismiss the dialog box.
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message.
        alertDialog.show();
    }

    // Start 'HomeFragment' fragment.
    private void startHomeFragment() {
        Intent intent = new Intent(FacebookTask.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // Result sent by facebook activity when user log in with facebook.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // Confirm before user actually goes back to 'HomeFragment' fragment.
    @Override
    public void onBackPressed() {
        if (exitCode == 1) {
            showExitDialogBox();
        }
        else {
            showDiscardDialogBox();
        }
    }
}