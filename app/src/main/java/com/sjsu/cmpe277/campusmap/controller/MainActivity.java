package com.sjsu.cmpe277.campusmap.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.sjsu.cmpe277.campusmap.R;

public class MainActivity extends FragmentActivity implements View.OnTouchListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult>{

    private static final String TAG = "MainActivity";
    private static final int REQUEST_ERROR = 0;
    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    LocationSettingsRequest mLocationSettingsRequest;

    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ImageView iv = (ImageView) findViewById(R.id.image);
        if (iv != null) {
            iv.setOnTouchListener(this);
        }

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();

    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {
        boolean flag = false;
        final int action = me.getAction();
        final int X = (int) me.getX();
        final int Y = (int) me.getY();

        ImageView image = (ImageView) v.findViewById(R.id.image);
        if (image == null)
            return false;

        Integer tagNum = (Integer) image.getTag();
        int currentResource = (tagNum == null) ? R.drawable.campusmap : tagNum.intValue();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (currentResource == R.drawable.campusmap) {
                    flag = true;
                } else {
                    flag = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                int touchColor = getSpotColor(R.id.image_areas, X, Y);
                ColorTool ct = new ColorTool();
                int tolerance = 25;
                if (ct.closeMatch(Color.YELLOW, touchColor, tolerance)) {
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra(BuildingActivity.BUILDING_NAME, "King Library");
                    i.putExtra(BuildingActivity.CURRENT_LATITUDE, mCurrentLocation.getLatitude());
                    i.putExtra(BuildingActivity.CURRENT_LONGITUDE, mCurrentLocation.getLongitude());
                    startActivity(i);
                    /*
                    Intent i = new Intent(MainActivity.this, StreetViewActivity.class);
                    i.putExtra("latitude", -121.8849988);
                    i.putExtra("longitude", 37.3355068);
                    startActivity(i);
                    */
                } else if (ct.closeMatch(Color.BLACK, touchColor, tolerance)) {
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra("buildingName", "Engineering Building");
                    i.putExtra("userLocation", mCurrentLocation);
                    startActivity(i);
                } else if (ct.closeMatch(Color.GREEN, touchColor, tolerance)) {
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra("buildingName", "Yoshihiro Uchida Hall");
                    i.putExtra("userLocation", mCurrentLocation);
                    startActivity(i);
                } else if (ct.closeMatch(Color.BLUE, touchColor, tolerance)) {
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra("buildingName", "Student Union");
                    i.putExtra("userLocation", mCurrentLocation);
                    startActivity(i);
                } else if (ct.closeMatch(Color.RED, touchColor, tolerance)) {
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra("buildingName", "BBC");
                    i.putExtra("userLocation", mCurrentLocation);
                    startActivity(i);
                } else if (ct.closeMatch(Color.GRAY, touchColor, tolerance)) {
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra("buildingName", "South Parking Garage");
                    i.putExtra("userLocation", mCurrentLocation);
                    startActivity(i);
                }
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        return flag;
    }

    public int getSpotColor(int imageId, int X, int Y) {
        ImageView image = (ImageView) findViewById(imageId);
        if (image == null) {
            Log.d("ImageAreasActivity", "Color spot image not found");
            return 0;
        } else {
            image.setDrawingCacheEnabled(true);
            Bitmap colorSpot = Bitmap.createBitmap(image.getDrawingCache());
            if (colorSpot == null) {
                Log.d("ImageAreasActivity", "Hot spot bitmap was not created");
                return 0;
            } else {
                image.setDrawingCacheEnabled(false);
                return colorSpot.getPixel(X, Y);
            }
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, REQUEST_ERROR).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // ToDo
        Toast.makeText(getApplicationContext(), "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // ToDo
        Toast.makeText(getApplicationContext(), "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        Log.d(TAG, String.format("Current Location is: (%f, %f)",
                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do the task you need to do.
                   startLocationUpdates();
                } else {
                    // permission denied, disable functionality that depends on this permission.
                    mGoogleApiClient.disconnect();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }


    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_LOCATION);
            return;
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mCurrentLocation != null) {
            Log.d(TAG, String.format("last known Location is: (%f, %f)",
                    mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }
}
