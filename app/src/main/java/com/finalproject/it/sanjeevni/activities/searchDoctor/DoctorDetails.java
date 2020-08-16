package com.finalproject.it.sanjeevni.activities.searchDoctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.BaseActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorDetails extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        final ImageView logo = (ImageView)findViewById(R.id.logo);
        final ProgressBar logo_load = (ProgressBar)findViewById(R.id.logo_load);
        final TextView pri_dr_name = (TextView)findViewById(R.id.pri_dr_name);
        final TextView pri_dr_spec = (TextView)findViewById(R.id.pri_dr_spec);
        final ListView other_dr_list = (ListView)findViewById(R.id.other_dr_list);
        final TextView addr = (TextView)findViewById(R.id.addr);
        final TextView phone = (TextView)findViewById(R.id.phone);
        final TextView mail = (TextView)findViewById(R.id.mail);
        final TextView appoint_detail = (TextView)findViewById(R.id.appoint_detail);
        final Button btn_map= (Button)findViewById(R.id.btn_map);
        final Button btn_call= (Button)findViewById(R.id.btn_call);
        final Button btn_mail= (Button)findViewById(R.id.btn_mail);
        final Button onSpot= (Button)findViewById(R.id.onSpot);
        final Button onCall= (Button)findViewById(R.id.onCall);
        final Button onLine= (Button)findViewById(R.id.onLine);

        String key = getIntent().getStringExtra("key");

        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        StorageReference storeRef = FirebaseStorage.getInstance().getReference();
        final StorageReference fileRef = storeRef.child("doctors/"+key+"/logo_image.jpg");
        DocumentReference docRef = fstore.collection("Doctor_Details").document(key);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("name");
                    final String email = documentSnapshot.getString("emailID");
                    String address = documentSnapshot.getString("fullAddress");
                    String city = documentSnapshot.getString("city");
                    String state = documentSnapshot.getString("state");
                    final String full_addr = address + "," + city + "," + state;
                    final String phoneNo = documentSnapshot.getString("phoneNo");
                    String category = documentSnapshot.getString("category");
                    String primary_dr_name = documentSnapshot.getString("drNamePrimary");
                    String primary_spec = documentSnapshot.getString("drSpecPrimary");   //check for other doctors
                    String onspot = documentSnapshot.getString("onspotAppointment");
                    String oncall = documentSnapshot.getString("oncallAppointment");
                    String online = documentSnapshot.getString("onlineAppointment");
                    String rating = documentSnapshot.getString("rating");
                    Integer no_of_other_dr = Math.toIntExact(documentSnapshot.getLong("no_of_otherdoctors"));
                    ArrayList<String> dr_list = new ArrayList<>();
                    if(no_of_other_dr == 0){
                        dr_list.add("No other doctor available!");
                    } else {
                        for(int i=0; i<no_of_other_dr; i++){
                            String dName = documentSnapshot.getString("drName"+i);
                            String dSpec = documentSnapshot.getString("drSpec"+i);
                            String dInfo = dName + "\n(" + dSpec + ")";
                            dr_list.add(dInfo);
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, dr_list){
                        @SuppressLint("ResourceAsColor")
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView tv = (TextView)view.findViewById(android.R.id.text1);
                            tv.setTextColor(Color.parseColor("#880e4f"));
                            return view;
                        }
                    };
                    other_dr_list.setAdapter(adapter);
                    if(no_of_other_dr < 2){
                        ConstraintLayout parent = findViewById(R.id.parent_layout);
                        ConstraintSet set = new ConstraintSet();
                        set.clone(parent);
                        set.connect(R.id.addr_layout, ConstraintSet.TOP, R.id.logo, ConstraintSet.BOTTOM);
                        set.applyTo(parent);
                    } else {
                        ConstraintLayout parent = findViewById(R.id.parent_layout);
                        ConstraintSet set = new ConstraintSet();
                        set.clone(parent);
                        set.connect(R.id.addr_layout, ConstraintSet.TOP, R.id.other_dr_list, ConstraintSet.BOTTOM);
                        set.applyTo(parent);
                    }

                    //setting title
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(name);
                    getSupportActionBar().setSubtitle(category);

                    //setting logo in image_view
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).resize(100, 100).into(logo, new Callback() {
                                @Override
                                public void onSuccess() {
                                    logo_load.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Toast.makeText(DoctorDetails.this, "Error in loading image!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    //setting string values
                    pri_dr_name.setText(primary_dr_name);
                    pri_dr_spec.setText("(" + primary_spec + ")");
                    addr.setText(full_addr);
                    phone.setText(phoneNo);
                    mail.setText(email);

                    //button functions
                    btn_map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(full_addr));
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });
                    btn_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri callUri = Uri.parse("tel:" + phoneNo);
                            Intent callIntent = new Intent(Intent.ACTION_DIAL, callUri);
                            startActivity(callIntent);
                        }
                    });
                    btn_mail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mailIntent = new Intent(Intent.ACTION_SEND);
                            mailIntent.setData(Uri.parse("mailto:"));
                            mailIntent.setType("text/plain");
                            mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                            try {
                                startActivity(Intent.createChooser(mailIntent, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(DoctorDetails.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //setting appointment section
                    if(onspot.equals("YES")){
                        onSpot.setEnabled(true);
                        onSpot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appoint_detail.setText("On-Spot appointent available here...");
                            }
                        });
                    } else {
                        onSpot.setEnabled(false);
                    }
                    if(oncall.equals("YES")){
                        onCall.setEnabled(true);
                        onCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appoint_detail.setText("To fix appointment, call on : " + documentSnapshot.getString("oncallContact"));
                            }
                        });
                    } else {
                        onCall.setEnabled(false);
                    }
                    if(online.equals("YES")){
                        onLine.setEnabled(true);
                        onLine.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appoint_detail.setText("To fix appointment, go to the website : " + documentSnapshot.getString("onlineLink"));
                            }
                        });
                    } else {
                        onLine.setEnabled(false);
                    }

                }
            }
        });

    }
}
