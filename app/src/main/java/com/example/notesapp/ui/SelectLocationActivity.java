package com.example.notesapp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.ui.fragments.EditorFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerView restaurantsRv;
    SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    Button confirmBtn;
    LatLng latLng=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        getSupportActionBar().setTitle("Your location");


        //initializing xml views....

        confirmBtn=findViewById(R.id.confirmBtn);

        //setting up map fragment to show google map
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapFragment);
        supportMapFragment.getMapAsync(this);


        requestPermissionLocation();


        //click event on confirm button to navigate back

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLng==null){
                    Toast.makeText(SelectLocationActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                }else {
                    EditorFragment.latitude=String.valueOf(latLng.latitude);
                    EditorFragment.longitude=String.valueOf(latLng.longitude);
                    Toast.makeText(SelectLocationActivity.this, "Location confirmed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }


    private void requestPermissionLocation() {

        //asking location permission from user

        if (ActivityCompat.checkSelfPermission(SelectLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();

        } else {
            ActivityCompat.requestPermissions(SelectLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1122);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //getting result of permission from user to check if granted or denied

        if (requestCode == 1122) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
                // enableLoc();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void getCurrentLocation() {


       //getting current location of user.....

        LocationServices.getFusedLocationProviderClient(SelectLocationActivity.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        if (location!=null){


                            latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.setMyLocationEnabled(true);

                            //focusing and zooming map on current location of user

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));


                        }else {
                             
                            enableLoc();
                        }


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SelectLocationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        //showing map when ready


        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        }
    }


    private void enableLoc() {

        //showing the enable permission dialog to user

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(SelectLocationActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(SelectLocationActivity.this, REQUEST_LOCATION);

//                                finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Finding Location...", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getCurrentLocation();
                }
            },2000);

        }
    }






}