package com.finalproject.it.sanjeevni.activities.bloodBank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.BaseActivity;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.finalproject.it.sanjeevni.fragment.ProfileView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CurrentUserRequests extends BaseActivity {

    private static final int REQUEST_CALL=1;
    private ListView listView;
    private List<HashMap<String,Object>> display_list=new ArrayList<>();
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userID;
    private Boolean flag=false;
    private TextView defaultMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user_requests);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sanjeevni");
        getSupportActionBar().setSubtitle("Please Keep Your List Updated..");


        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        userID=fAuth.getCurrentUser().getUid();


        defaultMsg=findViewById(R.id.default_msg);
        listView=findViewById(R.id.list_view);

        if(fAuth.getCurrentUser()!=null) {
            fStore.collection("DonationRequestList").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    display_list.clear();
                    if(!(queryDocumentSnapshots.isEmpty())) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            if (!(snapshot.getId().equals("01_RequestIndex"))
                                    && !(snapshot.getId().equals("02_RequestNoPerUser"))) {
                                if (snapshot.getString("userID").equals(userID)) {
                                    HashMap<String, Object> temp = new HashMap<>();
                                    temp.put("Bgroup", snapshot.getString("BloodGroup"));
                                    temp.put("City", snapshot.getString("City"));
                                    temp.put("Contact", snapshot.getString("Contact"));
                                    temp.put("Time", snapshot.getString("TimeStamp"));
                                    temp.put("answered", snapshot.getString("Answered"));
                                    temp.put("name", snapshot.getString("Name"));
                                    temp.put("docID",snapshot.getId());
                                    display_list.add(temp);
                                }
                            }
                        }
                    }
                    if(display_list.size()==0)
                    {
                        defaultMsg.setVisibility(View.VISIBLE);
                    }
                    else {
                        defaultMsg.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        CurrentUserRequestsAdapter adapter = new CurrentUserRequestsAdapter(CurrentUserRequests.this, display_list);
                        Collections.sort(display_list, new MyCustomComparator());
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
