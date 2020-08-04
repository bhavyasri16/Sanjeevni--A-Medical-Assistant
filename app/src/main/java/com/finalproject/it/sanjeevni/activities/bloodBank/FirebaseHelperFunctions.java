package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

public class FirebaseHelperFunctions {

    private static String TAG="Helper Class";
    private int index,FLAG=0;
    private FirebaseFirestore fstore=FirebaseFirestore.getInstance();


    //Check Presence of a user id in a document
    public int checkIDPresence(String collectionName, String documentName, final String userID){

        fstore.collection(collectionName).document(documentName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> temp=document.getData();
                    if (document.exists()) {
                        if(temp.containsValue(userID)) {
                            FLAG=1;
                            Log.d("TAG", "FOUND DOCUMENT ");
                            return;
                        }
                    }
                } else {
                    FLAG=0;
                    Log.d("TAG", "Error getting documents: "+ task.getException());
                }
            }
        });
        return FLAG;
    }

    //Update index field in a Document
    public void updateRequestIndex(final String collectionName,final String documentName, String fieldName) {
        index = getRequestIndex(collectionName,documentName,fieldName);
        DocumentReference docref = fstore.collection(collectionName).document(documentName);
        Map<String, Object> indexadd = new ArrayMap<>();
        indexadd.put(fieldName, index + 1);
        docref.update(indexadd).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"updateRequestIndexError:"+collectionName+"/"+documentName+" : "+e.getMessage());
            }
        });
    }

    //Get the value of index field in a document
    public int getRequestIndex(final String collectionName,final String documentName,final String fieldName) {
        index=0;
        final DocumentReference docref=fstore.collection(collectionName).document(documentName);
        docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Map<String,Object> thisData= value.getData();
                if(thisData.containsKey(fieldName)) {
                    index = Integer.parseInt(thisData.get(fieldName).toString());
                    Log.d("TAG", "INDEX VALUE "+index);
                    return;
                }
                else
                    index=0;
            }
        });

        return index;
    }
}
