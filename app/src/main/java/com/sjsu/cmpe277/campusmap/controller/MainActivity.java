package com.sjsu.cmpe277.campusmap.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.sjsu.cmpe277.campusmap.R;

public class MainActivity extends FragmentActivity implements View.OnTouchListener{

    private static final int REQUEST_ERROR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ImageView iv = (ImageView) findViewById (R.id.image);
        if (iv != null) {
            iv.setOnTouchListener (this);
        }
/*
        FragmentManager fm= getSupportFragmentManager();
        Fragment fragment= fm.findFragmentById(R.id.fragment_map_container);
        */
        /*
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_map_container);

        if (fragment == null) {
            fragment = new MapFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_map_container, fragment)
                    .commit();
        }
        */
    }

    @Override
    public boolean onTouch(View v, MotionEvent me){
        boolean flag = false;
        final int action = me.getAction();
        final int X = (int)me.getX();
        final int Y = (int)me.getY();

        ImageView image = (ImageView)v.findViewById(R.id.image);
        if(image == null)
            return false;

        Integer tagNum = (Integer) image.getTag();
        int currentResource = (tagNum==null) ? R.drawable.campusmap : tagNum.intValue();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(currentResource==R.drawable.campusmap){
                    flag = true;
                }
                else{
                    flag = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                int touchColor = getSpotColor(R.id.image_areas, X, Y);
                ColorTool ct = new ColorTool();
                int tolerance = 25;
                if(ct.closeMatch(Color.YELLOW, touchColor, tolerance)){
                    Toast.makeText(getApplicationContext(),"King Library",Toast.LENGTH_SHORT).show();
                    /*
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra("buildingName", "King Library");
                    i.putExtra("userLocation", 0);
                    startActivity(i);*/
                    /*
                    Intent i = new Intent(MainActivity.this, StreetViewActivity.class);
                    i.putExtra("latitude", -33.87365);
                    i.putExtra("longitude", 151.20689);
                    startActivity(i);
                    */
                }
                else if(ct.closeMatch(Color.BLACK, touchColor, tolerance)){
                    Toast.makeText(getApplicationContext(),"Engineering Building",Toast.LENGTH_SHORT).show();
                    /*
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra("buildingName", "Engineering Building");
                    i.putExtra("userLocation", 0);
                    startActivity(i);
                    */
                }
                else if(ct.closeMatch(Color.GREEN, touchColor, tolerance)){
                    Toast.makeText(getApplicationContext(),"Yoshihiro Uchida Hall",Toast.LENGTH_SHORT).show();
                    /*
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra("buildingName", "Yoshihiro Uchida Hall");
                    i.putExtra("userLocation", 0);
                    startActivity(i);
                    */
                }
                else if(ct.closeMatch(Color.BLUE, touchColor, tolerance)){
                    Toast.makeText(getApplicationContext(),"Student Union",Toast.LENGTH_SHORT).show();
                    /*
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra("buildingName", "Student Union");
                    i.putExtra("userLocation", 0);
                    startActivity(i);
                    */
                }
                else if(ct.closeMatch(Color.RED, touchColor, tolerance)){
                    Toast.makeText(getApplicationContext(),"BBC",Toast.LENGTH_SHORT).show();
                    /*
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra("buildingName", "BBC");
                    i.putExtra("userLocation", 0);
                    startActivity(i);
                    */
                }
                else if(ct.closeMatch(Color.GRAY, touchColor, tolerance)){
                    Toast.makeText(getApplicationContext(),"South Parking Garage",Toast.LENGTH_SHORT).show();
                    /*
                    Intent i = new Intent(MainActivity.this, BuildingActivity.class);
                    i.putExtra("buildingName", "South Parking Garage");
                    i.putExtra("userLocation", 0);
                    startActivity(i);
                    */
                }
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        return flag;
    }

    public int getSpotColor(int imageId, int X, int Y){
        ImageView image = (ImageView)findViewById(imageId);
        if (image == null) {
            Log.d ("ImageAreasActivity", "Color spot image not found");
            return 0;
        } else {
            image.setDrawingCacheEnabled(true);
            Bitmap colorSpot = Bitmap.createBitmap(image.getDrawingCache());
            if (colorSpot == null) {
                Log.d ("ImageAreasActivity", "Hot spot bitmap was not created");
                return 0;
            } else {
                image.setDrawingCacheEnabled(false);
                return colorSpot.getPixel(X,Y);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
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
}
