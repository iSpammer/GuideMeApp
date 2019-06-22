package com.guideme.guideme.ENTITY;


//import com.google.gson.annotations.SerializedName;

public class Coord {

//    @SerializedName("lon")
    private String lon;

//    @SerializedName("lat")
    private String lat;

    public Coord(String lon, String lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }
}
