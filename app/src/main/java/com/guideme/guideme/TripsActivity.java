package com.guideme.guideme;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TripsActivity extends AppCompatActivity {

    private int selectedCity, selectedCategory;
    private DatabaseReference dbRef;
    private Boolean searchMode = false;
    private Boolean itemSelected = false;
    private int selectedPosition = 0;
    private ListView dataListView;


    private ArrayList<String> cityItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trips);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedCity = bundle.getInt("city", 0);
            selectedCategory = bundle.getInt("category", 0);
        }

        dataListView = findViewById(R.id.dataListView);
        database = FirebaseDatabase.getInstance();
        String _city = "";
        switch (selectedCity) {
            case 1:
                _city = "Alexandria";
                break;
            case 2:
                _city = "Aswan";
                break;
            case 3:
                _city = "Cairo";
                break;
            case 4:
                _city = "Fayyoum";
                break;
            case 5:
                _city = "Giza";
                break;
            case 6:
                _city = "Hurghada";
                break;
            case 7:
                _city = "Luxor";
                break;
            case 8:
                _city = "Marsa Matruh";
                break;
            case 9:
                _city = "North Coast";
                break;
            case 10:
                _city = "Sharm El-Sheikh";
                break;
            case 11:
                _city = "Sharqia";
                break;
            case 12:
                _city = "Siwa";
                break;
            case 13:
                _city = "Taba";
                break;


        }
        databaseReference = database.getReference("Places/" + _city);


        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,
                cityItems);
        dataListView.setAdapter(adapter);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue(String.class);
                cityItems.add(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dataListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        selectedPosition = position;
                        itemSelected = true;
                    }
                });

    }

}



