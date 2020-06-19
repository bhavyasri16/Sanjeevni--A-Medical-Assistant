package com.finalproject.it.sanjeevni.activities.SearchDoctor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.it.sanjeevni.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFrag extends Fragment {

    public FilterFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }
}
