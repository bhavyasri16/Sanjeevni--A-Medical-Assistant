package com.finalproject.it.sanjeevni.activities;

import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Validations {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" + "(?=\\S+$)" + ".{6,}" + "$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^" + "(?=\\S+$)" + ".{10,}" + "$");
    public boolean validateString(EditText anyString) {
        String userInput = anyString.getEditableText().toString().trim();
        if (userInput.isEmpty()) {
            anyString.setError("Field can't be empty");
            return false;
        } else {
            anyString.setError(null);
            return true;
        }
    }

    public boolean validateEmail(EditText anyEmail) {
        String emailInput = anyEmail.getEditableText().toString().trim();
        if (emailInput.isEmpty()) {
            anyEmail.setError("Field can't be empty");
            return true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            anyEmail.setError("Please enter a valid email address");
            return true;
        } else {
            anyEmail.setError(null);
            return false;
        }
    }

    public boolean validatePassword(EditText password) {
        String passwordInput = password.getEditableText().toString().trim();
        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return true;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Min. 6 characters required(No Spaces Allowed)");
            return true;
        } else {
            password.setError(null);
            return false;
        }
    }

    public boolean validatePhone(EditText phone) {
        String Input = phone.getEditableText().toString().trim();
        if (Input.isEmpty()) {
            phone.setError("Field can't be empty");
            return true;
        } else if (!PHONE_PATTERN.matcher(Input).matches()) {
            phone.setError("Please enter a valid Mobile Number");
            return true;
        } else {
            phone.setError(null);
            return false;
        }
    }

    public boolean validateDate(EditText dob) {
        String Input = dob.getEditableText().toString().trim();
        if (Input.isEmpty()) {
            dob.setError("Date Required");
            return false;
        } else {
            dob.setError(null);
            return true;
        }
    }

    public boolean validateCheckbox3(CheckBox ch1, CheckBox ch2, CheckBox ch3, TextView tv)
    {   if(!(ch1.isChecked() | ch2.isChecked() | ch3.isChecked() ))
        {   tv.setError("Select At least one:");
            return false;   }
        else
        {   tv.setError(null);
            return true;    }
    }

    public boolean validateRadio(View view, RadioGroup rg, TextView tv)
    {
        RadioButton sel = view.findViewById(rg.getCheckedRadioButtonId());
        if(sel==null) {
            tv.setError("Select One");
            return false;
        }
        else {
            tv.setError(null);
            return true;
        }
    }
    public boolean validateImage(Boolean b, TextView tv)
    {
        if(!b)
        {
            tv.setError("Image Required.");
            return false;
        }
        else
        {
            tv.setError(null);
            return true;
        }
    }

    public boolean validateCheckBoxText(CheckBox cb, EditText et) {
        return cb.isChecked() && validateString(et);
    }

    public boolean validateBGroup(EditText inputBGroup){
        String BGroup = inputBGroup.getEditableText().toString().trim();
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

    public boolean validateBGroup(TextInputLayout inputBGroup){
        String BGroup = inputBGroup.getEditText().getText().toString().trim();
        BGroup=BGroup.toUpperCase();
        BGroup = BGroup.replaceAll("\\s", "");
        String[] groups= {"A+","A-","B+","B-","O+","O-","AB-","AB+"};
        if(BGroup.length() == 0)
            return true;
        if(!Arrays.asList(groups).contains(BGroup))
        {
            inputBGroup.setError("Valid Blood Groups : A+, A-, B+, B-, O+, O-, AB-, AB+");
            return true;
        }
        return false;
    }

    public boolean validateID(TextInputLayout inputID){
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
}
