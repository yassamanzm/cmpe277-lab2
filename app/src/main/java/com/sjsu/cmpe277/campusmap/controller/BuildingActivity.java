package com.sjsu.cmpe277.campusmap.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.sjsu.cmpe277.campusmap.R;

public class BuildingActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        FragmentManager fm= getSupportFragmentManager();
        Fragment fragment= fm.findFragmentById(R.id.fragment_building_container);

        if (fragment == null) {
            fragment= new BuildingFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_building_container, fragment)
                    .commit();
        }
    }
}
