package com.arafa.mohamed.whitedental.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import com.arafa.mohamed.whitedental.Model.DoctorData;
import com.arafa.mohamed.whitedental.Adapter.ItemListAdapter;
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

public class PatientData extends AppCompatActivity {
    AppCompatButton btSend;
    TextInputEditText etSeries,etPatientName,etAge,etPhoneNumber,etAddress,etJob;
    String series,patientName,age,phoneNumber,address,job,selectName;
    DatabaseReference databaseReference;
    RetrievePatientsData retrievePatientsData;
    DoctorData retrieveDoctorName;
    Spinner spinnerDoctors;
    ArrayList<String> downloadDoctors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_data);

        btSend=findViewById(R.id.button_send);
        etSeries=findViewById(R.id.editText_series);
        etPatientName=findViewById(R.id.editText_name_patient);
        etAge=findViewById(R.id.editText_age);
        etPhoneNumber=findViewById(R.id.editText_phone_number);
        etAddress=findViewById(R.id.editText_address);
        etJob=findViewById(R.id.editText_job);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        downloadDoctors=new ArrayList<>();
        spinnerDoctors=findViewById(R.id.list_doctors);


        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#184967"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Patient Data");
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        ItemListAdapter listLevel = new ItemListAdapter(PatientData.this,R.layout.my_spinner,downloadDoctors);
        listLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectName=spinnerDoctors.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        databaseReference.child("DoctorsName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                downloadDoctors.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        retrieveDoctorName = postSnapshot.getValue(DoctorData.class);
                        if(retrieveDoctorName != null) {
                            String DoctorName = retrieveDoctorName.getDoctorName();
                            downloadDoctors.add(DoctorName);
                        }
                        else{
                            Toast.makeText(PatientData.this, "Not found Doctor Names", Toast.LENGTH_SHORT).show();
                        }
                    }

                if (!downloadDoctors.isEmpty()){
                    spinnerDoctors.setAdapter(listLevel);
                }
                if (downloadDoctors.isEmpty()){
                    Toast.makeText(PatientData.this, "Not Doctor Name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PatientData.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btSend.setOnClickListener(v -> {

            series = Objects.requireNonNull(etSeries.getText()).toString().trim();
            patientName = Objects.requireNonNull(etPatientName.getText()).toString().trim();
            age = Objects.requireNonNull(etAge.getText()).toString().trim();
            phoneNumber = Objects.requireNonNull(etPhoneNumber.getText()).toString().trim();
            address = Objects.requireNonNull(etAddress.getText()).toString().trim();
            job = Objects.requireNonNull(etJob.getText()).toString().trim();


            if(!series.isEmpty() && !patientName.isEmpty() && !age.isEmpty() && !phoneNumber.isEmpty() && !selectName.isEmpty() ){
                retrievePatientsData=new RetrievePatientsData(series,patientName,age,phoneNumber,address,job,selectName);
                databaseReference.child("PatientData").child(selectName).child(series).setValue(retrievePatientsData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){

                        Toast.makeText(PatientData.this, "Send Successfully", Toast.LENGTH_SHORT).show();
                        etSeries.getText().clear();
                        etPatientName.getText().clear();
                        etAge.getText().clear();
                        etPhoneNumber.getText().clear();
                        etAddress.getText().clear();
                        etJob.getText().clear();
                    }
                    else {
                        Toast.makeText(PatientData.this, ""+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }

            if(series.isEmpty()){
                etSeries.setError("Please enter series");
            }

            if(patientName.isEmpty()){
                etPatientName.setError("Please enter Patient Name");
            }
            if(age.isEmpty()){
                etAge.setError("Please enter Age");
            }

            if(phoneNumber.isEmpty()){
                etPhoneNumber.setError("Please enter phone number");
            }

        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}

