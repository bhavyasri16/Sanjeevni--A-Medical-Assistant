package com.finalproject.it.sanjeevni.activities.bloodBank;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.ui.login.City;
import com.finalproject.it.sanjeevni.activities.ui.login.State;
import com.finalproject.it.sanjeevni.activities.ui.login.StateCity_List;

import java.util.ArrayList;

public class SpinnerDialog extends AlertDialog.Builder {

    private Spinner state_spinner,city_spinner;
    private String stateSelected,citySelected=null;
    private StateCity_List scl= new StateCity_List();
    private ArrayAdapter<City> cityArrayAdapter;
    private int cityID=0,stateID=0;

    public SpinnerDialog(Context context){
        super(context);
        View customView = View.inflate(context,
                R.layout.spinner_dialog, null);
        setView(customView);

        city_spinner =  customView.findViewById(R.id.city);
        state_spinner =  customView.findViewById(R.id.state);

        scl.createLists();
        ArrayAdapter<State> stateArrayAdapter = new ArrayAdapter<State>(context, R.layout.support_simple_spinner_dropdown_item, scl.states);
        stateArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        state_spinner.setAdapter(stateArrayAdapter);

        cityArrayAdapter = new ArrayAdapter<City>(getContext(), R.layout.support_simple_spinner_dropdown_item, scl.cities);
        cityArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        city_spinner.setAdapter(cityArrayAdapter);

        state_spinner.setOnItemSelectedListener(state_listener);
        city_spinner.setOnItemSelectedListener(city_listener);

        setTitle("Please Select Your Location");

    }

    private AdapterView.OnItemSelectedListener state_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position >= 0) {
                final State state = (State) state_spinner.getItemAtPosition(position);
                stateSelected=state.getStateName();
                stateID = state.getStateID();
                Log.d("SpinnerCountry", "onItemSelected: state: "+state.getStateID());
                ArrayList<City> tempCities = new ArrayList<>();

                State firstState = new State(0, "Choose a State");
                tempCities.add(new City(0, firstState, "Choose a City"));

                if(position!=0){
                    for (City singleCity : scl.cities) {
                        if (singleCity.getState().getStateID() == state.getStateID()) {
                            tempCities.add(singleCity);
                        }
                    }}
                cityArrayAdapter = new ArrayAdapter<City>(getContext(), R.layout.support_simple_spinner_dropdown_item, tempCities);
                cityArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                city_spinner.setAdapter(cityArrayAdapter);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Toast toast = Toast.makeText(getContext(), "MANDATORY : SELECT STATE", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    };

    private AdapterView.OnItemSelectedListener city_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final City city = (City) city_spinner.getItemAtPosition(position);
            citySelected=city.getCityName();
            cityID = city.getCityID();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Toast toast = Toast.makeText(getContext(), "MANDATORY : SELECT CITY", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    };

    public String getStateSelected() {
        return stateSelected;
    }

    public String getCitySelected() {
        return citySelected;
    }

    public int getCityID() {
        return cityID;
    }

    public int getStateID() {
        return stateID;
    }
}
