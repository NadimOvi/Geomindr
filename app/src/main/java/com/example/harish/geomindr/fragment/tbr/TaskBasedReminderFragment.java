package com.example.harish.geomindr.fragment.tbr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;
import static android.widget.Toast.makeText;
import static com.facebook.FacebookSdk.getApplicationContext;

public class TaskBasedReminderFragment extends Fragment implements RecyclerView.OnItemTouchListener, View.OnClickListener {
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void onClick(View v) {

    }

    /*private static final int PICK_CONTACT_REQUEST = 0;
    private static final int ALARM_REQUEST = 1;
    private static final int MESSAGE_REQUEST = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;
    //public static String location="";
    public int buttonCheck = 0;
    String LOG_TAG = "Reminder APP:";
    RecyclerView task_remlist;
    CoordinatorLayout parent_layout;
    ReminderRecyclerAdapter mAdapter;
    ActionMode rem_list_action_mode;
    ActionMode.Callback rem_list_action_mode_callback;
    GestureDetectorCompat gestureDetector;
    FloatingActionButton fb_fab, alarm_fab, msg_fab;
    LayoutInflater layoutInflater;
    View fb_popup_view, alarm_popup_view, msg_popup_view;
    ActionBar actionBar;
    LoginButton loginButton;
    CallbackManager callbackManager;
    ImageButton fb_map_button;
    ImageButton fb_my_loc_button;
    AlertDialog.Builder alertDialogBuilderFBPost, alertDialogBuilderFBLogin, alertDialogBuilderAlarm, alertDialogBuilderMsg;
    AlertDialog alertDialogFB , alertDialogAlarm, alertDialogMsg;
    String locationName;
    Double latitude, longitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());
        AppEventsLogger.activateApp(getActivity().getApplication());

        // getting application context
        Context context = getApplicationContext();
        // getting SharedPreference instance
        SharedPreferences sharedPreferences = context.getSharedPreferences("COUNTER", Context.MODE_PRIVATE);
        // getting SharedPreferences.Editor instance
        // SharedPreferences.Editor instance is required to edit the SharedPreference file
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.getInt("counter", -1) == -1) {
            editor.putInt("counter", 0);
        }

        editor.apply();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_based_rem, container, false);

        task_remlist = (RecyclerView) view.findViewById(R.id.task_rem_list);
        parent_layout = (CoordinatorLayout) view.findViewById(R.id.taskrem_coordinatorlayout);
        fb_fab = (FloatingActionButton) view.findViewById(R.id.task_fb_fab);
        alarm_fab = (FloatingActionButton) view.findViewById(R.id.task_alarm_fab);
        msg_fab = (FloatingActionButton) view.findViewById(R.id.task_msg_fab);

        layoutInflater = LayoutInflater.from(getContext());
        alarm_popup_view = layoutInflater.inflate(R.layout.popup_task_alarm, null);
        msg_popup_view = layoutInflater.inflate(R.layout.popup_task_msg, null);

        gestureDetector = new GestureDetectorCompat(getContext(), new RecyclerViewDemoOnGestureListener());
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        rem_list_action_mode_callback = new ActionMode.Callback() {
            private int statusBarColor;

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                actionBar.hide();
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.action_mode_options_menu, menu);

                fb_fab.setVisibility(View.GONE);
                alarm_fab.setVisibility(View.GONE);
                msg_fab.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //hold current color of status bar
                    statusBarColor = getActivity().getWindow().getStatusBarColor();
                    //set your gray color
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
                int currPos = -1;
                for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                    currPos = selectedItemPositions.get(i);
                }

                switch (item.getItemId()) {
                    case R.id.option_edit:
                        makeText(getContext(), "Task reminder edited.", Toast.LENGTH_SHORT).show();

                        mode.finish();
                        return true;
                    case R.id.option_delete:
                        makeText(getContext(), "Task reminder Deleted.", Toast.LENGTH_SHORT).show();
                        Reminder tbr = reminderList.get(currPos);
                        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
                        databaseHelper.deleteTask(tbr.reminder_id);
                        reminderList.remove(currPos);
                        mAdapter.notifyDataSetChanged();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                rem_list_action_mode = null;
                mAdapter.clearSelections();

                fb_fab.setVisibility(View.VISIBLE);
                alarm_fab.setVisibility(View.VISIBLE);
                msg_fab.setVisibility(View.VISIBLE);

//                actionBar.show();
            }
        };

        // if the access token is not present, then it means that user has not given our app
        // required permissions to post status update on its facebook wall
        if (AccessToken.getCurrentAccessToken() == null || AccessToken.getCurrentAccessToken().isExpired()) {
            fb_popup_view = layoutInflater.inflate(R.layout.popup_fb_login, null);

            // creating the callback manager
            callbackManager = CallbackManager.Factory.create();

            loginButton = (LoginButton) fb_popup_view.findViewById(R.id.login_button);
            // setting 'publish_actions' permission
            // this permission will allow our app to post to user's facebook wall
            loginButton.setPublishPermissions(Collections.singletonList("publish_actions"));
            loginButton.setFragment(this);

            // registering the callback manager
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                // if the login via facebook is successful
                // we will get the access token with requested permissions, if the login is successful
                public void onSuccess(LoginResult loginResult) {
                    makeText(getContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                    alertDialogFB.dismiss();
                    tbrFacebookPostDialogBox();
                    fb_popup();
                }

                // if the user cancels the login
                @Override
                public void onCancel() {
                    makeText(getContext(), "Log in cancelled", Toast.LENGTH_SHORT).show();
                }

                // if there is some error logging in via facebook (maybe internet connectivity issue)
                @Override
                public void onError(FacebookException e) {
                    makeText(getContext(), "Error logging in with facebook", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            tbrFacebookPostDialogBox();
        }

        ImageButton alarm_map_button = (ImageButton) alarm_popup_view.findViewById(R.id.ctbr_map_button);

        alarm_map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCheck = 1;
                //GIVE THE CURRENT LOCATION HERE INSTEAD OF THE BELOW HARD CODED DOUBLE VALUE
                Intent a = new Intent(getContext(), TaskMapActivity.class);
                a.putExtra("taskId", 2);
                *//*a.putExtra("Longitude",curr_lon);*//*
                startActivityForResult(a, ALARM_REQUEST);
            }
        });

        ImageButton msg_map_button = (ImageButton) msg_popup_view.findViewById(R.id.ctbr_map_button);
        ImageButton msg_contact_button = (ImageButton) msg_popup_view.findViewById(R.id.ctbr_contact);

        msg_map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: NAYEEM
                buttonCheck=2;
                //GIVE THE CURRENT LOCATION HERE INSTEAD OF THE BELOW HARD CODED DOUBLE VALUE
                Intent a = new Intent(getContext(), TaskMapActivity.class);
                a.putExtra("taskId", 3);
                *//*a.putExtra("Latitude",curr_lat);
                a.putExtra("Longitude",curr_lon);*//*
                startActivityForResult(a, MESSAGE_REQUEST);
            }
        });

        msg_contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check for read contacts permission
                int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS);

                // if permission is not granted, prompt user for permission
                if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    return;
                }
                pickContact();
            }
        });

        alertDialogBuilderFBPost = new AlertDialog.Builder(getContext());
        alertDialogBuilderFBLogin = new AlertDialog.Builder(getContext());
        alertDialogBuilderAlarm = new AlertDialog.Builder(getContext());
        alertDialogBuilderMsg = new AlertDialog.Builder(getContext());

        fb_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb_popup();
            }
        });

        alarm_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm_popup();
            }
        });

        msg_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg_popup();
            }
        });

        mAdapter = new ReminderRecyclerAdapter(getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        task_remlist.setLayoutManager(mLayoutManager);
        task_remlist.setItemAnimator(new DefaultItemAnimator());
        task_remlist.setAdapter(mAdapter);
        task_remlist.addOnItemTouchListener(this);
        mAdapter.notifyDataSetChanged();

        return view;
    }

    private void tbrFacebookPostDialogBox() {
        fb_popup_view = layoutInflater.inflate(R.layout.popup_task_fb, null);

        // Get reference of widgets from XML layout
        final Spinner places_spinner = (Spinner) fb_popup_view.findViewById(R.id.places_spinner);

        String[] places = new String[]{
                "Post when i am at?",
                "Shopping Complex",
                "Food Outlet",
                "Airport"
        };

        final List<String> placesList = new ArrayList<>(Arrays.asList(places));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(), R.layout.spinner_item, placesList){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        places_spinner.setAdapter(spinnerArrayAdapter);
    }

    private void fb_popup() {
        if (fb_popup_view.getParent() != null) {
            ((ViewGroup) (fb_popup_view.getParent())).removeView(fb_popup_view);
        }

        if(AccessToken.getCurrentAccessToken() == null) {
            alertDialogBuilderFBLogin.setView(fb_popup_view);
            alertDialogBuilderFBLogin
                    .setTitle("FACEBOOK LOGIN")
                    .setMessage("Log in with your Facebook profile to allow the app to post status update to your Facebook wall.")
                    .setCancelable(false)
                    .setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            alertDialogFB = alertDialogBuilderFBLogin.create();
        }
        else {
            EditText post_msg = (EditText) fb_popup_view.findViewById(R.id.ctbr_post_msg);
            final Spinner places_spinner = (Spinner) fb_popup_view.findViewById(R.id.places_spinner);

            post_msg.setText("");
            places_spinner.setSelection(0);

            alertDialogBuilderFBPost.setView(fb_popup_view);
            alertDialogBuilderFBPost
                    .setTitle("FACEBOOK TASK")
                    .setCancelable(false)
                    .setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })

                    .setPositiveButton("SAVE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    create_fb_task();
                                }
                            });
            alertDialogFB = alertDialogBuilderFBPost.create();
        }

        alertDialogFB.show();
    }

    private void create_fb_task() {
        EditText post_msg = (EditText) fb_popup_view.findViewById(R.id.ctbr_post_msg);
        final Spinner places_spinner = (Spinner) fb_popup_view.findViewById(R.id.places_spinner);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());

        // getting application context
        Context context = getApplicationContext();
        // getting SharedPreference instance
        SharedPreferences sharedPreferences = context.getSharedPreferences("COUNTER", Context.MODE_PRIVATE);
        // getting SharedPreferences.Editor instance
        // SharedPreferences.Editor instance is required to edit the SharedPreference file
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // insert the record into the database
        long isInserted = databaseHelper.insertRecord(sharedPreferences.getInt("counter", -1), 1, "Post to my facebook wall", null, null,
                // IIIT-Delhi coordinates - lat=28.5440071,lon=77.2723876
                // Home Location - lat=28.6269387,long=77.2977636
                post_msg.getText().toString(), null, places_spinner.getSelectedItem().toString(), 28.5440071, 77.2723876, 0);

        // check whether the record is successfully inserted or not
        if(isInserted >= 1) {
            makeText(getContext(), "Reminder created.", Toast.LENGTH_LONG).show();
        }
        else {
            makeText(getContext(), "Unable to create reminder. Please try again.", Toast.LENGTH_SHORT).show();
        }

        reminderList.add(new Reminder(sharedPreferences.getInt("counter", -1), 1, "Post to my facebook wall", null, null,
                post_msg.getText().toString(), null, places_spinner.getSelectedItem().toString(), 28.5440071, 77.2723876));
        mAdapter.notifyDataSetChanged();

        editor.putInt("counter", sharedPreferences.getInt("counter", -1) + 1);
        editor.apply();

        // start the ReminderService service
        Intent intent = new Intent(getContext(), ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getContext(), 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        // start the service after 1 second
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);
    }

    private void alarm_popup() {
        if (alarm_popup_view.getParent() != null) {
            ((ViewGroup) (alarm_popup_view.getParent())).removeView(alarm_popup_view);
        }

        EditText alarm_msg = (EditText) alarm_popup_view.findViewById(R.id.ctbr_alarm_msg);
        EditText loc_alarm = (EditText) alarm_popup_view.findViewById(R.id.location_alarm);

        loc_alarm.setText("");
        alarm_msg.setText("");

        alertDialogBuilderAlarm.setView(alarm_popup_view);
        alertDialogBuilderAlarm
                .setTitle("ALARM TASK")
                .setCancelable(false)
                .setPositiveButton("SAVE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                create_alarm_task();
                            }
                        })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alertDialogAlarm = alertDialogBuilderAlarm.create();
        alertDialogAlarm.show();
    }

    private void create_alarm_task() {
        EditText alarm_msg = (EditText) alarm_popup_view.findViewById(R.id.ctbr_alarm_msg);

        *//*String location = loc_alarm.getText().toString();
         Address address;
        if (!location.equals(""))
        {
            Geocoder geocoder = new Geocoder(getActivity());
            try
            {
                addressList = geocoder.getFromLocationName(location, 1);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            //if size is 0 then wrong item is entered
            if(addressList != null) {
                if (addressList.size() == 0) {
                    Toast.makeText(getActivity(), "Wrong address.", Toast.LENGTH_SHORT).show();
                    lat = 28.5473;
                    lon = 77.2732;
                } else {
                    address = addressList.get(0);
                    //take the location coordinates from here
                    lat = address.getLatitude();
                    lon = address.getLongitude();
                    Log.d(TAG, "For alarm" + " " + lat.toString() + " " + lon.toString());
                }
            }
        }
        else {
            Toast.makeText(getActivity(), "No Location is set.", Toast.LENGTH_SHORT).show();
            lat=28.5473;lon=77.2732;
        }*//*

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());

        // getting application context
        Context context = getApplicationContext();
        // getting SharedPreference instance
        SharedPreferences sharedPreferences = context.getSharedPreferences("COUNTER", Context.MODE_PRIVATE);
        // getting SharedPreferences.Editor instance
        // SharedPreferences.Editor instance is required to edit the SharedPreference file
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // insert the record into the database
        long isInserted = databaseHelper.insertRecord(sharedPreferences.getInt("counter", -1), 2, "Trigger alarm", null, null, alarm_msg.getText().toString(),
                null, locationName, latitude, longitude, 0);

        // check whether the record is successfully inserted or not
        if(isInserted >= 1) {
            makeText(getContext(), "Reminder created.", Toast.LENGTH_LONG).show();
        }
        else {
            makeText(getContext(), "Unable to create reminder. Please try again.", Toast.LENGTH_LONG).show();
        }

        reminderList.add(new Reminder(sharedPreferences.getInt("counter", -1), 2, "Trigger alarm", null, null, alarm_msg.getText().toString(),
                null, locationName, latitude, longitude));
        mAdapter.notifyDataSetChanged();

        editor.putInt("counter", sharedPreferences.getInt("counter", -1) + 1);
        editor.apply();

        stopService = false;
        getActivity().startService(new Intent(getActivity(), ReminderService.class));

        //start the ReminderService service
        //Intent intent = new Intent(getContext(), ReminderService.class);
        //PendingIntent pendingIntent = PendingIntent.getService(getContext(), 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        // start the service after 1 second
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);
    }

    private void msg_popup() {
        if (msg_popup_view.getParent() != null) {
            ((ViewGroup) (msg_popup_view.getParent())).removeView(msg_popup_view);
        }

        EditText msg_number = (EditText) msg_popup_view.findViewById(R.id.ctbr_number);
        EditText msg_arrival = (EditText) msg_popup_view.findViewById(R.id.ctbr_message_arrival);
        EditText msg_departure = (EditText) msg_popup_view.findViewById(R.id.ctbr_message_departure);
        EditText loc_msg = (EditText) msg_popup_view.findViewById(R.id.location_msg);

        // clear out the text fields
        msg_number.setText("");
        msg_arrival.setText("");
        msg_departure.setText("");
        loc_msg.setText("");

        alertDialogBuilderMsg.setView(msg_popup_view);
        alertDialogBuilderMsg
                .setTitle("MESSAGE TASK")
                .setCancelable(false)
                .setPositiveButton("SAVE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                create_msg_task();
                            }
                        })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alertDialogMsg = alertDialogBuilderMsg.create();
        alertDialogMsg.show();
    }

    private void create_msg_task() {
        // check for send SMS permission
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS);

        // if permission is not granted, prompt user for permission
        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);

            return;
        }

        EditText msg_number = (EditText) msg_popup_view.findViewById(R.id.ctbr_number);
        EditText msg_arrival = (EditText) msg_popup_view.findViewById(R.id.ctbr_message_arrival);
        EditText msg_departure = (EditText) msg_popup_view.findViewById(R.id.ctbr_message_departure);

        *//*String location = loc_msg.getText().toString();

        List<Address> addressList = null;
        Address address;
        if (location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(getActivity());
            try
            {
                addressList = geocoder.getFromLocationName(location, 1);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            //if size is 0 then wrong item is entered
            if(addressList.size()==0) {
                Toast.makeText(getActivity(), "Wrong address", Toast.LENGTH_SHORT).show();
                lat=28.5473;lon=77.2732;
            }
            else
            {
                address = addressList.get(0);
                //take the location coordinates from here
                lat = address.getLatitude();
                lon = address.getLongitude();
                Log.d(TAG,"For msg"+" "+lat.toString()+" "+lon.toString());
            }
        }
        else
        {
            Toast.makeText(getActivity(), "No Location is Set", Toast.LENGTH_SHORT).show();
            lat=28.5473;lon=77.2732;
        }*//*


        String[] str = msg_number.getText().toString().split(":");
        String name = null;
        String number;

        if(str.length == 1) {
            number = str[0].trim();
        }
        else {
            number = str[0].trim();
            name = str[1].trim();
        }

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());

        // getting application context
        Context context = getApplicationContext();
        // getting SharedPreference instance
        SharedPreferences sharedPreferences = context.getSharedPreferences("COUNTER", Context.MODE_PRIVATE);
        // getting SharedPreferences.Editor instance
        // SharedPreferences.Editor instance is required to edit the SharedPreference file
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // insert the record into the database
        long isInserted = databaseHelper.insertRecord(sharedPreferences.getInt("counter", -1), 3, "Send message", name, number, msg_arrival.getText().toString(),
                msg_departure.getText().toString(), locationName, latitude, longitude, 0);

        // check whether the record is successfully inserted or not
        if(isInserted >= 1) {
            makeText(getContext(), "Reminder created.", Toast.LENGTH_SHORT).show();
        }
        else {
            makeText(getContext(), "Unable to create reminder. Please try again.", Toast.LENGTH_SHORT).show();
        }

        reminderList.add(new Reminder(sharedPreferences.getInt("counter", -1), 3, "Send message", name, number, msg_arrival.getText().toString(),
                msg_departure.getText().toString(), locationName, latitude, longitude));
        mAdapter.notifyDataSetChanged();

        editor.putInt("counter", sharedPreferences.getInt("counter", -1) + 1);
        editor.apply();

        // start the ReminderService service
        Intent intent = new Intent(getContext(), ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getContext(), 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        // start the service after 1 second
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        // Show user only contacts w/ phone numbers
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                @SuppressLint("Recycle")
                Cursor cursor = getContext().getContentResolver().query(contactUri, projection, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column1 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column1);
                int column2 = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(column2);

                EditText msg_number = (EditText) msg_popup_view.findViewById(R.id.ctbr_number);
                // Do something with the phone number...
                msg_number.setText(number + " : " + name);
            }
        }
        else if(requestCode == ALARM_REQUEST) {
            if(resultCode == RESULT_OK) {
                locationName = data.getStringExtra("locationName");
                latitude = data.getDoubleExtra("latitude", 0.0);
                longitude = data.getDoubleExtra("longitude", 0.0);

                Log.d("hola", locationName);

                EditText loc_alarm = (EditText) alarm_popup_view.findViewById(R.id.location_alarm);  //name of the location
                loc_alarm.setText(locationName);
            }
        }
        else if(requestCode == MESSAGE_REQUEST) {
            if(resultCode == RESULT_OK) {
                locationName = data.getStringExtra("locationName");
                latitude = data.getDoubleExtra("latitude", 0.0);
                longitude = data.getDoubleExtra("longitude", 0.0);

                EditText loc_msg = (EditText) msg_popup_view.findViewById(R.id.location_msg);  //name of the location
                loc_msg.setText(locationName);
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d("2","Inside Fragment");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickContact();
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    create_msg_task();
                }
            }
            break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        Log.d(TAG,location);
        // TODO: NAYEEM
        ////////////////////////////TO GET THE NAME OF LOCATION FROM THE MAP ////////////////////////////////
        if(buttonCheck==1)
        {
            buttonCheck=0;
            *//*EditText loc_alarm = (EditText) alarm_popup_view.findViewById(R.id.location_alarm);
            if(!location.equals(""))
                loc_alarm.setText(location);*//*
        }
        else if(buttonCheck==2)
        {
            buttonCheck=0;
            *//*EditText loc_msg = (EditText) msg_popup_view.findViewById(R.id.location_msg);
            if(!location.equals(""))
                loc_msg.setText(location);*//*
        }
        else
            buttonCheck=0;
    }

    // Motion Event Methods

    private void myToggleSelection(int idx) {
        mAdapter.toggleSelection(idx);
//        String title = getString (R.string.rem_action_mode_title, mAdapter.getSelectedItemCount());
//        rem_list_action_mode.setTitle (title);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(LOG_TAG, "onInterceptTouchEvent");
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(LOG_TAG, "onTouchEvent");
        return;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.d(LOG_TAG, "onRequestDisallowInterceptTouchEvent");
        return;
    }

    @Override
    public void onClick(View v) {
        Log.d(LOG_TAG, "onClick Entry");
        switch (v.getId()) {
            case R.id.rem_list_container_item:
                Log.d(LOG_TAG, "onClick BP1");
                int idx = task_remlist.getChildPosition(v);
                if (rem_list_action_mode != null) {
                    int selectCount;

                    myToggleSelection(idx);

                    selectCount = mAdapter.getSelectedItemCount();
                    if (selectCount == 0) {
                        rem_list_action_mode.finish();
                        rem_list_action_mode = null;
                    }
                }
                break;
            default:
                Log.d(LOG_TAG, "onClick BP2");
                break;
        }
    }

    private class RecyclerViewDemoOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = task_remlist.findChildViewUnder(e.getX(), e.getY());
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent event) {
            View view = task_remlist.findChildViewUnder(event.getX(), event.getY());

            if (rem_list_action_mode != null) {
                return;
            }

            rem_list_action_mode = ((AppCompatActivity) getActivity()).startSupportActionMode(rem_list_action_mode_callback);

            int idx = task_remlist.getChildPosition(view);
            myToggleSelection(idx);

            super.onLongPress(event);
        }
    }*/
}