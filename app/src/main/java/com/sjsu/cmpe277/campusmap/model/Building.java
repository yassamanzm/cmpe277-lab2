package com.sjsu.cmpe277.campusmap.model;

public class Building {

    private String mFullName;
    private String mShortName;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;
    private float mLeftX;
    private float mRightX;
    private float mTopY;
    private float mBottomY;

    public Building(String shortName, String address, double latitude, double longitude) {
        this.mShortName = shortName;
        this.mAddress = address;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public Building(String fullName, String shortName, String address, double latitude,
                    double longitude, float leftX, float topY, float rightX, float bottomY) {
        this.mFullName = fullName;
        this.mShortName = shortName;
        this.mAddress = address;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mLeftX = leftX;
        this.mRightX = rightX;
        this.mTopY = topY;
        this.mBottomY = bottomY;
    }

    public void setShortName(String shortName) {
        this.mShortName = shortName;
    }

    public void setLeftX(float leftX) {
        this.mLeftX = leftX;
    }

    public void setRightX(float rightX) {
        this.mRightX = rightX;
    }

    public void setTopY(float topY) {
        this.mTopY = topY;
    }

    public void setBottomY(float bottomY) {
        this.mBottomY = bottomY;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getShortName() {
        return mShortName;
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

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
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
                "mFullName='" + mFullName + '\'' +
                ", mShortName='" + mShortName + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mLeftX=" + mLeftX +
                ", mRightX=" + mRightX +
                ", mTopY=" + mTopY +
                ", mBottomY=" + mBottomY +
                '}';
    }
}
