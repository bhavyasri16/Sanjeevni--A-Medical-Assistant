package com.finalproject.it.sanjeevni.fragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class ProfileView extends AppCompatActivity {

    private EditText firstname, lastname, emailid, phone, dob,statetext,citytext;
    private TextView gender;
    private Spinner state_spinner,city_spinner;
    private Button edit_btn;
    private ImageView dp;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private  String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        firstname = findViewById(R.id.first_name);
        lastname =  findViewById(R.id.last_name);
        emailid =  findViewById(R.id.email_id);
        phone =  findViewById(R.id.phone_no);
        gender =findViewById(R.id.gender);
        statetext=findViewById(R.id.state_text);
        citytext=findViewById(R.id.citytext);
        edit_btn= findViewById(R.id.edit);
        city_spinner =  findViewById(R.id.city);
        state_spinner =  findViewById(R.id.state);
        dob=  findViewById(R.id.dob);
        dp=findViewById(R.id.dp);

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        userID=mAuth.getCurrentUser().getUid();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DocumentReference docrref= fStore.collection("userDetails").document(userID);
        docrref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                firstname.setText(documentSnapshot.getString("firstName"));
                lastname.setText(documentSnapshot.getString("lastName"));
                emailid.setText(documentSnapshot.getString("emailID"));
                phone.setText(documentSnapshot.getString("phoneNo"));
                gender.setText(documentSnapshot.getString("gender"));
                statetext.setText(documentSnapshot.getString("state"));
                citytext.setText(documentSnapshot.getString("city"));
                dob.setText(documentSnapshot.getString("dateOfBirth"));
                if(gender.getText()=="male")
                {
                    dp.setImageResource(R.drawable.dp_male);
                }
                else
                    dp.setImageResource(R.drawable.dp_female);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mAuth.getCurrentUser()!=null){
            getMenuInflater().inflate(R.menu.mymenu, menu);
            return super.onCreateOptionsMenu(menu);}
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mAuth.getCurrentUser()!=null){
            int id = item.getItemId();
            if (id == R.id.logout_btn) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
            }
            else if(id==R.id.refresh){
                recreate();
            }
            else if(id==R.id.profile_btm){
                startActivity(new Intent(getBaseContext(), ProfileView.class));
            }
            return super.onOptionsItemSelected(item);}
        return false;
    }
}
