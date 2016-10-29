package com.sjsu.cmpe277.campusmap.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sjsu.cmpe277.campusmap.R;
import com.sjsu.cmpe277.campusmap.model.GoogleMatrixRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class BuildingActivity extends FragmentActivity {

    private static final String COLOR_DARK_BLUE = "#303F9F";
    private static final String TAG = "BuildingActivity";
    public static final String BUILDING_NAME = "buildingName";
    public static final String CURRENT_LATITUDE = "current_latitude";
    public static final String CURRENT_LONGITUDE = "current_longitude";

    // !!!! for test !!!! replace me with the data you get from a static map delete me later
    private static final String BUILDING_ADDRESS = "Dr. Martin Luther King, Jr. Library, 150 East San Fernando Street, San Jose, CA 95112";

    private String mName;
    RequestQueue mRequestQueue;

    private TextView mNameTextView;
    private TextView mDistanceTextView;
    private TextView mWalkingDurationTextView;
    private TextView mDrivingDurationTextView;
    private ImageView mBuildingImageView;
    private Button mStreetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        // TODO should save the instance and retrieve it to handle rotation
        mRequestQueue = Volley.newRequestQueue(this);

        // retrieve extras
        double currentLatitude = 0;
        double currentLongitude = 0;
        Intent buildingIntent = getIntent();
        Bundle infoBundle = buildingIntent.getExtras();
        if (infoBundle != null) {
            mName = infoBundle.getString(BUILDING_NAME);
            currentLatitude = infoBundle.getDouble(CURRENT_LATITUDE);
            currentLongitude = infoBundle.getDouble(CURRENT_LONGITUDE);
        }

        // Name
        mNameTextView = (TextView) findViewById(R.id.building_name_text_view);
        mNameTextView.setText(mName);

        // Image
        // TODO: find the right image based on the name
        mBuildingImageView = (ImageView) findViewById(R.id.building_image_view);
        mBuildingImageView.setImageResource(R.drawable.mlk);

        // Street view button
        mStreetButton = (Button) findViewById(R.id.street_view_button);
        mStreetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent streetIntent = new Intent(BuildingActivity.this, StreetViewActivity.class);
                // TODO in StreetView activity retrieve lat/long of the address based on the name
                streetIntent.putExtra(BUILDING_NAME, mName);
                startActivity(streetIntent);
            }
        });

        // Travel Info
        mDistanceTextView = (TextView) findViewById(R.id.distance_text_view);
        mWalkingDurationTextView = (TextView) findViewById(R.id.duration_walking_text_view);
        mDrivingDurationTextView = (TextView) findViewById(R.id.duration_driving_text_view);
        clearTexts();

        // Instantiate the RequestQueue.

        String walkingUrl = null;
        String drivingUrl = null;
        try {
            walkingUrl = GoogleMatrixRequest.createRequest(currentLatitude, currentLongitude, BUILDING_ADDRESS,
                    GoogleMatrixRequest.MODE_WALKING, GoogleMatrixRequest.UNITS_IMPERIAL);
            drivingUrl = GoogleMatrixRequest.createRequest(currentLatitude, currentLongitude, BUILDING_ADDRESS,
                    GoogleMatrixRequest.MODE_DRIVING, GoogleMatrixRequest.UNITS_IMPERIAL);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        // Request a json response from the provided URL.
        JsonObjectRequest walkingJsonRequest = getWalkingDistance(walkingUrl);
        JsonObjectRequest drivingJsonRequest = getDrivingInfo(drivingUrl);

        // Add the request to the RequestQueue.
        walkingJsonRequest.setTag(TAG);
        drivingJsonRequest.setTag(TAG);
        mRequestQueue.add(walkingJsonRequest);
        mRequestQueue.add(drivingJsonRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    private JsonObjectRequest getWalkingDistance(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray rows = response.getJSONArray("rows");
                            JSONObject elementsObject = rows.getJSONObject(0);
                            JSONArray elements = elementsObject.getJSONArray("elements");
                            JSONObject travelInfoObject = elements.getJSONObject(0);

                            // Distance
                            JSONObject distanceObject = travelInfoObject.getJSONObject("distance");
                            SpannableString spannedDistanceString= formatText(getString(R.string.distance, distanceObject.getString("text")),
                                    0, 10, COLOR_DARK_BLUE);
                            mDistanceTextView.setText(spannedDistanceString);

                            // Duration
                            JSONObject durationObject = travelInfoObject.getJSONObject("duration");
                            SpannableString spannedDurationString= formatText(getString(R.string.walking_duration, durationObject.getString("text")),
                                    0, 9, COLOR_DARK_BLUE);
                            mWalkingDurationTextView.setText(spannedDurationString);

                        } catch (JSONException e) {
                            Log.e(TAG, e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getLocalizedMessage());
                        clearTexts();
                        Toast.makeText(getApplicationContext(), R.string.volley_request_error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        return request;
    }

    private JsonObjectRequest getDrivingInfo(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray rows = response.getJSONArray("rows");
                            JSONObject elementsObject = rows.getJSONObject(0);
                            JSONArray elements = elementsObject.getJSONArray("elements");
                            JSONObject travelInfoObject = elements.getJSONObject(0);

                            // ToDo driving distance might be different from walking distance
                            // Duration
                            JSONObject durationObject = travelInfoObject.getJSONObject("duration");
                            SpannableString spannedDurationString= formatText(getString(R.string.driving_duration, durationObject.getString("text")),
                                    0, 9, COLOR_DARK_BLUE);
                            mDrivingDurationTextView.setText(spannedDurationString);

                        } catch (JSONException e) {
                            Log.e(TAG, e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getLocalizedMessage());
                        clearTexts();
                        Toast.makeText(getApplicationContext(), R.string.volley_request_error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        return request;
    }

    private void clearTexts() {
        mDistanceTextView.setText(getString(R.string.distance, ""));
        mWalkingDurationTextView.setText(getString(R.string.walking_duration, ""));
        mDrivingDurationTextView.setText(getString(R.string.driving_duration, ""));
    }

    private SpannableString formatText(String text, int startIndx, int endIndx, String color) {
        SpannableString spannedString=  new SpannableString(text);
        spannedString.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndx, endIndx, 0);
        return spannedString;
    }
}
