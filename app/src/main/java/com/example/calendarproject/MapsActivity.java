package com.example.calendarproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final static int REQUEST_lOCATION = 0;
    private LatLng curLocation;
    private boolean flag = false;
    private boolean getLocationFlag = false;
    private Button buttonSave;
    private double latitude, longitude, nowLocationLongitude, nowLocationLatitude;
    private String markedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        longitude = getIntent().getDoubleExtra("Longitude", 0);
        latitude = getIntent().getDoubleExtra("Latitude", 0);

        ZoomControls zoom = (ZoomControls) findViewById(R.id.zoom);
        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        buttonSave = findViewById(R.id.button_save_location);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                //if(flag){
                    returnIntent.putExtra("Longitude",curLocation.longitude);
                    returnIntent.putExtra("Latitude",curLocation.latitude);
                    returnIntent.putExtra("Location", markedLocation);
                    setResult(Activity.RESULT_OK,returnIntent);
                    Toast.makeText(MapsActivity.this, "Location is saved succesfully.", Toast.LENGTH_SHORT).show();
                //}
                //else
                //    setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        nowLocationLatitude = latitude;
        nowLocationLongitude = longitude;


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);


            Log.d("Location", "flag = " + getLocationFlag);
            if(longitude == 0.0 && latitude == 0.0){
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(3000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                                        .removeLocationUpdates(this);
                                if(locationResult != null && locationResult.getLocations().size() > 0){
                                    int lastLocationIndex = locationResult.getLocations().size() - 1 ;
                                    nowLocationLatitude = locationResult.getLocations().get(lastLocationIndex).getLatitude();
                                    nowLocationLongitude = locationResult.getLocations().get(lastLocationIndex).getLongitude();

                                    LatLng currLocation = new LatLng(nowLocationLatitude, nowLocationLongitude);
                                    mMap.addMarker(new MarkerOptions().position(currLocation).title(findAdress(nowLocationLatitude, nowLocationLongitude)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
                                }
                            }
                        }, Looper.myLooper());

            }else{
                LatLng currLocation = new LatLng(nowLocationLatitude, nowLocationLongitude);
                mMap.addMarker(new MarkerOptions().position(currLocation).title(findAdress(nowLocationLatitude, nowLocationLongitude)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_lOCATION);
            }
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                flag = true;
                mMap.clear();

                Geocoder myLocation = new Geocoder(MapsActivity.this, Locale.getDefault());
                List<Address> myList = null;
                try {
                    myList = myLocation.getFromLocation(latLng.latitude,latLng.longitude, 1);       //Getting location name
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = (Address) myList.get(0);
                markedLocation = address.getAddressLine(0);

                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(markedLocation);
                mMap.addMarker(markerOptions);
                curLocation = latLng;
            }
        });

    }

    public String findAdress(double latitude, double longitude){
        Geocoder myLocation = new Geocoder(MapsActivity.this, Locale.getDefault());
        List<Address> myList = null;
        try {
            myList = myLocation.getFromLocation(latitude,longitude, 1);       //Getting location name
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = (Address) myList.get(0);
        String markedLocation = address.getAddressLine(0);
        return markedLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_lOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getLocationFlag = true;
                }
            } else {
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
