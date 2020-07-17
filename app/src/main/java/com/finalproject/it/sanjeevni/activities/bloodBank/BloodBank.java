package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.finalproject.it.sanjeevni.R;

public class BloodBank extends AppCompatActivity {
    Button btnRegAs, btnDonate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        btnRegAs = (Button)findViewById(R.id.btnRegAs);
        btnDonate = (Button)findViewById(R.id.btnDonate);

        //adding back button in Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sanjeevni");
        getSupportActionBar().setSubtitle("Blood Bank");

        btnRegAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.finalproject.it.sanjeevni.activities.bloodBank.BloodBank.this, com.finalproject.it.sanjeevni.activities.bloodBank.bbRegister.class);
                startActivity(intent);
            }
        });

        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.finalproject.it.sanjeevni.activities.bloodBank.BloodBank.this, com.finalproject.it.sanjeevni.activities.bloodBank.bbDonate.class);
                startActivity(intent);
            }
        });
    }
}
