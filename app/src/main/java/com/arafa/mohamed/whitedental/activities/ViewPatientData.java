package com.arafa.mohamed.whitedental.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.arafa.mohamed.whitedental.Model.DoctorData;
import com.arafa.mohamed.whitedental.Adapter.ItemListAdapter;
import com.arafa.mohamed.whitedental.Adapter.MyAdapterPatients;
import com.arafa.mohamed.whitedental.Model.RetrievePatientsData;
import com.arafa.mohamed.whitedental.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ViewPatientData extends AppCompatActivity {
    RecyclerView listPatient;
    ArrayList<RetrievePatientsData> downloadData;
    ProgressBar progressBar;
    RelativeLayout rlProgressBar;
    DatabaseReference databaseReference;
    RetrievePatientsData retrievePatientsData;
    DoctorData retrieveDoctorName;
    MyAdapterPatients adapterPatients;
    Spinner spinnerDoctors;
    ArrayList<String> downloadDoctors;
    String selectNameDoctor;
    ItemListAdapter listLevel;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_data);
        listPatient=findViewById(R.id.recyclerView_patients);
        progressBar=findViewById(R.id.progress_bar);
        rlProgressBar=findViewById(R.id.relative_layout);
        downloadData=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        View parentLayout=findViewById(android.R.id.content);
        downloadDoctors=new ArrayList<>();
        spinnerDoctors=findViewById(R.id.list_doctors);



        toolbar=findViewById(R.id.toolbar_view_patients);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        rlProgressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.child("DoctorsName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                downloadDoctors.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    retrieveDoctorName = postSnapshot.getValue(DoctorData.class);
                    if(retrieveDoctorName != null) {
                        String DoctorName = retrieveDoctorName.getDoctorName();
                        downloadDoctors.add(DoctorName);
                    }else{
                        Toast.makeText(ViewPatientData.this, "Not found Doctor Names", Toast.LENGTH_SHORT).show();
                    }
                }

                if (!downloadDoctors.isEmpty()){
                    spinnerDoctors.setAdapter(listLevel);
                }
                if (downloadDoctors.isEmpty()){
                    Toast.makeText(ViewPatientData.this, "Not Doctor Names", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listLevel = new ItemListAdapter(ViewPatientData.this,R.layout.my_spinner,downloadDoctors);
        listLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectNameDoctor=spinnerDoctors.getSelectedItem().toString();

                databaseReference.child("PatientData").child(selectNameDoctor).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                            downloadData.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            retrievePatientsData=postSnapshot.getValue(RetrievePatientsData.class);
                            downloadData.add(retrievePatientsData);

                        }
                        if (!downloadData.isEmpty()){
                            adapterPatients = new MyAdapterPatients(ViewPatientData.this,downloadData,parentLayout);
                            listPatient.setAdapter(adapterPatients);
                            listPatient.setLayoutManager(new LinearLayoutManager(ViewPatientData.this));
                            rlProgressBar.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                        if (downloadData.isEmpty()){
                            rlProgressBar.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            downloadData.clear();
                            adapterPatients = new MyAdapterPatients(ViewPatientData.this,downloadData,parentLayout);
                            listPatient.setAdapter(adapterPatients);
                            listPatient.setLayoutManager(new LinearLayoutManager(ViewPatientData.this));
                            Toast.makeText(ViewPatientData.this,"Not Application ",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {
                        rlProgressBar.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ViewPatientData.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint(getString(R.string.search_by_name_or_phone_number));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    adapterPatients.getFilter().filter(newText);
                }
                return false;
            }
        });
        return true;
    }

}