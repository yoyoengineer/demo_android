package com.yosir.createmeeting.model;

import java.io.Serializable;

public class Location implements Serializable{
    private  String address;
    private  Double longitude;
    private  Double latitude;
    private  String town;

    public Location() {
    }

    public Location(String address, Double longitude, Double latitude, String town) {
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Override
    public String toString() {
        return "Location{" +
                "address='" + address + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", town='" + town + '\'' +
                '}';
    }
}
