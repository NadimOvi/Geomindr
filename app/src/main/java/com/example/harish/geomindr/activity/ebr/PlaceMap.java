package com.example.harish.geomindr.activity.ebr;

import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.harish.geomindr.R;
import com.example.harish.geomindr.database.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlaceMap extends FragmentActivity implements LocationListener, OnMapReadyCallback {

    Double currLat,currLong;
    String entityLat,entityLon;
    String entityName;

    String entity;
    DatabaseHelper myDb;
    Double entLat,entLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        entity = this.getIntent().getStringExtra("map");
        currLat = this.getIntent().getDoubleExtra("map1",-9.99);
        currLong = this.getIntent().getDoubleExtra("map2",-9.99);
        entityLat = this.getIntent().getStringExtra("map3");
        entityLon = this.getIntent().getStringExtra("map4");
        entityName = this.getIntent().getStringExtra("map5");
        entLat = Double.parseDouble(entityLat);
        entLng = Double.parseDouble(entityLon);

        //deleting the entry in the database
        myDb = DatabaseHelper.getInstance(this);
        Integer deleted = myDb.deleteData(entity);
        if(deleted<=0)
            Toast.makeText(this, "Nothing is deleted", Toast.LENGTH_SHORT).show();

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        fragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }

        //Setting markers for user's current location and entity location
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(currLat,currLong);
        markerOptions.position(latLng);
        markerOptions.title("You are here !");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        LatLng latLng1 = new LatLng(entLat,entLng);
        markerOptions.position(latLng1);
        markerOptions.title(entityName);
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}