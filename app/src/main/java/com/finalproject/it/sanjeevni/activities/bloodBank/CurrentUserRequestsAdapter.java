package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.finalproject.it.sanjeevni.R;

import java.util.HashMap;
import java.util.List;

public class CurrentUserRequestsAdapter extends ArrayAdapter {


    private static final String TAG = "Custom_Adapter";
    private static final int REQUEST_CALL = 1;
    private static Context context;
    private int inputSelection=0;

    public CurrentUserRequestsAdapter(@NonNull Context context, List<HashMap<String, String>> hashMap) {
        super(context, R.layout.list_view_items, hashMap);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final HashMap<String,String> hashMap1= (HashMap<String, String>) getItem(position);
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.current_user_request_adapter,parent,false);
        }

        String date[];
        date=hashMap1.get("Time").split(" ");
        TextView name=convertView.findViewById(R.id.name);
        TextView bgroup=convertView.findViewById(R.id.bgroup);
        TextView city=convertView.findViewById(R.id.city);
        final TextView contact=convertView.findViewById(R.id.contact);
        TextView time=convertView.findViewById(R.id.time);
        TextView index=convertView.findViewById(R.id.index);
        TextView ans=convertView.findViewById(R.id.ans);
        ImageButton check=convertView.findViewById(R.id.done);
        final View tempView=convertView;

        name.setText(hashMap1.get("name"));
        bgroup.setText(hashMap1.get("Bgroup"));
        city.setText(hashMap1.get("City"));
        contact.setText(hashMap1.get("Contact"));
        time.setText(date[0]);
        index.setText((position+1)+"");
        ans.setText(hashMap1.get("answered"));

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog= new AlertDialog.Builder(context);
                dialog.setTitle("Are You Sure ?");
                dialog.setMessage("This Request would be marked as Answered and not available for the Donors. This Step is irreversible ");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Dialog requestDialog = new Dialog(context);
                        ///requestDialog.setContentView(R.layout.layout);
                         ReviewDialogBuilder requestDialog = new ReviewDialogBuilder(context);
                        requestDialog.create().show();
                    }
                }).create().show();
            }
        });



        return convertView;
    }

}
