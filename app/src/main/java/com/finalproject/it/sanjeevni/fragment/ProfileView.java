package com.finalproject.it.sanjeevni.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.BaseActivity;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileView extends BaseActivity {

    private TextView name, email, phone, dob,place,donor,adhar,bloodgroup,gender,health;
    private Button save_changes;
    private CircleImageView dp;
    private ImageButton edit_gender,edit_dob,edit_mail,edit_phone,edit_place,edit_donor,edit_bg,edit_healthbg;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private StorageReference storageReference;
    private  String userID,nametext, emailtext, phonetext, dobtext,placetext,
            donortext="",adhartext,bloodgrouptext,gendertext,healthtext,city,state;
    private Uri imageUri;
    private boolean img=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        mAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        userID=mAuth.getCurrentUser().getUid();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        name=findViewById(R.id.name);
        edit_gender=findViewById(R.id.edit_gender);
        gender=findViewById(R.id.gender);
        dob=findViewById(R.id.dob);
        edit_dob=findViewById(R.id.edit_dob);
        email=findViewById(R.id.email);
        edit_mail=findViewById(R.id.edit_email);
        phone=findViewById(R.id.phone);
        edit_phone=findViewById(R.id.edit_phone);
        place=findViewById(R.id.place);
        edit_place=findViewById(R.id.edit_place);
        donor=findViewById(R.id.donor);
        edit_donor=findViewById(R.id.edit_donor);
        phone=findViewById(R.id.phone);
        edit_phone=findViewById(R.id.edit_phone);
        adhar=findViewById(R.id.adhar);
        bloodgroup=findViewById(R.id.bloodgroup);
        edit_bg=findViewById(R.id.edit_bgroup);
        health=findViewById(R.id.health);
        edit_healthbg=findViewById(R.id.edit_healthbg);
        save_changes=findViewById(R.id.dave_changes);
        dp=findViewById(R.id.profile_image);


        final DocumentReference docrref= fStore.collection("userDetails").document(userID);
        docrref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()) {
                    nametext = documentSnapshot.getString("firstName");
                    emailtext = documentSnapshot.getString("emailID");
                    phonetext = documentSnapshot.getString("phoneNo");
                    gendertext = documentSnapshot.getString("gender");
                    state = documentSnapshot.getString("state");
                    city = documentSnapshot.getString("city");
                    placetext = city + " , " + state;
                    dobtext = documentSnapshot.getString("dateOfBirth");
                    donortext = documentSnapshot.getString("donor");
                    if (donortext.equalsIgnoreCase("yes")) {
                        adhartext = documentSnapshot.getString("adhar");
                        bloodgrouptext = documentSnapshot.getString("blood_group");
                        healthtext = documentSnapshot.getString("disease_description");
                    }
                    setValues();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "No Information to Display", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    startActivity(new Intent(ProfileView.this,WelcomeActivity.class));
                    finish();
                }
            }
        });
        final StorageReference fileref=storageReference.child("user/"+userID+"/profile_image.jpg");
        fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(ProfileView.this).load(uri).into(dp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                int errorCode = ((StorageException) e).getErrorCode();
                    if(gendertext.equalsIgnoreCase("female")) {
                       // Glide.with(ProfileView.this).load(getImage("dp_female")).into(dp);
                        dp.setImageResource(R.drawable.dp_male);
                    }else {
                        dp.setImageResource(R.drawable.dp_male);
                    }

                Log.d("TAG",e.getMessage());
            }
        });


        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(img)
                    uploadImageToFirebase(imageUri);
            }
        });
    }

    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }
    public void onImageViewClicked(View view)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode== RESULT_OK)
            {
                imageUri=result.getUri();
                dp.setImageURI(imageUri);
                img=true;
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        final StorageReference fileref=storageReference.child("user/"+userID+"/profile_image.jpg");
        fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getBaseContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(),"Image Uploaded Failed.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setValues() {
        name.setText(nametext);
        dob.setText(dobtext);
        email.setText(emailtext);
        phone.setText(phonetext);
        place.setText(placetext);
        gender.setText(gendertext);
        if(donortext.equalsIgnoreCase("yes"))
        {
            donor.setText("You are Registered as a donor");
            adhar.setText(adhartext);
            bloodgroup.setText(bloodgrouptext);
            health.setText(healthtext);
        }
        else if(donortext.equalsIgnoreCase("no"))
        {
            donor.setText("Yor are NOT Registered as a donor");
            LinearLayout adharLayout=findViewById(R.id.adhar_layout),
                    bgroupLayout=findViewById(R.id.bg_layout),healthLayout=findViewById(R.id.health_layout);
            adharLayout.setVisibility(View.GONE);
            bgroupLayout.setVisibility(View.GONE);
            healthLayout.setVisibility(View.GONE);
        }
    }

}
