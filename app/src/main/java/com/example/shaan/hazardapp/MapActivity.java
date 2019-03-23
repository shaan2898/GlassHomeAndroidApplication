package com.example.shaan.hazardapp;

import android.Manifest;
import android.accounts.Account;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(MapActivity.this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        //call the map
        mMap = googleMap;

        //zoom into user's current location
        if (mLocationPermissionsGranted) { //if user has location permissions enabled
            getDeviceLocation(); //call function that gets current location and zooms into it
            //getLocationPermission will find location but wont mark it on the map, so now we mark it

            //mark location on map
            //will mark a little blue dot on map of location
            //permission check, need it to use the setMyLocation call (it automatically pops up dont need to know this)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true); //this will put blue dot, and also a center button
            //the center button is gonna be covered tho when we add a search bar below so we will create a image to act as center button

           // mMap.getUiSettings().setMyLocationButtonEnabled(false); //will remove the set my location button
            mMap.getUiSettings().isCompassEnabled();
            mMap.getUiSettings().isZoomGesturesEnabled();
        }


    }

    private static final Integer ERROR_DIALOG_REQUEST = 9001;
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final Integer LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap; //the actual map
    private FusedLocationProviderClient mFusedLocationProviderClient; //get device location


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      isServicesOK();
      getLocationPermission();


    }

     private void getDeviceLocation(){
        //gets devices current location (obvious)
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            //we were able to find the user's current location
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                           LatLng cor =  new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                        //pass current lat and long into moveCamera function, and also zoom Default frames in, found in declaration
                            moveCamera(cor, DEFAULT_ZOOM);
                        }
                        else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch(SecurityException e){
             Log.d(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }


     }


     //move the map camera to user location\
    private void moveCamera(LatLng latLng, float zoom){

        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }

    private void initMap(){
        //it will start the map in map id from xml file
        Log.d(TAG, "initMap: initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission(){

        Log.d(TAG, "getLocationPermission: getting location permissions");

        String[] permissions = {FINE_LOCATION, COURSE_LOCATION};

        if(ContextCompat.checkSelfPermission( this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission( this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            }
            else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){

            case 1234:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length;i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionResult: permission failed.");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionResult: permission granted.");
                    mLocationPermissionsGranted = true;
                    //initialize our map if all requests true
                    initMap();
                }
            }
            break;

        }
    }

    //method to check if user has correct version of Google Play Services for MAPS
    public boolean isServicesOK(){

        Log.d(TAG, "isServicesOK: checking google services version");

        int avail = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapActivity.this);

        if(avail == ConnectionResult.SUCCESS){

            //everything is OK
            Log.d(TAG,"isServicesOK: Google Play Services is working");
            return true;

        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(avail)){

            //if the error is resolvable. An error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            //get error resolve from google
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapActivity.this,avail,ERROR_DIALOG_REQUEST);
            dialog.show();

        }
        else {
            Toast.makeText(MapActivity.this,"You can't make map requests", Toast.LENGTH_SHORT).show();
        }

        return false ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.Settings:

                startActivity(new Intent(this,SettingsActivity.class ));
                break;

            case R.id.dashboard:

                startActivity(new Intent(this,AccountActivity.class ));
                break;
            case R.id.AccountSettings:

                startActivity(new Intent(this,AccountSettings.class ));
                break;
        }


        return true;
    }


}
