package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.finalproject.it.sanjeevni.R;

public class bbDonate extends AppCompatActivity {
    TextInputLayout inputName, inputContact, inputIDProof, searchBGroup, searchCity;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bb_donate);

        inputName = (TextInputLayout)findViewById(R.id.inputName);
        inputContact = (TextInputLayout)findViewById(R.id.inputContact);
        inputIDProof = (TextInputLayout)findViewById(R.id.inputIDProof);
        searchBGroup = (TextInputLayout)findViewById(R.id.searchBgroup);
        searchCity = (TextInputLayout)findViewById(R.id.searchCity);
        btnSearch = (Button)findViewById(R.id.btnSearch);

        //adding back button in Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Blood Bank");
        getSupportActionBar().setSubtitle("Search donor...");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateID();
                if(!validateName() | !validateContact() | !validateBGroup() | !validateCity() | !validateID()){
                    Toast.makeText(bbDonate.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
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
        if(contact.length() == 0)
            return false;
        return true;
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
