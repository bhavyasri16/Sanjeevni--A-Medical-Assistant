package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.it.sanjeevni.R;
import com.google.android.material.textfield.TextInputLayout;

public class bbRegister extends AppCompatActivity {
    TextInputLayout inputID, inputBGroup;
    EditText etDisease;
    Button btnReg;
    RadioGroup rbDisease;
    RadioButton rbYes, rbNo;
    CheckBox cbDeclaration;

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
                validateID();
                if(!validateID() || !validateBGroup())
                    Toast.makeText(bbRegister.this, "Please fill all the fields as required!", Toast.LENGTH_SHORT).show();
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
        String BGroup = inputBGroup.getEditText().getText().toString().trim();
        if(BGroup.length() == 0)
            return false;
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
}
