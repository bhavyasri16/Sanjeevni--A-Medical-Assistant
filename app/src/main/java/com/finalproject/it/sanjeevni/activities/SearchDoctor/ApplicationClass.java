package com.finalproject.it.sanjeevni.activities.SearchDoctor;

import android.app.Application;

import java.util.ArrayList;

public class ApplicationClass extends Application {
    public static ArrayList<Doctor> doctors;

    @Override
    public void onCreate() {
        super.onCreate();

        doctors = new ArrayList<Doctor>();
        doctors.add(new Doctor("Dr.Shashank", "Jeevan", "ABC colony", "Mumbai", "Private Clinic"));
        doctors.add(new Doctor("Dr.Juhi", "Vedanta", "XYZ colony", "Kolkata", "Public Hospital"));
    }
}
