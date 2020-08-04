package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.fragment.ProfileView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Map;

public class BloodBank extends AppCompatActivity {
    Button btnRegAs, btnDonate;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        btnRegAs = (Button)findViewById(R.id.btnRegAs);
        btnDonate = (Button)findViewById(R.id.btnDonate);

        //adding back button in Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sanjeevni");
        getSupportActionBar().setSubtitle("Blood Bank");

        fstore.collection("User_Type").document("bloodDonors").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> temp=document.getData();
                    if (document.exists()) {
                        if(temp.containsValue(mAuth.getCurrentUser().getUid())) {
                            flag=1;
                        }
                    }
                } else {
                    Log.d("TAG", "Error getting documents: "+ task.getException());
                }
            }
        });

        btnRegAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==1)
                {
                    AlertDialog.Builder dialog= new AlertDialog.Builder(view.getContext());
                    dialog.setTitle("Congratulations !!").setMessage("You Are already Registered as a Blood Donor With Us.");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).create().show();
                    return;
                }
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
