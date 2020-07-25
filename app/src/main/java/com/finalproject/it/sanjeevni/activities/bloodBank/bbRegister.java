package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.finalproject.it.sanjeevni.activities.ui.login.LoginActivity;
import com.finalproject.it.sanjeevni.fragment.ProfileView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class bbRegister extends AppCompatActivity {
    TextInputLayout inputID, inputBGroup;
    EditText etDisease;
    Button btnReg;
    String BGroup;
    RadioGroup rbDisease;
    RadioButton rbYes, rbNo;
    CheckBox cbDeclaration;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bb_register);

        inputBGroup = (TextInputLayout)findViewById(R.id.inputBGroup);
        inputID = (TextInputLayout)findViewById(R.id.inputID);
        etDisease = (EditText)findViewById(R.id.etDisease);
        btnReg = (Button)findViewById(R.id.btnReg);
        rbDisease = (RadioGroup)findViewById(R.id.rbDisease);
        rbYes = (RadioButton)findViewById(R.id.rbYes);
        rbNo = (RadioButton)findViewById(R.id.rbNo);
        cbDeclaration = (CheckBox)findViewById(R.id.cbDeclaration);

        //adding back button in Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Blood Bank");
        getSupportActionBar().setSubtitle("Register yourself...");

        btnReg.setEnabled(false);
        btnReg.setTextColor(getResources().getColor(android.R.color.white));
        btnReg.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        etDisease.setVisibility(View.INVISIBLE);
        inputBGroup.getEditText().requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputBGroup.getEditText(), InputMethodManager.SHOW_IMPLICIT);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateID() || !validateBGroup()) {
                    Toast.makeText(bbRegister.this, "Please fill all the fields as required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton sel= findViewById(rbDisease.getCheckedRadioButtonId());
                mAuth=FirebaseAuth.getInstance();
                fstore=FirebaseFirestore.getInstance();
                final String userID=mAuth.getCurrentUser().getUid();
                DocumentReference docref= fstore.collection("userDetails").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("donor","YES");
                user.put("adhar",inputID.getEditText().getText().toString().trim());
                user.put("health_history", sel.getText().toString().trim());
                user.put("disease_description",etDisease.getEditableText().toString().trim());
                user.put("blood_group",BGroup);
                docref.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Registration Successful !!",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,5);
                        toast.show();
                        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast=Toast.makeText(getApplicationContext(),"Error in registration: "+e.getMessage(),Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,5);
                        toast.show();
                    }
                });
            }
        });
    }

    private boolean validateID(){
        String IDProof = inputID.getEditText().getText().toString().trim();
        if(IDProof.length() < 12){
            inputID.setError("*A 12-digit number required!");
            return false;
        }
        else {
            inputID.setError("");
            return true;
        }
    }

    private boolean validateBGroup(){
        BGroup = inputBGroup.getEditText().getText().toString().trim();
        BGroup=BGroup.toUpperCase();
        BGroup = BGroup.replaceAll("\\s", "");
        String[] groups= {"A+","A-","B+","B-","O+","O-","AB-","AB+"};
        if(BGroup.length() == 0)
            return false;
        if(!Arrays.asList(groups).contains(BGroup))
        {
            inputBGroup.setError("Valid Blood Groups : A+, A-, B+, B-, O+, O-, AB-, AB+");
            return false;
        }
        return true;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbNo:
                if (checked)
                    etDisease.setVisibility(View.INVISIBLE);
                    break;
            case R.id.rbYes:
                if (checked) {
                    etDisease.setVisibility(View.VISIBLE);
                    etDisease.requestFocus();
                }
                    break;
        }
    }


    public void onCheckBoxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.cbDeclaration:
                if (checked) {
                    btnReg.setEnabled(true);
                    btnReg.setTextColor(getResources().getColor(android.R.color.white));
                    btnReg.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    btnReg.setEnabled(false);
                    btnReg.setTextColor(getResources().getColor(android.R.color.white));
                    btnReg.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }
                break;
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
