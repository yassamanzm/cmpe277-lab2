package com.sjsu.cmpe277.campusmap.controller;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sjsu.cmpe277.campusmap.R;

import java.util.List;

public class StreetViewActivity extends FragmentActivity implements OnStreetViewPanoramaReadyCallback{

    private double latitude = 0.0;
    private double longitude = 0.0;

    // MLK = 37.3358043,-121.8860251;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view);
        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);
            StreetViewPanoramaFragment streetViewPanoramaFragment =
                    (StreetViewPanoramaFragment) getFragmentManager()
                            .findFragmentById(R.id.streetviewpanorama);
            streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        /*
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_street_container);

        if (fragment == null) {
            fragment = new StreetFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_street_container, fragment)
                    .commit();
        }
        */
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        //panorama.setPosition(new LatLng(-33.87365, 151.20689));37.3359452,-121.8857298
        panorama.setPosition(new LatLng(latitude,longitude));
    }
}
