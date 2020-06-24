package com.finalproject.it.sanjeevni.activities.SearchDoctor;

public class Doctor {
    private String dName;
    private String hName;
    private String location;
    private String city;
    private String hStatus;

    public Doctor(String dName, String hName, String location, String city, String hStatus) {
        this.dName = dName;
        this.hName = hName;
        this.location = location;
        this.city = city;
        this.hStatus = hStatus;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String gethStatus() {
        return hStatus;
    }

    public void sethStatus(String hStatus) {
        this.hStatus = hStatus;
    }
}
