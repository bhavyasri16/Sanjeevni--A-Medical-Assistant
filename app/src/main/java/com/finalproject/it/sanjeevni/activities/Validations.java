package com.finalproject.it.sanjeevni.activities;

import android.widget.EditText;

public class Validations {

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
}
