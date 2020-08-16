package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.BaseActivity;
import com.finalproject.it.sanjeevni.activities.Validations;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.onesignal.OneSignal;

import java.util.HashMap;
import java.util.Map;

public class bbRegister extends BaseActivity {
    TextInputLayout inputID, inputBGroup;
    EditText etDisease;
    Button btnReg;
    String BGroup;
    RadioGroup rbDisease;
    RadioButton rbYes, rbNo;
    CheckBox cbDeclaration;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bb_register);

        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        index=0;
        final DocumentReference docref=fstore.collection("User_Type").document("bloodDonors");
        docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Map<String,Object> thisData= value.getData();
                if(thisData.containsKey("donorCount")) {
                    index= Integer.parseInt(thisData.get("donorCount").toString());
                    Log.d("TAG", "INDEX VALUE "+index);
                    return;
                }
                else
                    index=0;
            }
        });

        inputBGroup = findViewById(R.id.inputBGroup);
        inputID = findViewById(R.id.inputID);
        etDisease = findViewById(R.id.etDisease);
        btnReg = findViewById(R.id.btnReg);
        rbDisease = findViewById(R.id.rbDisease);
        rbYes = findViewById(R.id.rbYes);
        rbNo = findViewById(R.id.rbNo);
        cbDeclaration = findViewById(R.id.cbDeclaration);
        final Validations vd=new Validations();

        //adding back button in Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Blood Connect");
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
                if (!vd.validateID(inputID) || vd.validateBGroup(inputBGroup)) {
                    Toast.makeText(bbRegister.this, "Please fill all the fields as required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton sel= findViewById(rbDisease.getCheckedRadioButtonId());
                final String userID=mAuth.getCurrentUser().getUid();
                DocumentReference docref= fstore.collection("userDetails").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("donor","YES");
                user.put("adhar",inputID.getEditText().getText().toString().trim());
                user.put("health_history", sel.getText().toString().trim());
                user.put("disease_description",etDisease.getEditableText().toString().trim());
                user.put("blood_group",inputBGroup.getEditText().getText().toString().trim());
                docref.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d("TAG", "INDEX VALUE RECIEVED "+index);
                        DocumentReference docref2=fstore.collection("User_Type").document("bloodDonors");
                        Map<String,Object> tempMap= new ArrayMap<>();
                        tempMap.put("donor_"+index,userID);
                        docref2.update(tempMap).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("bbRegister","Error : "+e.getMessage());
                                return;
                            }
                        });

                        Map<String, Object> indexadd = new ArrayMap<>();
                        indexadd.put("donorCount", index + 1);
                        docref2.update(indexadd).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG","updateRequestIndexError: : "+e.getMessage());
                            }
                        });

                        OneSignal.sendTag("User_Type","donor");
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


}
