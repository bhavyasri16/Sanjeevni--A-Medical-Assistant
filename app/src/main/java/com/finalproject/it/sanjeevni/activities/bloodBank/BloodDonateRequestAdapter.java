package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.finalproject.it.sanjeevni.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BloodDonateRequestAdapter extends ArrayAdapter {

    private static final String TAG = "Custom_Adapter";
    private static final int REQUEST_CALL=1;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private final  List<HashMap<String,Object>> originalData=new ArrayList<>();
    private List<HashMap<String,Object>> filteredData=new ArrayList<>();

    public BloodDonateRequestAdapter(@NonNull Context context, List<HashMap<String,Object>> hashMap){
        super(context, R.layout.list_view_items, hashMap);
        this.context=context;
        this.filteredData.addAll(hashMap);
        this.originalData.addAll(hashMap);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final HashMap<String,Object> hashMap1= (HashMap<String, Object>) getItem(position);
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.blood_request_list_adapter,parent,false);
        }

        String date[];
        date=hashMap1.get("Time").toString().split(" ");
        TextView bgroup=convertView.findViewById(R.id.bgroup);
        TextView city=convertView.findViewById(R.id.city);
        TextView contact=convertView.findViewById(R.id.contact);
        TextView time=convertView.findViewById(R.id.time);
        TextView index=convertView.findViewById(R.id.index);
        ImageButton call=convertView.findViewById(R.id.btn);


        bgroup.setText(hashMap1.get("Bgroup").toString());
        city.setText(hashMap1.get("City").toString().toUpperCase());
        if(hashMap1.get("donor").toString().equalsIgnoreCase("true")) {
            contact.setText(hashMap1.get("Contact").toString());
        }
        else
            contact.setText("**********");
        time.setText(date[0]);
        index.setText((position+1)+"");




        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number=hashMap1.get("Contact").toString();
                if(hashMap1.get("donor").toString().equalsIgnoreCase("false"))
                {
                    AlertDialog.Builder dialog= new AlertDialog.Builder(context);
                    dialog.setTitle("Register First");
                    dialog.setMessage("You have to be Registered as Donor with us first.");
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();
                }
                else {
                    makePhoneCall(number);
                }
            }
        });


        return convertView;
    }

    static void makePhoneCall(String number) {
        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,
                    new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else{
            String dial = "tel:"+ number;
            context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    List<HashMap<String,Object>> filter(String city, String bGroup) {
        int len = originalData.size();

        filteredData.clear();
        if (bGroup.length() == 0 && city.length() == 0) {
            filteredData.addAll(originalData);
        } else  {
            for (int i = 0; i < len; i++) {
                HashMap<String,Object> current = originalData.get(i);
                 if (bGroup.length() != 0 && city.length()==0 && Objects.requireNonNull(current.get("Bgroup")).toString().equalsIgnoreCase(bGroup)) {
                    filteredData.add(current);
                }
                else if (city.length() != 0 && bGroup.length()==0 && Objects.requireNonNull(current.get("City")).toString().equalsIgnoreCase(city)) {
                    filteredData.add(current);
                }
                else if(Objects.requireNonNull(current.get("Bgroup")).toString().equalsIgnoreCase(bGroup) &&
                         Objects.requireNonNull(current.get("City")).toString().equalsIgnoreCase(city)){
                     filteredData.add(current);
                 }

            }

        }
        Toast.makeText(context,"filtered Data:"+filteredData.size(),Toast.LENGTH_SHORT).show();
        return filteredData;

    }

}

