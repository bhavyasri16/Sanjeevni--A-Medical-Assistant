package com.finalproject.it.sanjeevni.activities.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.it.sanjeevni.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorRegister extends AppCompatActivity {

    private EditText fullname,address,dr_name,dr_spec,onlinedetail,oncalldetail;
    private RadioGroup category;
    private RadioButton sel_category;
    private CheckBox online,oncall,onspot;
    private Button add_dr,add_logo, submit;
    private String email,phone,state,city,password,userID;
    private List<String> drnames= new ArrayList<String>();
    private List<String> drspecs= new ArrayList<String>();
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private StorageReference storeRef;
    private ImageView imageView;
    private TextView categoryText,appointmentText,imageText;
    private Boolean logo_done=false;
    private ProgressBar progressBar;
    private int index=0,no_of_dr=0;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);
        final LayoutInflater factory = LayoutInflater.from(this);

        fullname= findViewById(R.id.name_of_hosp);
        address= findViewById(R.id.full_address);
        category = findViewById(R.id.category_options);
        online=findViewById(R.id.online);
        oncall=findViewById(R.id.oncall);
        onspot=findViewById(R.id.onspot);
        dr_name=findViewById(R.id.primary_dr);
        dr_spec=findViewById(R.id.spec);
        add_dr=findViewById(R.id.addmore);
        add_logo=findViewById(R.id.addlogo);
        submit=findViewById(R.id.submit);
        onlinedetail=findViewById(R.id.onlinedetail);
        oncalldetail=findViewById(R.id.oncalldetail);
        imageView=findViewById(R.id.logo_img);
        categoryText=findViewById(R.id.category);
        appointmentText=findViewById(R.id.appointment);
        imageText=findViewById(R.id.logo);
        progressBar=findViewById(R.id.loading);

        mAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        storeRef= FirebaseStorage.getInstance().getReference();

        phone = getIntent().getStringExtra("phoneNo");
        state=getIntent().getStringExtra("state");
        city=getIntent().getStringExtra("city");
        email=getIntent().getStringExtra("emailID");
        password=getIntent().getStringExtra("password");


        add_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent opengalleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(opengalleryintent,1000);
            }

        });

        add_dr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View textEntryView = factory.inflate(R.layout.text_entry_view, null);
                final EditText drName = textEntryView.findViewById(R.id.field1);
                final EditText drSpec = textEntryView.findViewById(R.id.field2);
                drName.setHint("Name of the Doctor");
                drSpec.setHint("Specialization");
                final AlertDialog.Builder add_dr_dialog = new AlertDialog.Builder(v.getContext());
                add_dr_dialog.setTitle("Add Doctor");
                add_dr_dialog.setMessage("Enter the Name exactly as it should be displayed .");
                add_dr_dialog.setView(textEntryView);

                add_dr_dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( !validateString(drName) | !validateString(drSpec))
                        {
                            Toast toast=Toast.makeText(getBaseContext(),"Entry Not Added.Both the fields are required.",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.getView().setAlpha(1);
                            toast.show();
                        }
                        else{
                        drnames.add(drName.getEditableText().toString().trim());
                        drspecs.add(drSpec.getEditableText().toString().trim());}
                    }
                }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                add_dr_dialog.create().show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateString(fullname) | !validateString(address) | !validateString(dr_name) | !validateString(dr_spec)
                        | !validateRadio(category,categoryText) | !validateCheckbox(online,oncall,onspot,appointmentText)
                        | !validateImage(logo_done,imageText) | !validateCheckBoxText(online,onlinedetail)
                        | !validateCheckBoxText(oncall,oncalldetail) )
                    return ;


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getBaseContext(),"authntication done",Toast.LENGTH_SHORT).show();
                        if(task.isSuccessful())
                        {
                            FirebaseUser fuser=mAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast toast = Toast.makeText(getBaseContext(),"Email Sent For Verification, Please Check !",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast toast = Toast.makeText(getBaseContext(),"Error in Email verification: "+ e.getMessage(),Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            });

                            userID=mAuth.getCurrentUser().getUid();
                            uploadImageToFirebase(imageUri);
                            DocumentReference docref= fstore.collection("Temp_Doctor_Details").document(userID);

                            Map<String,Object> user =new HashMap<>();
                            user.put("name",fullname.getEditableText().toString().trim());
                            sel_category=findViewById(category.getCheckedRadioButtonId());
                            user.put("category",sel_category.getText().toString().trim());
                            user.put("state",state);
                            user.put("city",city );
                            user.put("fullAddress",address.getEditableText().toString().trim());
                            user.put("emailID",email);
                            user.put("phoneNo",phone);
                            user.put("rating","NA");
                            if(online.isChecked())
                            {
                                user.put("onlineAppointment","YES");
                                user.put("onlineLink",onlinedetail.getEditableText().toString().trim());
                            }
                            else
                                user.put("onlineAppointment","NO");
                            if(oncall.isChecked())
                            {
                                user.put("oncallAppointment","YES");
                                user.put("oncallContact",oncalldetail.getEditableText().toString().trim());
                            }
                            else
                                user.put("oncallAppointment","NO");
                            if(onspot.isChecked())
                                user.put("onspotAppointment","YES");
                            else
                                user.put("onspotAppointment","NO");
                            user.put("drNamePrimary",dr_name.getEditableText().toString().trim());
                            user.put("drSpecPrimary",dr_spec.getEditableText().toString().trim());
                            for(int i=0;i<drnames.size();i++)
                            {
                                user.put("drName"+i, drnames.get(i));
                                user.put("drSpec"+i,drspecs.get(i));
                            }
                            user.put("no_of_otherdoctors",drnames.size());
                            docref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast toast = Toast.makeText(getApplicationContext(),"You will be Contacted soon for Verification !!",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,5);
                                    toast.show();
                                    mAuth.signOut();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast toast=Toast.makeText(getApplicationContext(),"Error in registration: "+e.getMessage(),Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,5);
                                    toast.show();
                                    final FirebaseUser userThis = mAuth.getCurrentUser();
                                    userThis.delete();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(getBaseContext(),RegisterActivity.class));
                                    finish();
                                }
                            });
                        }
                        else
                        {
                            Toast toast = Toast.makeText(getBaseContext(),"Error : "+task.getException().getMessage(),Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                    }
                });

            }
        });

        online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(android.widget.CompoundButton buttonView,boolean isChecked) {
                if(online.isChecked()) {
                    onlinedetail.setEnabled(true);
                    onlinedetail.setVisibility(View.VISIBLE);
                }
                else
                {
                    onlinedetail.setEnabled(false);
                    onlinedetail.setVisibility(View.INVISIBLE);
                }
            }
        });
        oncall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(android.widget.CompoundButton buttonView,boolean isChecked) {
                if(oncall.isChecked()) {
                    oncalldetail.setVisibility(View.VISIBLE);
                    oncalldetail.setEnabled(true);
                }
                else
                {
                    oncalldetail.setVisibility(View.INVISIBLE);
                    oncalldetail.setEnabled(false);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            logo_done=true;
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        final StorageReference fileref=storeRef.child("doctors/"+userID+"/logo_image.jpg");
        fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               /* fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView);
                    }
                });*/
                Toast.makeText(getBaseContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(),"Image Uploaded Failed.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateString(EditText anyString) {
        String userInput = anyString.getEditableText().toString().trim();
        if (userInput.isEmpty()) {
            anyString.setError("Field can't be empty");
            return false;
        } else {
            anyString.setError(null);
            return true;
        }
    }

    private boolean validateRadio(RadioGroup rg, TextView tv)
    {
        RadioButton sel= findViewById(rg.getCheckedRadioButtonId());
        if(sel==null) {
            tv.setError("Select One");
            return false;
        }
        else {
            tv.setError(null);
            return true;
        }
    }

    private boolean validateCheckbox(CheckBox ch1, CheckBox ch2, CheckBox ch3, TextView tv)
    {   if(!(ch1.isChecked() | ch2.isChecked() | ch3.isChecked() ))
        {   tv.setError("Select At least one:");
            return false;   }
        else
        {   tv.setError(null);
            return true;    }
    }


    private boolean validateCheckBoxText(CheckBox cb, EditText et) {
        if(cb.isChecked() && !validateString(et))
            return false;
        return true;
    }

    private boolean validateImage(Boolean b, TextView tv)
    {
        if(b==false)
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


}
