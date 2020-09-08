package com.finalproject.it.sanjeevni.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.finalproject.it.sanjeevni.R;

import java.util.HashMap;
import java.util.List;

public class ReviewAdapter extends ArrayAdapter {

    Context context;
    View convertView;

    public ReviewAdapter(@NonNull Context context, List<HashMap<String, Object>> hashMap) {
        super(context, R.layout.review_adapter,hashMap);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View thisView, @NonNull ViewGroup parent) {

        final HashMap<String,Object> hashMap1= (HashMap<String, Object>) getItem(position);
        this.convertView=thisView;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.review_adapter,parent,false);
        }

        TextView name=convertView.findViewById(R.id.name);
        TextView date=convertView.findViewById(R.id.date);
        TextView review=convertView.findViewById(R.id.review);

        String[] dates;
        dates=hashMap1.get("Time").toString().split(" ");

        name.setText(hashMap1.get("name").toString());
        date.setText(dates[0]);
        String reviewText="";
        if (hashMap1.get("apphelped").toString().equalsIgnoreCase("yes")){
            reviewText=reviewText+"App Helped : YES\n";
        }
        reviewText+=hashMap1.get("fullreview").toString();
        review.setText(reviewText);

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }


}
