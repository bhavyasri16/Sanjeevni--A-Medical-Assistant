package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.it.sanjeevni.R;
import com.google.android.material.textfield.TextInputLayout;

public class bbDonate extends AppCompatActivity {
    TextInputLayout inputName, inputContact, inputIDProof, searchBGroup, searchCity;
    Button btnSearch;

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
                }
            }
        });
    }

    private boolean validateName(){
        String name = inputName.getEditText().getText().toString().trim();
        if(name.length() == 0)
            return false;
        return true;
    }

    private boolean validateContact(){
        String contact = inputContact.getEditText().getText().toString().trim();
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
        String BGroup = searchBGroup.getEditText().getText().toString().trim();
        if(BGroup.length() == 0)
            return false;
        return true;
    }

    private boolean validateCity(){
        String city = searchCity.getEditText().getText().toString().trim();
        if(city.length() == 0)
            return false;
        return true;
    }

    private boolean validateID(){
        String IDProof = inputIDProof.getEditText().getText().toString().trim();
        if(IDProof.length() < 12){
            inputIDProof.setError("*A 12-digit number required!");
            return false;
        }
        else {
            inputIDProof.setError("");
            return true;
        }
    }




}
