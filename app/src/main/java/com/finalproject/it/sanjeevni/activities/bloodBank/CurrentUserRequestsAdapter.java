package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.finalproject.it.sanjeevni.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentUserRequestsAdapter extends ArrayAdapter {


    private static final String TAG = "Custom_Adapter";
    private static Context context;
    private String yes_no,review;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;
    private int requestNoForThisUser=0;
    private int activeRequests=0,reviewIndex=0;

    public CurrentUserRequestsAdapter(@NonNull Context context, List<HashMap<String, Object>> hashMap) {
        super(context, R.layout.list_view_items, hashMap);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final HashMap<String,Object> hashMap1= (HashMap<String, Object>) getItem(position);
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.current_user_request_adapter,parent,false);
        }

        fAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        final String userID= fAuth.getCurrentUser().getUid();

        String[] date;
        date=hashMap1.get("Time").toString().split(" ");
        TextView name=convertView.findViewById(R.id.name);
        TextView bgroup=convertView.findViewById(R.id.bgroup);
        TextView city=convertView.findViewById(R.id.city);
        final TextView contact=convertView.findViewById(R.id.contact);
        TextView time=convertView.findViewById(R.id.time);
        TextView index=convertView.findViewById(R.id.index);
        TextView ans=convertView.findViewById(R.id.ans);
        ImageButton check=convertView.findViewById(R.id.done);
        final View tempView=convertView;
        String pos=(position+1)+"";

        name.setText(hashMap1.get("name").toString());
        bgroup.setText(hashMap1.get("Bgroup").toString());
        city.setText(hashMap1.get("City").toString());
        contact.setText(hashMap1.get("Contact").toString());
        time.setText(date[0]);
        index.setText(pos);
        ans.setText(hashMap1.get("answered").toString());

        //if(ans.getText().toString().equalsIgnoreCase("YES"))
        //    check.setVisibility(View.INVISIBLE);


        final DocumentReference docref=fstore.collection("userResponse").document("01_ReviewIndex");
        docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                // thisData= value.getData();
                if(value.exists()) {

                    if (value.contains("NoOfReviews")) {
                        reviewIndex = Integer.parseInt(value.get("NoOfReviews").toString());
                    }
                }
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog= new AlertDialog.Builder(context);
                dialog.setTitle("Are You Sure ?");
                dialog.setMessage("This Request would be marked as Answered and not available for the Donors. This Step is irreversible ");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ReviewDialogBuilder requestDialog = new ReviewDialogBuilder(context);
                        requestDialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                yes_no=requestDialog.getSelectedOption();
                                review=requestDialog.getReviewText();
                                reviewEntry(yes_no,review,hashMap1);
                                alterReqList(hashMap1);
                              //  changeView(tempView);
                              //  updateIndex(userID);
                            }
                        });
                        requestDialog.create().show();
                    }
                }).create().show();
            }
        });
        return convertView;
    }

    private void updateIndex(final String userID) {

        final DocumentReference docref=fstore.collection("DonationRequestList").document("01_RequestIndex");
        docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()) {
                    if (value.contains("ActiveRequests") ) {
                        activeRequests = Integer.parseInt(value.get("ActiveRequests").toString());
                        /*Map<String, Object> indexadd = new ArrayMap<>();
                        indexadd.put("ActiveRequests", (activeRequests - 1));
                        docref.update(indexadd).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "updateRequestIndexError: " + e.getMessage());
                            }
                        });*/
                    }
                }
            }
        });




        final DocumentReference docref1=fstore.collection("DonationRequestList").document("02_RequestNoPerUser");
        docref1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.contains(userID)) {
                    requestNoForThisUser = Integer.parseInt(value.get(userID).toString());
                    Map<String, Object> indexaddnew = new ArrayMap<>();
                    indexaddnew.put(userID, (requestNoForThisUser - 1));
                    docref1.update(indexaddnew).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG,"updateRequestIndexError: "+e.getMessage());
                        }
                    });
                }
            }
        });



    }

    private void changeView(View tempView) {
        LinearLayout main=tempView.findViewById(R.id.main_layout);
        main.setBackgroundColor(Color.LTGRAY);
        ImageButton check=tempView.findViewById(R.id.done);
        check.setVisibility(View.INVISIBLE);
        check.setEnabled(false);
        check.setClickable(false);
    }

    private void alterReqList(HashMap<String, Object> hashMap1) {

        HashMap<String,Object> alter=new HashMap<>();
        alter.put("Answered","YES");
        fstore.collection("DonationRequestList").document(hashMap1.get("docID").toString())
                .update(alter).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","Changing Answered Field :"+e.getMessage());
            }
        });

    }

    private void reviewEntry(String yes_no, String review,HashMap<String,Object> hash) {

        HashMap<String,Object> reviewDetails= new HashMap<>() ;
        reviewDetails.put("name",hash.get("name").toString());
        reviewDetails.put("userID",fAuth.getCurrentUser().getUid());
        reviewDetails.put("timestamp",java.text.DateFormat.getDateTimeInstance().format(new Date()));
        reviewDetails.put("AppHelped",yes_no);
        reviewDetails.put("fullReview",review);
        reviewDetails.put("email",fAuth.getCurrentUser().getEmail());

        fstore.collection("userResponse").document("Review_"+reviewIndex)
                .set(reviewDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap<Object,Object> entry=new HashMap<>();
                        entry.put("NoOfReviews",reviewIndex+1);
                        fstore.collection("userResponse").document("01_ReviewIndex").set(entry);
                        Toast.makeText(context,"Successful !!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","Error in review DB:"+e.getMessage());
            }
        });
    }


}
