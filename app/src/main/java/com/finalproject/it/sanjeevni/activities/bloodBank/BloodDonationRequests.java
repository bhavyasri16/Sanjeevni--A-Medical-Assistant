package com.finalproject.it.sanjeevni.activities.bloodBank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.finalproject.it.sanjeevni.fragment.ProfileView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BloodDonationRequests extends AppCompatActivity {

    private static final int REQUEST_CALL=1;
    private ListView listView;
    private List<HashMap<String,String>> display_list=new ArrayList<>();
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String number;
    private Boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donation_requests);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sanjeevni");
        getSupportActionBar().setSubtitle("An Opportunity to Help...");


        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        listView=findViewById(R.id.list_view);

        fStore.collection("User_Type").document("bloodDonors").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> temp=document.getData();
                    if (document.exists()) {
                        FirebaseAuth mAuth;
                        if(temp.containsValue(fAuth.getCurrentUser().getUid())) {
                            flag=true;
                        }
                    }
                } else {
                    Log.d("TAG", "Error getting documents: "+ task.getException());
                }
            }
        });

        if(fAuth.getCurrentUser()!=null) {
            fStore.collection("DonationRequestList").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    display_list.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if(!(snapshot.getId().equals("01_RequestIndex"))
                                && !(snapshot.getId().equals("02_RequestNoPerUser")) ){
                            if(snapshot.getString("Answered").equalsIgnoreCase("NO")) {
                                HashMap<String, String> temp = new HashMap<>();
                                temp.put("Bgroup", snapshot.getString("BloodGroup"));
                                temp.put("City", snapshot.getString("City"));
                                temp.put("Contact", snapshot.getString("Contact"));
                                temp.put("Time", snapshot.getString("TimeStamp"));
                                temp.put("donor",flag+"");
                                number=snapshot.getString("Contact");
                                display_list.add(temp);
                            }
                        }
                    }
                    if(display_list.size()==0)
                    {
                        TextView defaultMsg=findViewById(R.id.default_msg);
                        defaultMsg.setVisibility(View.VISIBLE);
                    }
                    else{
                    listView.setVisibility(View.VISIBLE);
                    BloodDonateRequestAdapter adapter = new BloodDonateRequestAdapter(BloodDonationRequests.this, display_list);
                    Collections.sort(display_list,new MyCustomComparator());
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    }
                }
            });
        }


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
            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
            finish();
        }
        else if(id==R.id.refresh){
            recreate();
        }
        else if(id==R.id.profile_btm){
            startActivity(new Intent(getApplicationContext(), ProfileView.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                BloodDonateRequestAdapter.makePhoneCall(number);
            }else{
                Toast.makeText(BloodDonationRequests.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
