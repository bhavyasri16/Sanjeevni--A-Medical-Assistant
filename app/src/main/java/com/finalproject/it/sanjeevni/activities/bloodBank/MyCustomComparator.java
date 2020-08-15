package com.finalproject.it.sanjeevni.activities.bloodBank;

import java.util.Comparator;
import java.util.HashMap;

public class MyCustomComparator implements Comparator<HashMap<String, Object>> {
    @Override
    public int compare(HashMap<String, Object> lhs,
                       HashMap<String, Object> rhs) {

        return lhs.get("Time").toString().compareTo(rhs.get("Time").toString());
    }


}
