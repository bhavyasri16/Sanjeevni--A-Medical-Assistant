package com.finalproject.it.sanjeevni.activities.SearchDoctor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalproject.it.sanjeevni.R;
import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {
    private ArrayList<Doctor> doctors;

    ItemClicked activity;
    public interface ItemClicked{
        void onItemClicked(int index);
    }

    public DoctorAdapter(Context context, ArrayList<Doctor> list){
        doctors = list;
        activity = (ItemClicked)context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView drImg;
        TextView hName, dName, hStatus, rating;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            drImg = itemView.findViewById(R.id.drImg);
            hName = itemView.findViewById(R.id.hName);
            dName = itemView.findViewById(R.id.dName);
            hStatus = itemView.findViewById(R.id.hStatus);
            rating = itemView.findViewById(R.id.rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(doctors.indexOf((Doctor) v.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(doctors.get(i));
        viewHolder.hName.setText(doctors.get(i).gethName());
        viewHolder.dName.setText(doctors.get(i).getdName());
        viewHolder.hStatus.setText(doctors.get(i).gethStatus() + "(" + doctors.get(i).getLocation() + ")");
        viewHolder.rating.setText("0.1");
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }
}
