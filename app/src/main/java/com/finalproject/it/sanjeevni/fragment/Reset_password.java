package com.finalproject.it.sanjeevni.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.finalproject.it.sanjeevni.R;

import java.util.regex.Pattern;

public class Reset_password extends AppCompatDialogFragment {
    TextInputLayout pass,confirm_pass;
    Button reset_btn;
    private Reset_passwordListener listener;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" + "(?=\\S+$)" + ".{5,}" + "$");

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        final LayoutInflater inflator= getActivity().getLayoutInflater();
        final View view= inflator.inflate(R.layout.dialog_reset_password,null);
        builder.setView(view);

        reset_btn=(Button) view.findViewById(R.id.reset_btn);
        pass=(TextInputLayout) view.findViewById(R.id.password);
        confirm_pass=(TextInputLayout) view.findViewById(R.id.confirm_password);

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatePassword() | !validateConfirmPassword())
                    return;
                Toast toast=Toast.makeText(getContext(),"Password Reset Successfull",Toast.LENGTH_LONG);
                toast.getView().setAlpha(1);
                toast.show();
                dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dismiss();
            }
        });
        return builder.create();
    }

    private boolean validatePassword() {
        String passwordInput = pass.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            pass.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            pass.setError("Min. 5 characters required(No Spaces Allowed)");
            return false;
        } else {
            pass.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String passwordInput = confirm_pass.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            confirm_pass.setError("Field can't be empty");
            return false;
        } else if (!(pass.getEditText().getText().toString().trim().equals(passwordInput))) {
            confirm_pass.setError("Password does not match");
            return false;
        } else {
            confirm_pass.setError(null);
            return true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (Reset_passwordListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement Comfirm_dr_dialogListener");
        }
    }

    public interface Reset_passwordListener{

    }
}
