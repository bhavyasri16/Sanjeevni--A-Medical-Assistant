package com.finalproject.it.sanjeevni.activities.ui.login;

import android.widget.ArrayAdapter;

import com.finalproject.it.sanjeevni.R;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.finalproject.it.sanjeevni.activities.ui.login.State;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class StateCity_List {

    protected ArrayList<State> states=new ArrayList<>();
    protected ArrayList<City> cities= new ArrayList<>();
    private ArrayAdapter adapter;

    protected void createLists() {


        State state0 = new State(0,  "Select Your State");
        State state1 = new State(1,  "Uttar Pradesh");
        State state2 = new State(2,"Madhya Pradesh");
        State state3 = new State(3,"Delhi");
        //State state2 = new State(2,  "Maharashtra");

        states.add(state0);
        states.add(state1);
        states.add(state2);
        states.add(state3);

        cities.add(new City(0,  state0, "Select Your City"));
        cities.add(new City(1,  state1, "Prayagraj"));
        cities.add(new City(2,  state1, "Varanasi"));
        cities.add(new City(3,  state1, "Sultanpur"));
        cities.add(new City(4,  state1, "Kanpur"));
        cities.add(new City(5,  state1, "Lucknow"));
        cities.add(new City(6,  state1, "Moradabad"));
        cities.add(new City(7, state3,"Old Delhi"));
        cities.add(new City(8, state2,"Gwalior"));
        cities.add(new City(9, state2,"Ujjain"));
        cities.add(new City(10,state3,"New Delhi"));

    }

}
