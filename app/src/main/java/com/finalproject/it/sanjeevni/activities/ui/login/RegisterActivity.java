package com.finalproject.it.sanjeevni.activities.ui.login;

import android.app.DatePickerDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.graphics.drawable.AdaptiveIconDrawable;
import android.content.Intent;
import android.os.Bundle;
import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.fragment.Confirm_dr_dialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthRegistrar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {

    private EditText firstname, lastname, emailid, phone, password, confirm_password, dob;
    private TextView statetext,citytext;
    private RadioGroup gender;
    private RadioButton sel_gen;
    private Button registerButton,dr_register;
    private Spinner state_spinner,city_spinner;
    private ArrayAdapter<State> stateArrayAdapter;
    private ArrayAdapter<City> cityArrayAdapter;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" + "(?=\\S+$)" + ".{5,}" + "$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^" + "(?=\\S+$)" + ".{10,}" + "$");
    private FirebaseAuth mAuth;
    private ProgressBar loadingProgressBar;
    private FirebaseFirestore fstore;
    private String stateSelected,citySelected;
    private StateCity_List scl= new StateCity_List();
    private int stateID=0,cityID=0;

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
        statetext=findViewById(R.id.state_text);
        citytext=findViewById(R.id.citytext);

        registerButton = findViewById(R.id.register_button);
        dr_register= findViewById(R.id.register_dr_button);
        city_spinner =  findViewById(R.id.city);
        state_spinner =  findViewById(R.id.state);
        dob=  findViewById(R.id.dob);

        statetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statetext.setVisibility(View.INVISIBLE);
                state_spinner.setVisibility(View.VISIBLE);
                state_spinner.performClick();
            }
        });

        citytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                citytext.setVisibility(View.INVISIBLE);
                city_spinner.setVisibility(View.VISIBLE);
                city_spinner.performClick();
            }
        });
        scl.createLists();
        stateArrayAdapter = new ArrayAdapter<State>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, scl.states);
        stateArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        state_spinner.setAdapter(stateArrayAdapter);

        cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, scl.cities);
        cityArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        city_spinner.setAdapter(cityArrayAdapter);

        state_spinner.setOnItemSelectedListener(state_listener);
        city_spinner.setOnItemSelectedListener(city_listener);

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
                if (!validateFirstname() | !validateLastname() | !validateCity() | !validateEmail()  | !validatePhone() | !validateDate() | !validatePassword() | !validateConfirmPassword())
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
                            sel_gen= findViewById(gender.getCheckedRadioButtonId());
                            Intent intent = new Intent(getBaseContext(),Verification_phone.class);
                            intent.putExtra("firstName",firstname.getEditableText().toString().trim());
                            intent.putExtra("lastName",lastname.getEditableText().toString().trim());
                            intent.putExtra("emailID",emailid.getEditableText().toString().trim());
                            intent.putExtra("phoneNo",phone.getEditableText().toString().trim());
                            intent.putExtra("state",stateSelected);
                            intent.putExtra("city",citySelected);
                            intent.putExtra("gender",sel_gen.getText().toString().trim());
                            intent.putExtra("dateOfBirth",dob.getEditableText().toString());
                            startActivity(intent);
                            finish();

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
                if (!validateFirstname() | !validateLastname() | ! validateCity() | !validateEmail()  | !validatePhone() | !validateDate() | !validatePassword() | !validateConfirmPassword())
                {
                    return;
                }
                openDialog();
            }
        });
    }
    public void openDialog(){
        Confirm_dr_dialog confirmdialog = new Confirm_dr_dialog();
        confirmdialog.show(getSupportFragmentManager(),"Confirmation");
    }


    private AdapterView.OnItemSelectedListener state_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position >= 0) {
                final State state = (State) state_spinner.getItemAtPosition(position);
                stateSelected=state.getStateName();
                stateID=state.getStateID();
                Log.d("SpinnerCountry", "onItemSelected: state: "+state.getStateID());
                ArrayList<City> tempCities = new ArrayList<>();

                State firstState = new State(0, "Choose a State");
                tempCities.add(new City(0, firstState, "Choose a City"));

                if(position!=0){
                for (City singleCity : scl.cities) {
                    if (singleCity.getState().getStateID() == state.getStateID()) {
                        tempCities.add(singleCity);
                    }
                }}
                cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, tempCities);
                cityArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                city_spinner.setAdapter(cityArrayAdapter);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener city_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final City city = (City) city_spinner.getItemAtPosition(position);
            citySelected=city.getCityName();
            cityID=city.getCityID();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

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

    private boolean validateCity(){
        if(cityID==0)
        {
            citytext.setVisibility(View.VISIBLE);
            city_spinner.setVisibility(View.INVISIBLE);
            citytext.setError("Please Select One");
            return false;
        }
        if(stateID==0)
        {
            statetext.setVisibility(View.VISIBLE);
            state_spinner.setVisibility(View.INVISIBLE);
            statetext.setError("Please Select One");
            return false;
        }
        return true;
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
