package com.finalproject.it.sanjeevni.activities.ui.login;

import android.app.DatePickerDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.graphics.drawable.AdaptiveIconDrawable;
import android.content.Intent;
import android.os.Bundle;
import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.data.model.UserDetails;
import com.finalproject.it.sanjeevni.fragment.Confirm_dr_dialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthRegistrar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity implements Confirm_dr_dialog.Confirm_dr_dialogListener {

    EditText firstname, lastname, emailid, phone, password, confirm_password, dob, city;
    RadioGroup gender;
    RadioButton sel_gen;
    Button registerButton,dr_register;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" + "(?=\\S+$)" + ".{5,}" + "$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^" + "(?=\\S+$)" + ".{10,}" + "$");
    private FirebaseAuth mAuth;
    private ProgressBar loadingProgressBar;
    private FirebaseFirestore fstore;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadingProgressBar = findViewById(R.id.loading);
        firstname = findViewById(R.id.first_name);
        lastname =  findViewById(R.id.last_name);
        emailid =  findViewById(R.id.email_id);
        phone =  findViewById(R.id.phone_no);
        password =  findViewById(R.id.create_password);
        confirm_password =  findViewById(R.id.confirm_password);
        gender =findViewById(R.id.gender_radio);

        registerButton = findViewById(R.id.register_button);
        dr_register= findViewById(R.id.register_dr_button);
        city =  findViewById(R.id.city);
        dob=  findViewById(R.id.dob);

        mAuth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener mydate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dob,myCalendar);
            }
        };
        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, mydate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        loadingProgressBar = findViewById(R.id.loading);


        this.registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validateFirstname() | !validateLastname() | !validateCity() | !validateEmail() | !validatePhone() | !validateDate() | !validatePassword() | !validateConfirmPassword())
                {
                    return;
                }

                loadingProgressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(emailid.getEditableText().toString().trim(),
                        password.getEditableText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                                    Toast toast = Toast.makeText(getBaseContext(),"Error : "+ e.getMessage(),Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            });

                            sel_gen= findViewById(gender.getCheckedRadioButtonId());
                            userID=mAuth.getCurrentUser().getUid();
                            DocumentReference docref= fstore.collection("userDetails").document(userID);
                            Map<String,Object> user =new HashMap<>();
                            user.put("firstName",firstname.getEditableText().toString().trim());
                            user.put("lastName",lastname.getEditableText().toString().trim());
                            user.put("emailID",emailid.getEditableText().toString().trim());
                            user.put("phoneNo",phone.getEditableText().toString().trim());
                            user.put("city",city.getEditableText().toString().trim());
                            user.put("gender",sel_gen.getText().toString().trim());
                            user.put("dateOfBirth",dob.getEditableText().toString());
                            docref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast toast = Toast.makeText(getBaseContext(),"Registration Sucessful !!",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                    mAuth.signOut();
                                    startActivity(new Intent(getBaseContext(),LoginActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast toast=Toast.makeText(getBaseContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                    final FirebaseUser userThis = mAuth.getCurrentUser();
                                    userThis.delete();
                                }
                            });
                        }
                        else
                        {
                            Toast toast = Toast.makeText(getBaseContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                    }
                });
                loadingProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        this.dr_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFirstname() | !validateLastname() | !validateCity() | !validateEmail() | !validatePhone() | !validateDate() | !validatePassword() | !validateConfirmPassword())
                {
                    return;
                }
                openDialog();
            }
        });
    }
    public void openDialog(){
        Confirm_dr_dialog confirmdialog = new Confirm_dr_dialog();
        confirmdialog.show(getSupportFragmentManager(),"example");
    }


    private void updateLabel( EditText dob, Calendar myCal) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(sdf.format(myCal.getTime()));
    }


    private boolean validateFirstname() {
        String usernameInput = firstname.getEditableText().toString().trim();
        String[] arr= usernameInput.split(" ");
        if (usernameInput.isEmpty()) {
            firstname.setError("Field can't be empty");
            return false;
        } else if (arr.length>1) {
            firstname.setError("Only one word required");
            return false;
        } else {
            firstname.setError(null);
            return true;
        }
    }

    private boolean validateLastname() {
        String usernameInput = lastname.getEditableText().toString().trim();
        String[] arr= usernameInput.split(" ");
        if (usernameInput.isEmpty()) {
            lastname.setError(null);
            return true;
        } else if (arr.length>1) {
            lastname.setError("Only one word required");
            return false;
        } else {
            lastname.setError(null);
            return true;
        }
    }

    private boolean validateCity() {
        String cityname = city.getEditableText().toString().trim();
        String[] arr= cityname.split(" ");
        if (cityname.isEmpty()) {
            city.setError(null);
            return true;
        }
        else {
            city.setError(null);
            return true;
        }
    }
    private boolean validateEmail() {
        String emailInput = emailid.getEditableText().toString().trim();
        if (emailInput.isEmpty()) {
            emailid.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailid.setError("Please enter a valid email address");
            return false;
        } else {
            emailid.setError(null);
            return true;
        }
    }
    private boolean validatePhone() {
        String Input = phone.getEditableText().toString().trim();
        if (Input.isEmpty()) {
            phone.setError("Field can't be empty");
            return false;
        } else if (!PHONE_PATTERN.matcher(Input).matches()) {
            phone.setError("Please enter a valid Mobile Number");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String passwordInput = password.getEditableText().toString().trim();
        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Min. 5 characters required(No Spaces Allowed)");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }
    private boolean validateConfirmPassword() {
        String passwordInput = confirm_password.getEditableText().toString().trim();
        if (passwordInput.isEmpty()) {
            confirm_password.setError("Field can't be empty");
            return false;
        } else if (!(password.getEditableText().toString().trim().equals(passwordInput))) {
            confirm_password.setError("Password does not match");
            return false;
        } else {
            confirm_password.setError(null);
            return true;
        }
    }
    private boolean validateDate() {
        String Input = dob.getEditableText().toString().trim();
        if (Input.isEmpty()) {
            dob.setError("Date of Birth Required");
            return false;
        } else {
            dob.setError(null);
            return true;
        }
    }
}
