package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.BaseActivity;
import com.finalproject.it.sanjeevni.activities.Validations;
import com.finalproject.it.sanjeevni.activities.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class bbDonate extends BaseActivity {
    TextInputLayout inputName, inputContact, inputIDProof, searchBGroup, searchCity;
    Button btnSearch;
    String name, contact, IDProof, BGroup, city, message, userID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG="bbDonateActivity";
    private static int requestNoForThisUser=0,requestIndex=0,activeRequests=0;

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

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        final Validations vd=new Validations();

        if (Build.VERSION.SDK_INT >= 26) {
            if (checkPermission()) {
                Log.e("permission", "Permission already granted.");
            } else {
                requestPermission();
            }
        }
        //adding back button in Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Blood Connect");
        getSupportActionBar().setSubtitle("Search donor...");


        final DocumentReference docref=fstore.collection("DonationRequestList").document("01_RequestIndex");
        docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                // thisData= value.getData();
                if(value.exists()) {

                    if (value.contains("IndexCount")) {
                        requestIndex = Integer.parseInt(value.get("IndexCount").toString());
                    }
                    if (value.contains("ActiveRequests"))
                        activeRequests=Integer.parseInt(value.get("ActiveRequests").toString());
                }
            }
        });

        final DocumentReference docref1=fstore.collection("DonationRequestList").document("02_RequestNoPerUser");
        docref1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.contains(userID)) {
                    requestNoForThisUser = Integer.parseInt(value.get(userID).toString());
                    return;
                }
                else
                    requestNoForThisUser=0;
            }
        });


        Objects.requireNonNull(inputName.getEditText()).requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputName.getEditText(), InputMethodManager.SHOW_IMPLICIT);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                vd.validateID(inputIDProof);
                validateContact();
                if (!validateName() | !validateContact() | vd.validateBGroup(searchBGroup) | !validateCity() | !vd.validateID(inputIDProof)) {
                    Toast.makeText(bbDonate.this, "Please fill all the fields as required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkPermission()) {
                    IDProof=inputIDProof.getEditText().getText().toString().trim();
                    BGroup=searchBGroup.getEditText().getText().toString().trim();
                    addRequestAtIndex(requestIndex+1, view);
                } else {
                    Toast.makeText(bbDonate.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addRequestAtIndex(int i, final View view) {
        DocumentReference docref = fstore.collection("DonationRequestList").document("request_"+i);
        Map<String, Object> request = new ArrayMap<>();
        request.put("userID",userID);
        request.put("TimeStamp", java.text.DateFormat.getDateTimeInstance().format(new Date()));
        request.put("BloodGroup", BGroup);
        request.put("Name", name);
        request.put("City", city);
        request.put("Adhar",IDProof);
        request.put("Contact", contact);
        request.put("Answered", "NO");
        docref.set(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DocumentReference docref2 = fstore.collection("DonationRequestList").document("01_RequestIndex");
                Map<String, Object> indexadd = new ArrayMap<>();
                indexadd.put("IndexCount", requestIndex + 1);
                indexadd.put("ActiveRequests",activeRequests+1);
                docref2.set(indexadd).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"updateRequestIndexError: "+e.getMessage());
                    }
                });

                docref2 = fstore.collection("DonationRequestList").document("02_RequestNoPerUser");
                Map<String, Object> indexaddnew = new ArrayMap<>();
                indexaddnew.put(userID, requestNoForThisUser + 1);
                if(requestNoForThisUser==0)
                    docref2.set(indexaddnew);
                docref2.update(indexaddnew).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"updateRequestIndexError: "+e.getMessage());
                    }
                });

                getContacts(view);

            }
        });
    }

    private void getContacts(View view) {
        final List<HashMap<String,Object>> contactList = new ArrayList<>();
        fstore.collection("User_Type").document("bloodDonors").addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                int donorCount= Integer.parseInt(doc.get("donorCount").toString());
                for (int i =0; i<donorCount;i++) {
                    final String docid=doc.getString("donor_"+i);
                    Log.d("TAG here","id here"+docid);
                    DocumentReference tempDocRef=fstore.collection("userDetails").document(docid);
                    tempDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot donorData=task.getResult();
                                HashMap<String,Object> thisdonor=new HashMap<>();
                                thisdonor.put("name",donorData.getString("firstName"));
                                thisdonor.put("email",donorData.getString("emailID"));
                                thisdonor.put("userID",docid);
                                thisdonor.put("contact",donorData.getString("phoneNo"));
                                thisdonor.put("bgroup",donorData.getString("blood_group"));
                                thisdonor.put("city",donorData.getString("city"));
                                contactList.add(thisdonor);
                                if(thisdonor.get("bgroup").toString().equalsIgnoreCase(BGroup) && (thisdonor.get("city").toString()).equalsIgnoreCase(city))
                                {
                                    message = "Hello " + thisdonor.get("name") + " , If you are willing to donate " + BGroup + " in the city " + thisdonor.get("city")
                                            + ", Please Contact " + name + "( " + contact + " ) URGENTLY. Regards Sanjeevni Team.";
                                    try {
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(thisdonor.get("contact").toString(), null, message, null, null);
                                        //Toast.makeText(getApplicationContext(),"messg sent to "+thisdonor.get("name"),Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error in Sending Message : " + e, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                Log.d("TAG inside GetContacts","this entry: "+(donorData.getString("emailID")));
                            }
                        }
                    });
                }
            }
        });
        sendNotification();
        AlertDialog.Builder msgSent = new AlertDialog.Builder(view.getContext());
        msgSent.setTitle("Done !!");
        msgSent.setMessage("Message Sent to All the Potential Blood Donors. Take Care.");
        msgSent.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getBaseContext(), WelcomeActivity.class));
            }
        }).create().show();
    }

    private void sendNotification()
    {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_tag="donor";

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic MDc4MTNiYjktMzFmNC00Y2E2LWJkZjItNWZjYmQ3ODY5NDRk");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"9e64155a-bcbe-4d0d-ba1f-1867943ee5db\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_Type\", \"relation\": \"=\", \"value\": \"" + send_tag + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                //+ "\"contents\": {\"en\": \"We Have a Blood Donation Request.\"},"
                                +"\"template_id\": \"9614978e-b840-4751-8338-8f187c98dd6b\""
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(bbDonate.this, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(bbDonate.this,
                        "Permission accepted", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(bbDonate.this,
                        "Permission denied", Toast.LENGTH_LONG).show();
                Button sendSMS = (Button) findViewById(R.id.btnSearch);
                sendSMS.setEnabled(false);

            }
        }
    }

    private boolean validateName(){
        name = inputName.getEditText().getText().toString().trim();
        if(name.length() == 0)
            return false;
        return true;
    }

    private boolean validateContact(){
        contact = inputContact.getEditText().getText().toString().trim();
        if(contact.length()<10) {
            inputContact.setError("*A 10-digit number required!");
            return false;
        }
        else{
            inputContact.setError("");
            return true;
        }
    }

    private boolean validateCity(){
        if(city.length() == 0)
            return false;
        return true;
    }

    public void onSelectCity(final View view)
    {
        final SpinnerDialog dialog=new SpinnerDialog(view.getContext());
        dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialog.getCityID()!=0) {
                    city=dialog.getCitySelected();
                    searchCity.setHint(city);
                }else {
                    Toast toast = Toast.makeText(view.getContext(), "Please Select the City to filter.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
}
