package com.finalproject.it.sanjeevni.activities.searchDoctor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.it.sanjeevni.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchMain extends AppCompatActivity {
    Spinner stateSpinner, citySpinner;
    Button btnSearch;
    TextView tvNoSelection;
    ListView list;
    FirebaseFirestore fstore;
    StorageReference storeRef;
    private ArrayList<SearchItem> res_docs = new ArrayList<SearchItem>();
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Doctors");
        getSupportActionBar().setSubtitle("Doctors nearby you...");

        tvNoSelection = (TextView)findViewById(R.id.tvNoSelection);
        tvNoSelection.setVisibility(View.VISIBLE);

        list = (ListView)findViewById(R.id.list);
        list.setVisibility(View.INVISIBLE);

        fstore = FirebaseFirestore.getInstance();
        storeRef= FirebaseStorage.getInstance().getReference();

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
                if(stateSpinner.getSelectedItemPosition()==0){
                    Toast.makeText(SearchMain.this, "Please select a state and its city!", Toast.LENGTH_SHORT).show();
                } else if(citySpinner.getSelectedItemPosition()==0 && stateSpinner.getSelectedItemPosition()!=0){
                    Toast.makeText(SearchMain.this, "Please select a city as well!", Toast.LENGTH_SHORT).show();
                } else {
                    String city = citySpinner.getSelectedItem().toString();
                    fstore.collection("Doctor_Details")
                            .whereEqualTo("city", city)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if(e != null){
                                        Toast.makeText(SearchMain.this, "Error:"+e, Toast.LENGTH_SHORT).show();
                                    } else {
                                        if(!queryDocumentSnapshots.isEmpty()) {
                                            res_docs.clear();
                                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                                String docID = doc.getId();
                                                StorageReference fileRef = storeRef.child("doctors/" + docID + "/logo_image.jpg");
                                                res_docs.add(new SearchItem(doc.getId(), doc.getString("name"), doc.getString("category"), doc.getString("rating"), fileRef));
                                                ArrayAdapter<SearchItem> adapter = new SearchAdapter(getBaseContext(), 0, res_docs);
                                                list.setAdapter(adapter);
                                            }
                                        } else {
                                            list.setAdapter(null);
                                            Toast.makeText(SearchMain.this, "Sorry, we don't have any registered doctors in thiis area!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                            });
                    tvNoSelection.setVisibility(View.INVISIBLE);
                    list.setVisibility(View.VISIBLE);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SearchItem sel_item = res_docs.get(position);
                            String key = sel_item.getKey();
                            Intent intent = new Intent(getBaseContext(), DoctorDetails.class);
                            intent.putExtra("key", key);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

    }


}
