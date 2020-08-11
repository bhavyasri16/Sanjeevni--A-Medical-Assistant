package com.finalproject.it.sanjeevni.activities.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.finalproject.it.sanjeevni.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class My_custom_adapter extends ArrayAdapter {

    private static final String TAG = "Custom_Adapter";
    private FirebaseFirestore fstore;
    Context context;
    private StorageReference storeref;
    View convertView;

    public My_custom_adapter(@NonNull Context context,int resourceId, List<HashMap<String,String>> hashMap) {
        super(context, R.layout.list_view_items, hashMap);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View thisView, @NonNull ViewGroup parent) {
         final HashMap<String,String> hashMap1= (HashMap<String, String>) getItem(position);
         this.convertView=thisView;
         if(convertView==null)
         {
             convertView=LayoutInflater.from(getContext()).inflate(R.layout.list_view_items,parent,false);

         }
         TextView tvName= convertView.findViewById(R.id.name);
         TextView tvCity= convertView.findViewById(R.id.city);
         TextView tvContact=convertView.findViewById(R.id.phone);
         TextView tvEmail = convertView.findViewById(R.id.email);
         ImageButton add_user=convertView.findViewById(R.id.add_btn);
         ImageButton delete_btn=convertView.findViewById(R.id.delete_btn);

         tvName.setText(hashMap1.get("Name"));
         tvCity.setText(hashMap1.get("City"));
         tvContact.setText(hashMap1.get("Contact"));
         tvEmail.setText(hashMap1.get("Email"));

         add_user.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 AlertDialog.Builder dialog= new AlertDialog.Builder(convertView.getContext());
                 dialog.setTitle("Are You Sure ?").setMessage("On Confirmation this organisation would be added to the Verified List.");
                 dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         fstore=FirebaseFirestore.getInstance();
                         DocumentReference fromPath=fstore.collection("Temp_Doctor_Details").document(hashMap1.get("userID"));
                         DocumentReference toPath=fstore.collection("Doctor_Details").document(hashMap1.get("userID"));
                         moveFirestoreDocument(fromPath,toPath);
                     }
                 }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {

                     }
                 }).create().show();
             }
         });

         delete_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 AlertDialog.Builder dialog= new AlertDialog.Builder(convertView.getContext());
                 dialog.setTitle("Are You Sure ?");
                 dialog.setMessage("All the details of this Organisation would be Deleted. This action cannot be reversed.");
                 dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         fstore=FirebaseFirestore.getInstance();
                         storeref= FirebaseStorage.getInstance().getReference();
                         final DocumentReference fromPath=fstore.collection("Temp_Doctor_Details").document(hashMap1.get("userID"));
                         StorageReference thisRef=storeref.child("doctors/"+hashMap1.get("userID")+"/logo_image.jpg");
                         thisRef.delete().addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Log.d(TAG, "Image Deletion Failed:"+e.getMessage());
                             }
                         });
                         fromPath.delete()
                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void aVoid) {
                                         DocumentReference newRef=fstore.collection("To_Be_Deleted").document(hashMap1.get("userID"));
                                         Map<String,String> info = new HashMap<String,String>();
                                         info.put("info","Authentication Details Needs to be Deleted");
                                         newRef.set(info);
                                         Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                     }
                                 })
                                 .addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Log.w(TAG, "Error deleting document", e);
                                     }
                                 });
                     }
                 }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {

                     }
                 }).create().show();
             }
         });

         return convertView;
    }



    public void moveFirestoreDocument(final DocumentReference fromPath, final DocumentReference toPath) {
        fromPath.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        toPath.set(document.getData())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        fromPath.delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
