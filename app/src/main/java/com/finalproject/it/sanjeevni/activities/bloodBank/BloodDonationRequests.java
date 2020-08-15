package com.finalproject.it.sanjeevni.activities.bloodBank;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.BaseActivity;
import com.finalproject.it.sanjeevni.activities.Validations;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.finalproject.it.sanjeevni.fragment.ProfileView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BloodDonationRequests extends BaseActivity {

    private static final int REQUEST_CALL=1;
    private ListView listView;
    private List<HashMap<String,Object>> display_list=new ArrayList<>();
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String number,filterCity="",filterBg="";
    private Boolean flag=false,isOpen;
    private FloatingActionButton filter,byCity,byBg,clearFilters;
    private Animation fab_open,fab_close,fab_rotate;
    private TextView byCityText,byBgText,clearFilterText,defaultMsg;
    private BloodDonateRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donation_requests);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sanjeevni");
        getSupportActionBar().setSubtitle("An Opportunity to Help...");

        filter=findViewById(R.id.filters);
        byCity=findViewById(R.id.byCity);
        byBg=findViewById(R.id.byBG);
        clearFilters=findViewById(R.id.clearFilters);
        clearFilterText=findViewById(R.id.clear_filterText);
        byBgText=findViewById(R.id.byBgText);
        byCityText=findViewById(R.id.byCityText);
        defaultMsg=findViewById(R.id.default_msg);
        final Validations vd=new Validations();


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
                    assert queryDocumentSnapshots != null;
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if(!(snapshot.getId().equals("01_RequestIndex"))
                                && !(snapshot.getId().equals("02_RequestNoPerUser")) ){
                            if(snapshot.getString("Answered").equalsIgnoreCase("NO")) {
                                HashMap<String, Object> temp = new HashMap<>();
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
                        defaultMsg.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                    }
                    else{
                        defaultMsg.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        adapter = new BloodDonateRequestAdapter(BloodDonationRequests.this, display_list);
                        Collections.sort(display_list,new MyCustomComparator());
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                }
            });
        }


        isOpen=false;

        fab_open= AnimationUtils.loadAnimation(BloodDonationRequests.this,R.anim.fab_open);
        fab_close= AnimationUtils.loadAnimation(BloodDonationRequests.this,R.anim.fab_close);
        fab_rotate=AnimationUtils.loadAnimation(BloodDonationRequests.this,R.anim.fab_rotate);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isOpen){

                    filter.startAnimation(fab_rotate);
                    byBg.startAnimation(fab_close);
                    byCity.startAnimation(fab_close);
                    clearFilters.startAnimation(fab_close);
                    clearFilterText.startAnimation(fab_close);
                    byBgText.startAnimation(fab_close);
                    byCityText.startAnimation(fab_close);
                    isOpen=false;
                }else{

                    filter.startAnimation(fab_rotate);
                    byBg.startAnimation(fab_open);
                    byCity.startAnimation(fab_open);
                    clearFilters.startAnimation(fab_open);
                    clearFilterText.startAnimation(fab_open);
                    byBgText.startAnimation(fab_open);
                    byCityText.startAnimation(fab_open);
                    isOpen=true;
                }
            }
        });

        byCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final SpinnerDialog dialog=new SpinnerDialog(BloodDonationRequests.this);
                dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        filterCity=dialog.getCitySelected();
                        if(dialog.getCityID()!=0) {
                            applyFilter(filterCity,filterBg);
                        }else {
                            Toast toast = Toast.makeText(view.getContext(), "Please Select the City to filter.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
        });

        byBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LinearLayout container = new LinearLayout(view.getContext());
                container.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(60, 60, 60, 20);
                final EditText bgInput=new EditText(view.getContext());
                bgInput.setHint("Blood Group");
                bgInput.setLayoutParams(lp);
                container.addView(bgInput);
                AlertDialog.Builder dialog=new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Enter Blood Group To Filter List");
                dialog.setView(container);
                dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!vd.validateBGroup(bgInput)) {
                            Toast toast = Toast.makeText(view.getContext(), "Error : Valid Blood Groups are A+, A-, B+, B-, O+, O-, AB-, AB+", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                        else{
                            filterBg=bgInput.getEditableText().toString().trim();
                            applyFilter(filterCity,filterBg);
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
        });

        clearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyFilter("","");
                defaultMsg.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
            }
        });



    }
    private void applyFilter(String filterCity, String filterBg) {
        adapter.clear();
        display_list.clear();
        display_list.addAll(adapter.filter(filterCity,filterBg));
        if(display_list.size()==0)
        {
            defaultMsg.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
        else {
            defaultMsg.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            Collections.sort(display_list, new MyCustomComparator());
            adapter.notifyDataSetChanged();
        }

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                BloodDonateRequestAdapter.makePhoneCall(number);
            else{
                Toast.makeText(BloodDonationRequests.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
