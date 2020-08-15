package com.finalproject.it.sanjeevni.activities.searchDoctor;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.finalproject.it.sanjeevni.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends ArrayAdapter<SearchItem> {
    private Context context;
    private List<SearchItem> list;

    public SearchAdapter(@NonNull Context context, int resource, @NonNull List<SearchItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        SearchItem searchItem = list.get(position);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_layout, null);

        TextView name = (TextView)view.findViewById(R.id.hName);
        TextView category = (TextView)view.findViewById(R.id.hCategory);
        TextView rating = (TextView)view.findViewById(R.id.rating);
        final ImageView img = (ImageView) view.findViewById(R.id.img);
        final ProgressBar img_load = (ProgressBar)view.findViewById(R.id.img_load);


        name.setText(searchItem.getName());
        category.setText(searchItem.getCategory());
        rating.setText(searchItem.getRating());
        searchItem.getGetImageUrl().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).resize(100, 100).into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        img_load.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });

        return view;
    }
}
