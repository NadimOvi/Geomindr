package com.example.harish.geomindr.activity.map;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.harish.geomindr.R;
import com.example.harish.geomindr.service.main.ReminderService;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.harish.geomindr.service.main.ReminderService.lastLocation;
import static com.example.harish.geomindr.service.main.ReminderService.stopService;

public class TaskMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    EditText mLocation;
    Button mBtnUseLocation, mBtnSearch;
    FloatingActionButton mBtnMyLocation;

    String locationName;
    String hint;
    Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_map);
        setTitle("Add Location");

        locationName = null;
        latitude = 0.0;
        longitude = 0.0;

        mLocation = (EditText) findViewById(R.id.location);
        mBtnUseLocation = (Button) findViewById(R.id.btnUseLocation);
        mBtnSearch = (Button) findViewById(R.id.btnSearch);
        mBtnMyLocation = (FloatingActionButton) findViewById(R.id.fabMyLocation);

        mBtnUseLocation.getBackground().setColorFilter(ResourcesCompat.getColor(getResources(),
                R.color.colorPrimaryDark, null), PorterDuff.Mode.MULTIPLY);
        mBtnSearch.getBackground().setColorFilter(ResourcesCompat.getColor(getResources(),
                R.color.colorPrimaryDark, null), PorterDuff.Mode.MULTIPLY);

        mBtnUseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            // send back locationName, latitude and longitude
            public void onClick(View view) {
                Intent returnIntent = new Intent();

                if (locationName == null) {
                    Toast.makeText(TaskMapActivity.this, "You have not selected any location.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                returnIntent.putExtra("locationName", locationName);
                returnIntent.putExtra("latitude", latitude);
                returnIntent.putExtra("longitude", longitude);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();

                String location = mLocation.getText().toString();

                List<Address> addressList = null;

                if (!location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(TaskMapActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //if size is 0 then wrong item is entered
                    if(addressList != null) {
                        if (addressList.size() == 0) {
                            Toast.makeText(TaskMapActivity.this, "Invalid location.", Toast.LENGTH_SHORT).show();
                            mLocation.setText("");
                        } else {
                            Address address = addressList.get(0);

                            String cityName = address.getAddressLine(0);
                            String stateName = address.getAddressLine(1);
                            String countryName = address.getAddressLine(2);

                            locationName = "";
                            if (cityName != null) {
                                locationName += cityName;
                            }
                            if (stateName != null) {
                                locationName += ", " + stateName;
                            }
                            if (countryName != null) {
                                locationName += ", " + countryName;
                            }
                            latitude = address.getLatitude();
                            longitude = address.getLongitude();

                            LatLng latLng = new LatLng(latitude, longitude);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title(locationName);
                            mMap.addMarker(markerOptions);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(address.getLatitude(), address.getLongitude()), 14.0f));
                        }
                    }
                }
                else {
                    Toast.makeText(TaskMapActivity.this, "Please enter a location.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First check if the GPS is enabled.
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                // Prompt user to enable GPS if it is not enabled.
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showEnableGpsAlertDialogBox();
                }
                else {
                    if (lastLocation != null) {
                        Geocoder geocoder = new Geocoder(TaskMapActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(lastLocation.getLatitude(),
                                    lastLocation.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(addresses != null) {
                            String cityName = addresses.get(0).getAddressLine(0);
                            String stateName = addresses.get(0).getAddressLine(1);
                            String countryName = addresses.get(0).getAddressLine(2);

                            locationName = "";
                            if (cityName != null) {
                                locationName += cityName;
                            }
                            if (stateName != null) {
                                locationName += ", " + stateName;
                            }
                            if (countryName != null) {
                                locationName += ", " + countryName;
                            }
                            latitude = lastLocation.getLatitude();
                            longitude = lastLocation.getLongitude();
                            setFocus();
                        }
                    }
                    else {
                        Toast.makeText(TaskMapActivity.this, "Unable to retrieve your location. " +
                                        "Please wait for few moments to allow the app to retrieve your location.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // set respective hints
        if(getIntent().getExtras().getInt("taskId") == 2) {
            TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayoutLocation);
            textInputLayout.setHint("Trigger alarm at location?");
            hint = "Trigger alarm at this location";
            mBtnUseLocation.setText(hint);
        }
        else if(getIntent().getExtras().getInt("taskId") == 3) {
            TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayoutLocation);
            textInputLayout.setHint("Send message at location?");
            hint = "Send message at this location";
            mBtnUseLocation.setText(hint);
        }

        startReminderService();
        setUpMapIfNeeded();
    }

    private void startReminderService() {
        stopService = false;
        Intent intent = new Intent(TaskMapActivity.this, ReminderService.class);
        startService(intent);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void setFocus() {
        if (lastLocation != null) {
            mMap.clear();
            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(locationName);
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        }
    }

    // Showing GPS settings alert dialog.
    // It will take user to GPS settings menu so that user can enable GPS.
    private void showEnableGpsAlertDialogBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title.
        alertDialog.setTitle("GPS ALERT");

        // Setting Dialog Message.
        alertDialog.setMessage("GPS is not enabled. Please enable it to allow the app to work properly.");

        // To prevent dismiss dialog box on back key pressed.
        alertDialog.setCancelable(false);

        // On pressing Settings button, take user to GPS settings menu.
        alertDialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // On pressing cancel button, dismiss the dialog box
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}