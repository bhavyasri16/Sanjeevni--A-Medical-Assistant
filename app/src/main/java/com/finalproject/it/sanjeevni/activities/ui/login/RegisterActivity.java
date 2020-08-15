package com.finalproject.it.sanjeevni.activities.ui.login;

import android.app.DatePickerDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.Validations;

import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class RegisterActivity extends AppCompatActivity {

    private EditText firstname, emailid, phone, password, confirm_password, dob;
    private TextView statetext,citytext;
    private RadioGroup gender;
    private RadioButton sel_gen;
    private Button registerButton,dr_register;
    private Spinner state_spinner,city_spinner;
    private ArrayAdapter<City> cityArrayAdapter;
    private ProgressBar loadingProgressBar;
    private String stateSelected,citySelected;
    private StateCity_List scl= new StateCity_List();
    private int stateID=0,cityID=0;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadingProgressBar = findViewById(R.id.loading);
        firstname = findViewById(R.id.first_name);
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
        final Validations vd=new Validations();

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
        ArrayAdapter<State> stateArrayAdapter = new ArrayAdapter<State>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, scl.states);
        stateArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        state_spinner.setAdapter(stateArrayAdapter);

        cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, scl.cities);
        cityArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        city_spinner.setAdapter(cityArrayAdapter);

        state_spinner.setOnItemSelectedListener(state_listener);
        city_spinner.setOnItemSelectedListener(city_listener);

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
                if (!vd.validateString(firstname) | validateCity() | vd.validateEmail(emailid) | vd.validatePhone(phone) | !vd.validateDate(dob) | vd.validatePassword(password) | validateConfirmPassword())
                {
                    return;
                }

                loadingProgressBar.setVisibility(View.VISIBLE);
                            sel_gen= findViewById(gender.getCheckedRadioButtonId());
                            intent = new Intent(getBaseContext(),Verification_phone.class);
                            intent.putExtra("firstName",firstname.getEditableText().toString().trim());
                            intent.putExtra("emailID",emailid.getEditableText().toString().trim());
                            intent.putExtra("phoneNo",phone.getEditableText().toString().trim());
                            intent.putExtra("state",stateSelected);
                            intent.putExtra("city",citySelected);
                            intent.putExtra("gender",sel_gen.getText().toString().trim());
                            intent.putExtra("dateOfBirth",dob.getEditableText().toString());
                            intent.putExtra("password",password.getEditableText().toString().trim());
                            startActivity(intent);
                            finish();


                loadingProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        this.dr_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCity() | vd.validateEmail(emailid) | vd.validatePhone(phone) | vd.validatePassword(password) | validateConfirmPassword())
                {
                    return;
                }
                loadingProgressBar.setVisibility(View.VISIBLE);
                intent = new Intent(getBaseContext(),DoctorRegister.class);
                intent.putExtra("emailID",emailid.getEditableText().toString().trim());
                intent.putExtra("phoneNo",phone.getEditableText().toString().trim());
                intent.putExtra("state",stateSelected);
                intent.putExtra("city",citySelected);
                intent.putExtra("password",password.getEditableText().toString().trim());
                startActivity(intent);
                finish();


                loadingProgressBar.setVisibility(View.INVISIBLE);

            }
        });
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

    private boolean validateCity(){
        if(cityID==0)
        {
            citytext.setVisibility(View.VISIBLE);
            city_spinner.setVisibility(View.INVISIBLE);
            citytext.setError("Please Select One");
            return true;
        }
        if(stateID==0)
        {
            statetext.setVisibility(View.VISIBLE);
            state_spinner.setVisibility(View.INVISIBLE);
            statetext.setError("Please Select One");
            return true;
        }
        return false;
    }

    private boolean validateConfirmPassword() {
        String passwordInput = confirm_password.getEditableText().toString().trim();
        if (passwordInput.isEmpty()) {
            confirm_password.setError("Field can't be empty");
            return true;
        } else if (!(password.getEditableText().toString().trim().equals(passwordInput))) {
            confirm_password.setError("Password does not match");
            return true;
        } else {
            confirm_password.setError(null);
            return false;
        }
    }

}
