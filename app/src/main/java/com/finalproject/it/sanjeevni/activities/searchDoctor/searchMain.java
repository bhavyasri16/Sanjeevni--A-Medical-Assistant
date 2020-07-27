package com.finalproject.it.sanjeevni.activities.searchDoctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.it.sanjeevni.R;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class searchMain extends AppCompatActivity {
    Spinner stateSpinner, citySpinner;
    Button btnSearch;
    TextView tvNoSelection;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Doctors");
        getSupportActionBar().setSubtitle("Doctors nearby you...");

        tvNoSelection = (TextView)findViewById(R.id.tvNoSelection);
        tvNoSelection.setVisibility(View.VISIBLE);

        ArrayList<String> data = new ArrayList<String>();
        data.add("Item 1");
        data.add("Item 2");
        data.add("Item 3");
        data.add("Item 1");
        data.add("Item 2");
        data.add("Item 3");
        data.add("Item 1");
        data.add("Item 2");
        data.add("Item 3");
        data.add("Item 1");
        data.add("Item 2");
        data.add("Item 3");
        data.add("Item 1");
        data.add("Item 2");
        data.add("Item 3");
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setVisibility(View.INVISIBLE);

        citySpinner = (Spinner)findViewById(R.id.citySpinner);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this, R.array.def, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        stateSpinner = (Spinner)findViewById(R.id.stateSpinner);
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.def, android.R.layout.simple_spinner_item);
                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySpinner.setAdapter(cityAdapter);
                        break;
                    case 1:
                        ArrayAdapter<CharSequence> cityAdapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.bihar_cities, android.R.layout.simple_spinner_item);
                        cityAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySpinner.setAdapter(cityAdapter1);
                        break;
                    case 2:
                        ArrayAdapter<CharSequence> cityAdapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.delhi_cities, android.R.layout.simple_spinner_item);
                        cityAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySpinner.setAdapter(cityAdapter2);
                        break;
                    case 3:
                        ArrayAdapter<CharSequence> cityAdapter3 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.gujarat_cities, android.R.layout.simple_spinner_item);
                        cityAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySpinner.setAdapter(cityAdapter3);
                        break;
                    case 4:
                        ArrayAdapter<CharSequence> cityAdapter4 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.mp_cities, android.R.layout.simple_spinner_item);
                        cityAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySpinner.setAdapter(cityAdapter4);
                        break;
                    case 5:
                        ArrayAdapter<CharSequence> cityAdapter5 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.mh_cities, android.R.layout.simple_spinner_item);
                        cityAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySpinner.setAdapter(cityAdapter5);
                        break;
                    case 6:
                        ArrayAdapter<CharSequence> cityAdapter6 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.up_cities, android.R.layout.simple_spinner_item);
                        cityAdapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySpinner.setAdapter(cityAdapter6);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stateSpinner.getSelectedItemPosition()==0 || citySpinner.getSelectedItemPosition()==0){
                    Toast.makeText(searchMain.this, "Please select a state and its city!", Toast.LENGTH_SHORT).show();

                } else {
                    tvNoSelection.setVisibility(View.INVISIBLE);
                    list.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}
