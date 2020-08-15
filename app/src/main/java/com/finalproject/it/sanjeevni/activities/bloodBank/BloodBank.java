package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.BaseActivity;
import com.finalproject.it.sanjeevni.fragment.ProfileView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class BloodBank extends BaseActivity {
    private  Button btnRegAs, btnDonate, btnlist;
    private FloatingActionButton mainbtn,allreq,userreq;
    private TextView allreqText,userreqText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private Boolean isOpen;
    private Animation fab_open,fab_close,fab_rotate;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        btnRegAs = findViewById(R.id.btnRegAs);
        btnDonate = findViewById(R.id.btnDonate);
        btnlist=findViewById(R.id.btnList);
        mainbtn=findViewById(R.id.main_add_fab);
        allreq=findViewById(R.id.fab1);
        userreq=findViewById(R.id.fab2);
        allreqText=findViewById(R.id.all_req);
        userreqText=findViewById(R.id.user_req);

        //adding back button in Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sanjeevni");
        getSupportActionBar().setSubtitle("Blood Connect");

        /*fstore.collection("User_Type").document("bloodDonors").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
        });*/

        isOpen=false;

        fab_open= AnimationUtils.loadAnimation(BloodBank.this,R.anim.fab_open);
        fab_close= AnimationUtils.loadAnimation(BloodBank.this,R.anim.fab_close);
        fab_rotate=AnimationUtils.loadAnimation(BloodBank.this,R.anim.fab_rotate);

        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isOpen){

                    mainbtn.startAnimation(fab_rotate);
                    allreq.startAnimation(fab_close);
                    userreq.startAnimation(fab_close);
                    allreqText.startAnimation(fab_close);
                    userreqText.startAnimation(fab_close);

                    isOpen=false;
                }else{

                    mainbtn.startAnimation(fab_rotate);
                    allreq.startAnimation(fab_open);
                    userreq.startAnimation(fab_open);
                    allreqText.startAnimation(fab_open);
                    userreqText.startAnimation(fab_open);

                    isOpen=true;
                }
            }
        });

        allreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BloodBank.this,BloodDonationRequests.class));
                finish();
            }
        });

        userreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BloodBank.this,CurrentUserRequests.class));
                finish();
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

        btnlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BloodDonationRequests.class));
                finish();
            }
        });
    }

    /*
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

     */
}
