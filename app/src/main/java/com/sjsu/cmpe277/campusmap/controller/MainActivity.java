package com.sjsu.cmpe277.campusmap.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.sjsu.cmpe277.campusmap.R;

public class MainActivity extends FragmentActivity {
<<<<<<< HEAD
=======

    private static final int REQUEST_ERROR = 0;
>>>>>>> parent of 73e5162... Merge branch 'master' into bkNarahari

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

<<<<<<< HEAD
        FragmentManager fm= getSupportFragmentManager();
        Fragment fragment= fm.findFragmentById(R.id.fragment_map_container);
=======
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_map_container);
>>>>>>> parent of 73e5162... Merge branch 'master' into bkNarahari

        if (fragment == null) {
            fragment = new MapFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_map_container, fragment)
                    .commit();
        }
<<<<<<< HEAD
=======
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
>>>>>>> parent of 73e5162... Merge branch 'master' into bkNarahari
    }
}
