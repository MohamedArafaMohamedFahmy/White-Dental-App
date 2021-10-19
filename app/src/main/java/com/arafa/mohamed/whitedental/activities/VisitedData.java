package com.arafa.mohamed.whitedental.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arafa.mohamed.whitedental.Adapter.MyAdapterVisited;
import com.arafa.mohamed.whitedental.Model.RetrievePatientsData;
import com.arafa.mohamed.whitedental.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class VisitedData extends AppCompatActivity {
    ProgressBar progressBar;
    RelativeLayout rlProgressBar;
    DatabaseReference databaseReference;
    RecyclerView listVisits;
    ArrayList<RetrievePatientsData> downloadData;
    RetrievePatientsData retrievePatientsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_data);
        progressBar=findViewById(R.id.progress_bar);
        rlProgressBar=findViewById(R.id.relative_layout);
        listVisits=findViewById(R.id.recyclerView_visited);
        downloadData=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference();


        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#184967"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Visits Patient");
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        rlProgressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Bundle extra=getIntent().getExtras();
        View parentLayout=findViewById(android.R.id.content);

        databaseReference.child("VisitsPatient").child(extra.getString("idPatient")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                downloadData.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    retrievePatientsData=postSnapshot.getValue(RetrievePatientsData.class);
                    downloadData.add(retrievePatientsData);

                }
                if (!downloadData.isEmpty()){
                    MyAdapterVisited adapterVisits = new MyAdapterVisited(VisitedData.this,downloadData,parentLayout,extra.getString("series"));
                    listVisits.setAdapter(adapterVisits);
                    listVisits.setLayoutManager(new LinearLayoutManager(VisitedData.this));
                    rlProgressBar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
                if (downloadData.isEmpty()){
                    rlProgressBar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    downloadData.clear();
                    MyAdapterVisited adapterVisits = new MyAdapterVisited(VisitedData.this,downloadData,parentLayout,extra.getString("series"));
                    listVisits.setAdapter(adapterVisits);
                    listVisits.setLayoutManager(new LinearLayoutManager(VisitedData.this));
                    Toast.makeText(VisitedData.this,"Not Visits ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VisitedData.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                rlProgressBar.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}