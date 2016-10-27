package com.sjsu.cmpe277.campusmap.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjsu.cmpe277.campusmap.R;

public class BuildingFragment extends Fragment {
    // https://developers.google.com/maps/documentation/distance-matrix/intro
    // https://maps.googleapis.com/maps/api/distancematrix/json?mode=walking&origins=37.3325855,-121.9010574&destinations=San José State University Charles W. Davidson College of Engineering, 1 Washington Square, San Jose, CA 95112
    private static final String MAP_DISTANCE_MATRIX_API_KEY= "AIzaSyAUPOcTaBFpKPrQDGtMxU5GeAGsQe_fG68";

    private TextView mNameTextView;
    private TextView mDistanceTextView;
    private TextView mDurationTextView;
    private ImageView mBuildingImageView;
    private Button mStreetButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_building_detail, container, false);
        mNameTextView = (TextView) v.findViewById(R.id.building_name_text_view);
        mDistanceTextView = (TextView) v.findViewById(R.id.distance_text_view);
        mDurationTextView = (TextView) v.findViewById(R.id.duration_text_view);

        mBuildingImageView = (ImageView) v.findViewById(R.id.building_image_view);

        mStreetButton = (Button) v.findViewById(R.id.street_view_button);
        mStreetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent streetIntent= new Intent(getActivity(), StreetViewActivity.class);
//                streetIntent.putExtra(Key, Value);
                startActivity(streetIntent);
            }
        });

        return v;
    }
}