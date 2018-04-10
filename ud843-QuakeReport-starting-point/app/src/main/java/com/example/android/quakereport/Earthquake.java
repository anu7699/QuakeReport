package com.example.android.quakereport;

/**
 * Created by WELLCOME on 25-07-2017.
 */

public class Earthquake {
    private double magnitude;
    private String location;
    private long timems;
    private String url;

    public Earthquake(double magnitude,String location,long timems,String url)
    {
        this.magnitude=magnitude;
        this.location = location;
        this.timems=timems;
        this.url=url;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getUrl() {
        return url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTimems() {
        return timems;
    }

    public void setTimems(long timems) {
        this.timems=timems;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }
}
