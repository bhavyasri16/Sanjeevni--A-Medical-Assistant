package com.finalproject.it.sanjeevni.activities.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.Validations;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button loginButton,register_Button,forgotPasswordButton;
    private EditText usern,pass;

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
        final Validations vd=new Validations();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( vd.validateEmail(usern) | vd.validatePassword(pass))
                    return;
                mAuth.signInWithEmailAndPassword(usern.getEditableText().toString().trim(),
                        pass.getEditableText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getBaseContext(),"Login Successfull !",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
                            finish();
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

}
