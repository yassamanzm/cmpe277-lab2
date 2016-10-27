package com.sjsu.cmpe277.campusmap.model;

/**
 * Created by Yassaman on 10/16/2016.
 */

public class Building {

    private String mName;
    private String mDescription;
    private double mLatitude;
    private double mLongitude;
    private float mLeftX;
    private float mRightX;
    private float mTopY;
    private float mBottomY;

    public Building(String name, float leftX, float rightX, float topY, float bottomY) {
        this.mName = name;
        this.mLeftX = leftX;
        this.mRightX = rightX;
        this.mTopY = topY;
        this.mBottomY = bottomY;
    }

    public Building(String name, String description, double latitude, double longitude,
                    float leftX, float rightX, float topY, float bottomY) {
        this.mName = name;
        this.mDescription = description;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mLeftX = leftX;
        this.mRightX = rightX;
        this.mTopY = topY;
        this.mBottomY = bottomY;
    }

    public String getName() {
        return mName;
    }

    public float getLeftX() {
        return mLeftX;
    }

    public float getRightX() {
        return mRightX;
    }

    public float getTopY() {
        return mTopY;
    }

    public float getBottomY() {
        return mBottomY;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    @Override
    public String toString() {
        return "Building{" +
                "mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mLeftX=" + mLeftX +
                ", mRightX=" + mRightX +
                ", mTopY=" + mTopY +
                ", mBottomY=" + mBottomY +
                '}';
    }
}
