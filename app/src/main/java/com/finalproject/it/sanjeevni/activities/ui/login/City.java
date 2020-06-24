package com.finalproject.it.sanjeevni.activities.ui.login;

public class City implements Comparable<City> {

    private int cityID;
    private State state;
    private String cityName;

    public City(int cityID, State state, String cityName) {
        this.cityID = cityID;
        this.state = state;
        this.cityName = cityName;
    }

    public int getCityID() {
        return cityID;
    }

    public State getState() {
        return state;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return cityName;
    }

    @Override
    public int compareTo(City another) {
        return this.cityID - another.getCityID();//ascending order
//            return another.getCityID() - this.cityID;//descending order
    }
}
