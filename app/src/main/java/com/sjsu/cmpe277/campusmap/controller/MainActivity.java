package com.sjsu.cmpe277.campusmap.controller;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sjsu.cmpe277.campusmap.R;

public class MainActivity extends FragmentActivity implements View.OnTouchListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ImageView iv = (ImageView) findViewById (R.id.image);
        if (iv != null) {
            iv.setOnTouchListener (this);
        }
        Toast.makeText(getApplicationContext(), "Touch the screen to discover where the regions are.", Toast.LENGTH_LONG);
/*
        FragmentManager fm= getSupportFragmentManager();
        Fragment fragment= fm.findFragmentById(R.id.fragment_map_container);

        if (fragment == null) {
            fragment= new MapFragment();
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
                int touchColor = getHotspotColor(R.id.image_areas, X, Y);
                ColorTool ct = new ColorTool();
                int tolerance = 25;
                if(ct.closeMatch(Color.YELLOW, touchColor, tolerance)){

                }
                else{
                    //Toast.makeText(getApplicationContext(),"Bad",Toast.LENGTH_LONG).show();
                }
                flag = true;
                break;
            default:
                //Toast.makeText(getApplicationContext(),"Bye",Toast.LENGTH_LONG).show();
                flag = false;
                break;
        }
        return flag;
    }

    public int getHotspotColor(int imageId, int X, int Y){
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

}
