package com.finalproject.it.sanjeevni.activities.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.finalproject.it.sanjeevni.fragment.Reset_password;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" + "(?=\\S+$)" + ".{5,}" + "$");
    private FirebaseAuth mAuth;
    private Button loginButton,register_Button,forgotPasswordButton;
    private EditText usern,pass;
    private ProgressBar loadingProgressBar;
    private Drawable thisdra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usern =  findViewById(R.id.username);
        pass = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        register_Button = findViewById(R.id.register_button);
        forgotPasswordButton = findViewById(R.id.forgot_password);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !validateEmail() | !validatePassword())
                    return;
                //loadingProgressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(usern.getEditableText().toString().trim(),
                        pass.getEditableText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getBaseContext(),"Login Successfull !",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(), "Invalid Email or Password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        register_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // openDialog();

                final EditText resetMail = new EditText((v.getContext()));
                resetMail.setHint("Registered Email ID");
                AlertDialog.Builder passresetDialog = new AlertDialog.Builder(v.getContext());
                passresetDialog.setTitle("Reset Password ?");
                passresetDialog.setMessage("Enter Your Email To Receive Reset Link.");
                passresetDialog.setView(resetMail);

                passresetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetMail.getText().toString().trim();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getBaseContext(),"Password Link Sent To Your Email.",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getBaseContext(),"Reset Link Not Sent : "+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passresetDialog.create().show();
            }
        });
    }

    /*public void openDialog(){
        Reset_password confirmdialog = new Reset_password();
        confirmdialog.show(getSupportFragmentManager(),"example");
    }*/

    private boolean validateEmail() {
        String emailInput = usern.getEditableText().toString().trim();
        if (emailInput.isEmpty()) {
            usern.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            usern.setError("Please enter a valid email address");
            return false;
        } else {
            usern.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = pass.getEditableText().toString().trim();
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

}
