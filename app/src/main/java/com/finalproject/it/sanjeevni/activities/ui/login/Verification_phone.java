package com.finalproject.it.sanjeevni.activities.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.finalproject.it.sanjeevni.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.onesignal.OneSignal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Verification_phone extends AppCompatActivity {

    private String verificationCodebysystem;
    private EditText otp;
    private Button verify_btn;
    private ProgressBar progress;
    private String phoneNo,firstName,emailID,city,gender,dateOfBirth,userID,state,no="NO",password;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser userThis = mAuth.getCurrentUser();
        userThis.delete();
        Toast.makeText(getApplicationContext(),"Registration Unsuccessful",Toast.LENGTH_LONG).show();
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_phone);

        otp=findViewById(R.id.verify_otp);
        verify_btn=findViewById(R.id.verify_btn);
        progress=findViewById(R.id.loading);

        mAuth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phoneNo = getIntent().getStringExtra("phoneNo");
        firstName=getIntent().getStringExtra("firstName");
        state=getIntent().getStringExtra("state");
        city=getIntent().getStringExtra("city");
        emailID=getIntent().getStringExtra("emailID");
        dateOfBirth=getIntent().getStringExtra("dateOfBirth");
        gender=getIntent().getStringExtra("gender");
        password=getIntent().getStringExtra("password");

        sendVerificationCodeToUser(phoneNo);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code=otp.getText().toString();
                if(code.isEmpty() || code.length()<6)
                {
                    otp.setError("Wrong OTP...");
                    otp.requestFocus();
                }
            }
        });

    }
    private void sendVerificationCodeToUser(String phoneNo) {
        progress.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNo, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallbacks );
    }

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodebysystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


            String code = phoneAuthCredential.getSmsCode();
            if(code !=null )
            {
                Toast toast = Toast.makeText(getBaseContext(),"Phone Verification Successful !!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                mAuth.createUserWithEmailAndPassword(emailID,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser fuser=mAuth.getCurrentUser();
                           fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast toast = Toast.makeText(getBaseContext(),"Email Sent For Verification, Please Check !",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast toast = Toast.makeText(getBaseContext(),"Error in creation: "+ e.getMessage(),Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();

                                }
                            });
                            userID=mAuth.getCurrentUser().getUid();
                            DocumentReference docref= fstore.collection("userDetails").document(userID);
                            Map<String,Object> user =new HashMap<>();
                            user.put("firstName",firstName );
                            user.put("emailID",emailID );
                            user.put("phoneNo",phoneNo );
                            user.put("state",state);
                            user.put("city",city );
                            user.put("gender",gender);
                            user.put("dateOfBirth", dateOfBirth);
                            user.put("donor",no);
                            docref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DocumentReference docref= fstore.collection("DonorList").document(userID);
                                    docref.update("requests",0);
                                    Toast toast = Toast.makeText(getApplicationContext(),"Registration Successful !!",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,5);
                                    toast.show();
                                    mAuth.signOut();
                                    progress.setVisibility(View.INVISIBLE);
                                    OneSignal.sendTag("User_Type","regular");
                                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast toast=Toast.makeText(getApplicationContext(),"Error in registration: "+e.getMessage(),Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,5);
                                    toast.show();
                                    final FirebaseUser userThis = mAuth.getCurrentUser();
                                    userThis.delete();
                                    progress.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(getBaseContext(),RegisterActivity.class));
                                    finish();
                                }
                            });
                        }
                        else
                        {
                            Toast toast = Toast.makeText(getBaseContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            startActivity(new Intent(getBaseContext(),RegisterActivity.class));
                            finish();
                        }
                    }
                });

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getBaseContext(),"Try Again : "+e.getMessage(),Toast.LENGTH_LONG).show();
            final FirebaseUser userThis = mAuth.getCurrentUser();
            userThis.delete();
            startActivity(new Intent(getBaseContext(),RegisterActivity.class));
            finish();
        }
    };


}
