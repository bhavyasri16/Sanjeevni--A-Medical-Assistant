package com.finalproject.it.sanjeevni.fragment;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.BaseActivity;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.finalproject.it.sanjeevni.activities.bloodBank.BloodDonateRequestAdapter;
import com.finalproject.it.sanjeevni.activities.bloodBank.BloodDonationRequests;
import com.finalproject.it.sanjeevni.activities.bloodBank.CurrentUserRequests;
import com.finalproject.it.sanjeevni.activities.bloodBank.MyCustomComparator;
import com.finalproject.it.sanjeevni.activities.bloodBank.ReviewDialogBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReviewActivity extends BaseActivity {

    private ListView listView;
    private TextView defaultMsg;
    private List<HashMap<String,Object>> display_list=new ArrayList<>();
    private FirebaseAuth fauth;
    private FirebaseFirestore fStore;
    private int reviewIndex=0;
    private ReviewAdapter adapter;
    private FloatingActionButton mainbtn;
    private Boolean isOpen;
    private Animation fab_rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Sanjeevni");
        getSupportActionBar().setSubtitle("App Reviews");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fauth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        listView=findViewById(R.id.list_view);
        defaultMsg=findViewById(R.id.defaulmsg);
        mainbtn=findViewById(R.id.add_fab);
        final DocumentReference docref=fStore.collection("userResponse").document("01_ReviewIndex");
        docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                // thisData= value.getData();
                if(value.exists()) {

                    if (value.contains("NoOfReviews")) {
                        reviewIndex = Integer.parseInt(value.get("NoOfReviews").toString());
                    }
                }
            }
        });

        fStore.collection("userResponse").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                display_list.clear();
                assert queryDocumentSnapshots != null;
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    if(!(snapshot.getId().equals("01_ReviewIndex"))){
                            HashMap<String, Object> temp = new HashMap<>();
                            temp.put("apphelped", snapshot.getString("AppHelped"));
                            temp.put("fullreview", snapshot.getString("fullReview"));
                            temp.put("name", snapshot.getString("name"));
                            temp.put("Time", snapshot.getString("timestamp"));
                            display_list.add(temp);
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
                    adapter = new ReviewAdapter(ReviewActivity.this, display_list);
                    Collections.sort(display_list,new MyCustomComparator());
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }
            }
        });

        isOpen = false;

        fab_rotate = AnimationUtils.loadAnimation(ReviewActivity.this, R.anim.fab_rotate);

        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {

                    mainbtn.startAnimation(fab_rotate);

                    isOpen = false;
                } else {

                    mainbtn.startAnimation(fab_rotate);

                    isOpen = true;
                }
            }
        });

        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final EditText namefield=new EditText(view.getContext());
                namefield.setHint("Enter Your Name");
                AlertDialog.Builder dialog=new AlertDialog.Builder(view.getContext());
                dialog.setTitle("Enter Name First");
                dialog.setView(namefield);
                dialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String name = namefield.getText().toString().trim();
                        if (name.length()==0)
                            Toast.makeText(view.getContext(),"Name Field Cannot Be Empty!",Toast.LENGTH_SHORT).show();
                        else
                        {
                            final ReviewDialogBuilder requestDialog = new ReviewDialogBuilder(ReviewActivity.this);
                            requestDialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String yes_no=requestDialog.getSelectedOption();
                                    String review=requestDialog.getReviewText();
                                    reviewEntry(yes_no,review,name);
                                    //  changeView(tempView);
                                    //  updateIndex(userID);
                                }
                            });
                            requestDialog.create().show();
                        }
                    }
                }).create().show();
            }
        });

    }
    private void reviewEntry(String yes_no, String review,String name) {

        HashMap<String,Object> reviewDetails= new HashMap<>() ;
        reviewDetails.put("name",name);
        reviewDetails.put("userID",fauth.getCurrentUser().getUid());
        reviewDetails.put("timestamp",java.text.DateFormat.getDateTimeInstance().format(new Date()));
        reviewDetails.put("AppHelped",yes_no);
        reviewDetails.put("fullReview",review);
        reviewDetails.put("email",fauth.getCurrentUser().getEmail());

        fStore.collection("userResponse").document("Review_"+reviewIndex)
                .set(reviewDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap<Object,Object> entry=new HashMap<>();
                        entry.put("NoOfReviews",reviewIndex+1);
                        fStore.collection("userResponse").document("01_ReviewIndex").set(entry);
                        Toast.makeText(getApplicationContext(),"Successful !!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","Error in review DB:"+e.getMessage());
            }
        });
    }
}
