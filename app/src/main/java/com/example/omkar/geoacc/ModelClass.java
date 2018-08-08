package com.example.omkar.geoacc;

/**
 * Created by omkar on 7/18/2018.
 */

public class ModelClass {
    float lat,lon,x,y,z;
    String timestamp;

    public ModelClass(){

    }

    public ModelClass(float lat, float lon, float x, float y, float z, String timestamp) {
        this.lat = lat;
        this.lon = lon;
        this.x = x;
        this.y = y;
        this.z = z;
        this.timestamp = timestamp;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
