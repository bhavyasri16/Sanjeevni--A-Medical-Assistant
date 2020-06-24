package com.finalproject.it.sanjeevni.activities.SearchDoctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.finalproject.it.sanjeevni.R;

public class DoctorDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        String hName = getIntent().getStringExtra("key1");
        String status = getIntent().getStringExtra("key2");
        String location = getIntent().getStringExtra("key3");
        String city = getIntent().getStringExtra("key4");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(hName);
        getSupportActionBar().setSubtitle(status);

    }
}
