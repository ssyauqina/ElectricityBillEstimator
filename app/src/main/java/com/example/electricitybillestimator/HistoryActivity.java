package com.example.electricitybillestimator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<BillModel> billList;

    BillAdapter adapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_history);

        // CONNECT XML

        recyclerView =
                findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        // ARRAY LIST

        billList = new ArrayList<>();

        // ADAPTER

        adapter = new BillAdapter(
                this,
                billList);

        recyclerView.setAdapter(adapter);

        // FIREBASE

        databaseReference =
                FirebaseDatabase.getInstance()
                        .getReference("Bills");

        // RETRIEVE DATA

        databaseReference.addValueEventListener(

                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot) {

                        // CLEAR OLD DATA

                        billList.clear();

                        // LOOP DATA

                        for(DataSnapshot dataSnapshot
                                : snapshot.getChildren()) {

                            BillModel model =
                                    dataSnapshot.getValue(
                                            BillModel.class);

                            // ADD LATEST RECORD FIRST

                            billList.add(0, model);
                        }

                        // REFRESH RECYCLERVIEW

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error) {

                    }
                });
    }
}