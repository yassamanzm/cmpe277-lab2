package com.sjsu.cmpe277.campusmap.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class GoogleMatrixRequest {

    // https://developers.google.com/maps/documentation/distance-matrix/intro
    // https://maps.googleapis.com/maps/api/distancematrix/json?mode=walking&origins=37.3325855,-121.9010574&destinations=San José State University Charles W. Davidson College of Engineering, 1 Washington Square, San Jose, CA 95112
    private static final String GOOGLE_DISTANCE_MATRIX_API_KEY = "AIzaSyAUPOcTaBFpKPrQDGtMxU5GeAGsQe_fG68";
    private static final String BASE_DISTANCE_MATRIX_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    public static final String MODE_WALKING = "walking";
    public static final String MODE_DRIVING = "driving";
    public static final String UNITS_IMPERIAL = "imperial";
    public static final String UNITS_METRIC = "metric";

    public static String createRequest(double originLatitude, double originLongitude,
                                       String destinationAddress,
                                       String mode, String unit) throws UnsupportedEncodingException {
        String requestUrl = "";
        requestUrl = String.format("%sorigins=%f,%f&destinations=%s&mode=%s&units=%s&key=%s",
                BASE_DISTANCE_MATRIX_URL,
                originLatitude, originLongitude,
                URLEncoder.encode(destinationAddress, "UTF-8"),
                mode, unit, GOOGLE_DISTANCE_MATRIX_API_KEY);
        return requestUrl;
    }
}
