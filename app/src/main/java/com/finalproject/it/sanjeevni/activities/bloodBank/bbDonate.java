package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.finalproject.it.sanjeevni.fragment.ProfileView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class bbDonate extends AppCompatActivity {
    TextInputLayout inputName, inputContact, inputIDProof, searchBGroup, searchCity;
    Button btnSearch;
    String name, contact, IDProof, BGroup, city, message, userID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bb_donate);

        inputName = findViewById(R.id.inputName);
        inputContact = findViewById(R.id.inputContact);
        inputIDProof = findViewById(R.id.inputIDProof);
        searchBGroup = findViewById(R.id.searchBgroup);
        searchCity = findViewById(R.id.searchCity);
        btnSearch = findViewById(R.id.btnSearch);

        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        userID= mAuth.getCurrentUser().getUid();

       if (Build.VERSION.SDK_INT >= 26) {
            if (checkPermission()) {
                Log.e("permission", "Permission already granted.");
            } else {
                requestPermission();
            }
        }
        //adding back button in Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Blood Bank");
        getSupportActionBar().setSubtitle("Search donor...");

        inputName.getEditText().requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputName.getEditText(), InputMethodManager.SHOW_IMPLICIT);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateID();
                validateContact();
                if(!validateName() | !validateContact() | !validateBGroup() | !validateCity() | !validateID()){
                    Toast.makeText(bbDonate.this, "Please fill all the fields as required!", Toast.LENGTH_SHORT).show();
                    return ;
                }
               if(checkPermission()) {
                    SmsManager smsManager = SmsManager.getDefault();
                   DocumentReference docref= fstore.collection("DonorList").document(userID);
                    getContacts(BGroup,city);
                    String this_name="xyz";
                    message= "Hello " + this_name + " , If you are willing to donate " + BGroup + " in the city " + city
                        + ", Please Contact " + name + "( "+ contact + " ) URGENTLY. Regards Sanjeevni Team.";
                    smsManager.sendTextMessage(contact, null, message, null, null);
                   AlertDialog.Builder msgSent = new AlertDialog.Builder(view.getContext());
                   msgSent.setTitle("Done !!");
                   msgSent.setMessage("Message Sent to All the Potential Blood Donors. Take Care.");
                   msgSent.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
                       }
                   });

                }else {
                    Toast.makeText(bbDonate.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getContacts(String bGroup, String city) {
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(bbDonate.this, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(bbDonate.this,
                            "Permission accepted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(bbDonate.this,
                            "Permission denied", Toast.LENGTH_LONG).show();
                    Button sendSMS = (Button) findViewById(R.id.btnSearch);
                    sendSMS.setEnabled(false);

                }
                break;
        }
    }

    private boolean validateName(){
        name = inputName.getEditText().getText().toString().trim();
        if(name.length() == 0)
            return false;
        return true;
    }

    private boolean validateContact(){
        contact = inputContact.getEditText().getText().toString().trim();
        if(contact.length()<10) {
            inputContact.setError("*A 10-digit number required!");
            return false;
        }
        else{
            inputContact.setError("");
            return true;
        }
    }

    private boolean validateBGroup(){
        BGroup = searchBGroup.getEditText().getText().toString().trim();
        BGroup=BGroup.toUpperCase();
        BGroup = BGroup.replaceAll("\\s", "");
        String[] groups= {"A+","A-","B+","B-","O+","O-","AB-","AB+"};
        if(BGroup.length() == 0)
            return false;
        if(!Arrays.asList(groups).contains(BGroup))
        {
            searchBGroup.setError("Valid Blood Groups : A+, A-, B+, B-, O+, O-, AB-, AB+");
            return false;
        }
        searchBGroup.setError(null);
        return true;
    }

    private boolean validateCity(){
        city = searchCity.getEditText().getText().toString().trim();
        if(city.length() == 0)
            return false;
        return true;
    }

    private boolean validateID(){
        IDProof = inputIDProof.getEditText().getText().toString().trim();
        if(IDProof.length() < 12){
            inputIDProof.setError("*A 12-digit number required!");
            return false;
        }
        else {
            inputIDProof.setError("");
            return true;
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
