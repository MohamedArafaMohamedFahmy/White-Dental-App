package com.arafa.mohamed.whitedental.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.arafa.mohamed.whitedental.Model.DoctorData;
import com.arafa.mohamed.whitedental.Adapter.MyAdapterDoctors;
import com.arafa.mohamed.whitedental.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class AddDoctor extends AppCompatActivity {
    TextInputEditText etDoctorName;
    AppCompatButton btSave;
    DatabaseReference databaseReference;
    DoctorData doctorData,retrieveDoctorData;
    ArrayList<DoctorData>  retrieveDoctorName;
    MyAdapterDoctors adapterDoctors;
    RecyclerView recyclerViewDoctors;
    String idDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        etDoctorName=findViewById(R.id.editText_doctor_name);
        btSave=findViewById(R.id.button_save);
        recyclerViewDoctors=findViewById(R.id.recyclerView_doctors);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        retrieveDoctorName=new ArrayList<>();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#184967"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Add Doctor");
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        btSave.setOnClickListener(v -> {
            String doctorName= Objects.requireNonNull(etDoctorName.getText()).toString();
            if(!doctorName.isEmpty()){
                idDoctor=databaseReference.push().getKey();
                doctorData=new DoctorData(doctorName,idDoctor);
                databaseReference.child("DoctorsName").child(idDoctor).setValue(doctorData).addOnCompleteListener(task -> Toast.makeText(AddDoctor.this, "Add Successfully", Toast.LENGTH_SHORT).show());
            }
            if(doctorName.isEmpty()){
                etDoctorName.setError("Please enter name of doctor");
            }
        });

      databaseReference.child("DoctorsName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    retrieveDoctorData = postSnapshot.getValue(DoctorData.class);
                    retrieveDoctorName.add(retrieveDoctorData);
                }

                if (!retrieveDoctorName.isEmpty()){
                    adapterDoctors = new MyAdapterDoctors(AddDoctor.this,retrieveDoctorName);
                    recyclerViewDoctors.setAdapter(adapterDoctors);
                    recyclerViewDoctors.setLayoutManager(new LinearLayoutManager(AddDoctor.this));

                }
                if (retrieveDoctorName.isEmpty()){
                    Toast.makeText(AddDoctor.this, "Not Doctor Name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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