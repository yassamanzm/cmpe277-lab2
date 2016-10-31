package com.sjsu.cmpe277.campusmap.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
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
import com.sjsu.cmpe277.campusmap.model.Building;
import com.sjsu.cmpe277.campusmap.model.Information;

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

    private ImageView mCampusImage;
    private Bitmap mBitmap;
    private boolean mShowUserLocation = false;
    private int userX;
    private int userY;
    private String searchQuery;

    private int rect_1_x = 94;
    private int rect_1_y = 734;
    private int rect_2_x = 1298;
    private int rect_2_y = 734;
    private int rect_3_x = 94;
    private int rect_3_y = 1736;
    private int rect_4_x = 1298;
    private int rect_4_y = 1736;

    private double rect_a_x = 37.335822;
    private double rect_a_y = -121.886025;
    private double rect_b_x = 37.338846;
    private double rect_b_y = -121.879700;
    private double rect_c_x = 37.331568;
    private double rect_c_y = -121.882838;
    private double rect_d_x = 37.334563;
    private double rect_d_y = -121.876487;
    private double lat = 0.0;
    private double lon = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // this activity only supports portrait orientation
        // create a bitmap from the campus image so later on we can draw on it
        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.campusmap,myOptions);

        // Search Bar Implementation
        SearchView searchView =(SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Search Building");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                shouldHighlightBuilding(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                shouldHighlightBuilding(newText);
                return false;
            }
        });

        mCampusImage = (ImageView) findViewById(R.id.image);
        if (mCampusImage != null) {
            mCampusImage.setOnTouchListener(this);
        }

        ImageButton compassImage = (ImageButton) findViewById(R.id.compass_image);
        compassImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mShowUserLocation){
                    // Write the delete userlocation icon here
                }
                else{
                    if(inBounds(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())){
                        mShowUserLocation = true;
                        // calculate user's x and y here
                        changeLatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        userX = (int)Math.round(calculateX());
                        userY = (int)Math.round(calculateY());
                        drawUserLocation();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"You Need to be on SJSU Campus to be able to show the marker here", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();
    }

    public boolean inBounds(double x, double y){
        if((x >= rect_c_x)&&(x <= rect_b_x)&&(y >= rect_a_y)&&( y <= rect_d_y)){
            return true;
        }
        else{
            return false;
        }
    }

    public void changeLatLng(double x, double y){
        double theta = 0.0;
        if(x > rect_a_x){
            double x1 = Math.abs(x) - Math.abs(rect_a_x);
            x1 = Math.pow(x1, 2);
            double y1 = Math.abs(y)- Math.abs(rect_a_y);
            y1 = Math.pow(y1, 2);
            double p = Math.sqrt(x1 + y1);
            double value = Math.toRadians((rect_a_x-x)/p);
            theta = Math.sinh(value);
            theta = 25 - theta;
            lat = rect_a_x - (p*Math.sin(theta));
            lon = rect_a_y + (p*Math.cos(theta));
        }
        else{
            double x1 = Math.abs(rect_a_x) - Math.abs(x);
            x1 = Math.pow(x1, 2);
            double y1 = Math.abs(rect_a_y) - Math.abs(y);
            y1 = Math.pow(y1, 2);
            double p = Math.sqrt(x1 + y1);
            double value = Math.toRadians((rect_a_x-x)/p);
            theta = Math.sinh(value);
            theta = (int)(25 + theta);
            lat = rect_a_x - (p*Math.sin(theta));
            lon = rect_a_y + (p*Math.cos(theta));
        }
    }

    public double calculateX(){
        double xy = (((rect_2_x - rect_1_x)*(lon - rect_a_y))/(rect_b_y - rect_a_y));
        double px = xy + (double)rect_1_x ;
        return px;
    }

    public double calculateY(){
        double xy = (((rect_3_y - rect_1_y)*(lat - rect_a_x))/(rect_b_x - rect_a_x));
        double py = xy + (double)rect_1_y ;
        return py;
    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {
        boolean flag = false;
        final int action = me.getAction();
        final int X = (int) me.getX();
        final int Y = (int) me.getY();

        Log.d("***XY***", "(" + X + ", " + Y + ")");

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
                if ((touchColor==0)||(touchColor == -1)){

                }
                else{
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    if (ct.closeMatch(Color.YELLOW, touchColor, tolerance)) {
                        i.putExtra(BuildingActivity.BUILDING_NAME, "King Library");
                        Location lat = mCurrentLocation;
                        i.putExtra(BuildingActivity.CURRENT_LATITUDE, mCurrentLocation.getLatitude());
                        i.putExtra(BuildingActivity.CURRENT_LONGITUDE, mCurrentLocation.getLongitude());
                        i.putExtra(BuildingActivity.PIC_FLAG,1);
                    } else if (ct.closeMatch(Color.BLACK, touchColor, tolerance)) {
                        i.putExtra(BuildingActivity.BUILDING_NAME, "Engineering Building");
                        Location lat = mCurrentLocation;
                        i.putExtra(BuildingActivity.CURRENT_LATITUDE, mCurrentLocation.getLatitude());
                        i.putExtra(BuildingActivity.CURRENT_LONGITUDE, mCurrentLocation.getLongitude());
                        i.putExtra(BuildingActivity.PIC_FLAG,2);
                    } else if (ct.closeMatch(Color.GREEN, touchColor, tolerance)) {
                        i.putExtra(BuildingActivity.BUILDING_NAME, "Yoshihiro Uchida Hall");
                        Location lat = mCurrentLocation;
                        i.putExtra(BuildingActivity.CURRENT_LATITUDE, mCurrentLocation.getLatitude());
                        i.putExtra(BuildingActivity.CURRENT_LONGITUDE, mCurrentLocation.getLongitude());
                        i.putExtra(BuildingActivity.PIC_FLAG,3);
                    } else if (ct.closeMatch(Color.BLUE, touchColor, tolerance)) {
                        i.putExtra(BuildingActivity.BUILDING_NAME, "Student Union");
                        Location lat = mCurrentLocation;
                        i.putExtra(BuildingActivity.CURRENT_LATITUDE, mCurrentLocation.getLatitude());
                        i.putExtra(BuildingActivity.CURRENT_LONGITUDE, mCurrentLocation.getLongitude());
                        i.putExtra(BuildingActivity.PIC_FLAG,4);
                    } else if (ct.closeMatch(Color.RED, touchColor, tolerance)) {
                        i.putExtra(BuildingActivity.BUILDING_NAME, "Boccardo Business Complex");
                        Location lat = mCurrentLocation;
                        i.putExtra(BuildingActivity.CURRENT_LATITUDE, mCurrentLocation.getLatitude());
                        i.putExtra(BuildingActivity.CURRENT_LONGITUDE, mCurrentLocation.getLongitude());
                        i.putExtra(BuildingActivity.PIC_FLAG,5);
                    } else if (ct.closeMatch(Color.GRAY, touchColor, tolerance)) {
                        i.putExtra(BuildingActivity.BUILDING_NAME, "South Parking Garage");
                        Location lat = mCurrentLocation;
                        i.putExtra(BuildingActivity.CURRENT_LATITUDE, mCurrentLocation.getLatitude());
                        i.putExtra(BuildingActivity.CURRENT_LONGITUDE, mCurrentLocation.getLongitude());
                        i.putExtra(BuildingActivity.PIC_FLAG,6);
                    }
                    if(isNetworkOn()){
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"You Need Active Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        return flag;
    }

    public boolean isNetworkOn(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null){
            return true;
        }
        else{
            return false;
        }
    }

    public int getSpotColor(int imageId, int X, int Y) {
        ImageView image = (ImageView) findViewById(imageId);
        /*
        image.setDrawingCacheEnabled(true);
        Bitmap colorSpot = Bitmap.createBitmap(image.getDrawingCache());
        image.setDrawingCacheEnabled(false);
        return colorSpot.getPixel(X, Y);
        */
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
//        if (mBitmap != null)
//            mBitmap.recycle();
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

    private void drawUserLocation() {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        Bitmap workingBitmap = Bitmap.createBitmap(mBitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawCircle((int)(userX/2.15), (int)((userY - 300)/2.14), 10, paint);

        mCampusImage.setAdjustViewBounds(true);
        mCampusImage.setImageBitmap(mutableBitmap);
        mCampusImage.invalidate();
    }

    private void highlightBuilding(float leftX, float topY, float rightX, float bottomY ) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        Bitmap workingBitmap = Bitmap.createBitmap(mBitmap);
        Bitmap mutableBitmap  = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mutableBitmap.setHasAlpha(true);

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawRoundRect(new RectF(leftX, topY, rightX, bottomY), 2, 2, paint);

        if (mShowUserLocation) {
            canvas.drawCircle((int)(userX/2.15), (int)((userY - 300)/2.14), 10, paint);
        }

        mCampusImage.setAdjustViewBounds(true);
        mCampusImage.setImageBitmap(mutableBitmap);
        mCampusImage.invalidate();
    }

    private void clearMap() {
        if (mBitmap != null) {
            mCampusImage.setImageBitmap(mBitmap);
            mCampusImage.invalidate();
        }
    }

    private boolean shouldHighlightBuilding(String name) {
        String lowerName= name.toLowerCase();
        Building building = null;

        if (Information.BUILDING_MAP.containsKey(lowerName) ) {
            building = Information.BUILDING_MAP.get(lowerName);
            Log.d(TAG, "*** " + lowerName);
        } else if (Information.SHORT_NAME_MAP.containsKey(lowerName)) {
            name = Information.SHORT_NAME_MAP.get(lowerName).toLowerCase();
            building = Information.BUILDING_MAP.get(name);
            Log.d(TAG, "*** query: " + lowerName);
            Log.d(TAG, "*** Full name: " + name);
        }
        if (building != null) {
            Log.d(TAG, "Rectangle Coordinates: (" + building.getLeftX()  + ", " + building.getTopY() + ") " +
                    "( " + building.getRightX() + ", " +  building.getBottomY() + ")");
            // draw the rectangle or put a marker
            highlightBuilding(building.getLeftX(), building.getTopY(), building.getRightX(), building.getBottomY());
            return true;
        }
        // the string couldn't be found, clear the map if necessary
        clearMap();
        if (mShowUserLocation) {
            drawUserLocation();
        }
//        Toast.makeText(getBaseContext(), R.string.not_found_building, Toast.LENGTH_LONG).show();
        return false;
    }
}