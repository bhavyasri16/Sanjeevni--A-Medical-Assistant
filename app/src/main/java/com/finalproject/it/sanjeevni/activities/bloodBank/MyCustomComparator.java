package com.finalproject.it.sanjeevni.activities.bloodBank;

import java.util.Comparator;
import java.util.HashMap;

public class MyCustomComparator implements Comparator<HashMap<String, String>> {
    @Override
    public int compare(HashMap<String, String> lhs,
                       HashMap<String, String> rhs) {

        return lhs.get("Time").compareTo(rhs.get("Time"));
    }
}
