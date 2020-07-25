package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.fragment.ProfileView;
import com.google.firebase.auth.FirebaseAuth;

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.mymenu, menu);
            return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            int id = item.getItemId();
            if (id == R.id.logout_btn) {
                FirebaseAuth.getInstance().signOut();
                recreate();
            }
            else if(id==R.id.refresh){
                recreate();
            }
            else if(id==R.id.profile_btm){
                startActivity(new Intent(getBaseContext(), ProfileView.class));
            }
            return super.onOptionsItemSelected(item);
    }
}
