package com.sjsu.cmpe277.campusmap.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sjsu.cmpe277.campusmap.R;
import com.sjsu.cmpe277.campusmap.view.CampusView;

public class MapFragment extends Fragment {

    private CampusView mCampusView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v= inflater.inflate(R.layout.fragment_map, container, false);
        mCampusView= (CampusView)v.findViewById(R.id.campus_view);
        mCampusView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent buildingIntent= new Intent(getActivity(), BuildingActivity.class);
//                streetIntent.putExtra(Key, Value);
                startActivity(buildingIntent);
                return false;
            }
        });

        return v;
    }
}
