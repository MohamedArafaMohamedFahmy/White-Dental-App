package com.arafa.mohamed.whitedental.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arafa.mohamed.whitedental.Model.Controller;
import com.arafa.mohamed.whitedental.Model.DoctorData;
import com.arafa.mohamed.whitedental.Model.ImagesData;
import com.arafa.mohamed.whitedental.Adapter.ItemListAdapter;
import com.arafa.mohamed.whitedental.Model.RetrievePatientsData;
import com.arafa.mohamed.whitedental.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PatientDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    AppCompatButton btUpdate, btAddVisit;
    AppCompatTextView tvHintDate;
    AppCompatImageView imgPickerDate, imgUploadImage;
    TextInputEditText etPatientName, etAge, etPhoneNumber, etAddress, etJob, etChronicDiseases, etNotes, etAmount, etVisitNumber, etOperation;
    String retrieveIDPatient, idPatient, patientName, age, address, job, phoneNumber,
            chronicDiseases, notes, visitDate, amount, visitNumber, downloadUrl, operation,
            selectName, currentDate, uploadId, result = null;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    RetrievePatientsData updatePatientsData, updateHitData;
    ImagesData uploadImages;
    DoctorData retrieveDoctorName;
    Spinner spinnerDoctors;
    ArrayList<String> downloadDoctors;
    public Uri imgUri;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#184967"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Patient Details");
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        final Object objDetailed=getIntent().getSerializableExtra("patientData");
        updatePatientsData = (RetrievePatientsData) objDetailed;

        etPatientName = findViewById(R.id.editText_name_patient);
        etPhoneNumber = findViewById(R.id.editText_phone_number);
        etAge = findViewById(R.id.editText_age);
        etAddress = findViewById(R.id.editText_address);
        etJob = findViewById(R.id.editText_job);
        etChronicDiseases = findViewById(R.id.editText_chronic_diseases);
        etNotes = findViewById(R.id.editText_notes);
        etAmount = findViewById(R.id.editText_amount);
        etVisitNumber = findViewById(R.id.editText_visit_number);
        etOperation = findViewById(R.id.editText_operation);
        btUpdate = findViewById(R.id.button_update);
        btAddVisit = findViewById(R.id.button_add_visit);
        tvHintDate = findViewById(R.id.text_date);
        imgPickerDate = findViewById(R.id.image_date);
        spinnerDoctors = findViewById(R.id.list_doctors);
        imgUploadImage = findViewById(R.id.upload_image);
        downloadDoctors = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        parentLayout = findViewById(android.R.id.content);
        btUpdate.setText(R.string.register);

        if (updatePatientsData != null) {

            etPatientName.setText(updatePatientsData.getPatientName());
            etAge.setText(updatePatientsData.getAge());
            etPhoneNumber.setText(updatePatientsData.getPhoneNumber());
            etChronicDiseases.setText(updatePatientsData.getChronicDiseases());
            etNotes.setText(updatePatientsData.getNotes());
            etAddress.setText(updatePatientsData.getAddress());
            etJob.setText(updatePatientsData.getJob());
            retrieveIDPatient = updatePatientsData.getIdPatient();
            spinnerDoctors.setEnabled(false);
            btUpdate.setText(R.string.update);

        }


        ItemListAdapter listLevel = new ItemListAdapter(PatientDetails.this, R.layout.my_spinner, downloadDoctors);
        listLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectName = spinnerDoctors.getSelectedItem().toString();
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
                    }else{
                        Toast.makeText(PatientDetails.this, "Not found Doctor Names", Toast.LENGTH_SHORT).show();
                    }
                }

                if (!downloadDoctors.isEmpty()) {
                    spinnerDoctors.setAdapter(listLevel);

                }
                if (downloadDoctors.isEmpty()) {
                    Toast.makeText(PatientDetails.this, "Not Doctor Name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        btUpdate.setOnClickListener(v -> {

            patientName = Objects.requireNonNull(etPatientName.getText()).toString().trim();
            age = Objects.requireNonNull(etAge.getText()).toString().trim();
            phoneNumber = Objects.requireNonNull(etPhoneNumber.getText()).toString().trim();
            address = Objects.requireNonNull(etAddress.getText()).toString().trim();
            job = Objects.requireNonNull(etJob.getText()).toString().trim();
            chronicDiseases = Objects.requireNonNull(etChronicDiseases.getText()).toString().trim();
            notes = Objects.requireNonNull(etNotes.getText()).toString().trim();

            if (!patientName.isEmpty() && !age.isEmpty() && !phoneNumber.isEmpty() && retrieveIDPatient == null) {

                idPatient = databaseReference.push().getKey();

                updatePatientsData = new RetrievePatientsData(idPatient, patientName, age, phoneNumber, chronicDiseases, notes, address, job, selectName);

                databaseReference.child("PatientData").child(selectName).child(idPatient).setValue(updatePatientsData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PatientDetails.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                        addItemToSheet();
                        if (imgUri != null) {
                            StorageReference filePath = storageReference.child("UploadImages").child(retrieveIDPatient).child(System.currentTimeMillis() + "." + getFileExtension(imgUri));
                            final UploadTask uploadTask = filePath.putFile(imgUri);
                            uploadTask.addOnSuccessListener(taskSnapshot -> uploadTask.continueWithTask(task13 -> {
                                if (!task13.isSuccessful()) {
                                    throw Objects.requireNonNull(task13.getException());
                                }
                                downloadUrl = filePath.getDownloadUrl().toString();
                                return filePath.getDownloadUrl();
                            }).addOnCompleteListener(task14 -> {
                                if (task14.isSuccessful()) {
                                    downloadUrl = Objects.requireNonNull(task14.getResult()).toString();
                                    uploadId = databaseReference.push().getKey();
                                    uploadImages = new ImagesData(downloadUrl, currentDate, uploadId);
                                    databaseReference.child("ImagesPatients").child(retrieveIDPatient).child(uploadId).setValue(uploadImages);
                                }

                            }));
                        }

                    } else {
                        Toast.makeText(PatientDetails.this, "" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }

            if (!patientName.isEmpty() && !age.isEmpty() && !phoneNumber.isEmpty() && retrieveIDPatient != null) {


                updatePatientsData = new RetrievePatientsData(retrieveIDPatient, patientName, age, phoneNumber, chronicDiseases, notes, address, job, selectName);

                databaseReference.child("PatientData").child(selectName).child(retrieveIDPatient).setValue(updatePatientsData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PatientDetails.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                        updateData();
                        if (imgUri != null) {
                            StorageReference filePath = storageReference.child("UploadImages").child(retrieveIDPatient).child(System.currentTimeMillis() + "." + getFileExtension(imgUri));
                            final UploadTask uploadTask = filePath.putFile(imgUri);
                            uploadTask.addOnSuccessListener(taskSnapshot -> uploadTask.continueWithTask(task12 -> {
                               if (!task12.isSuccessful()) {
                                   throw Objects.requireNonNull(task12.getException());
                               }
                               downloadUrl = filePath.getDownloadUrl().toString();
                               return filePath.getDownloadUrl();
                           }).addOnCompleteListener(task1 -> {
                               if (task1.isSuccessful()) {
                                   downloadUrl = Objects.requireNonNull(task1.getResult()).toString();
                                   uploadId = databaseReference.push().getKey();
                                   uploadImages = new ImagesData(downloadUrl, currentDate, uploadId);
                                   databaseReference.child("ImagesPatients").child(retrieveIDPatient).child(uploadId).setValue(uploadImages);
                               }

                           }));
                        }

                    } else {
                        Toast.makeText(PatientDetails.this, "" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }


            if (age.isEmpty()) {
                etAge.setError("Please enter Age");
            }

            if (patientName.isEmpty()) {
                etPatientName.setError("Please enter Patient Name");
            }


            if (phoneNumber.isEmpty()) {
                etPhoneNumber.setError("Please enter phone number");
            }

        });

        btAddVisit.setOnClickListener(v -> {
            DatabaseReference databaseReferenceVisit = FirebaseDatabase.getInstance().getReference();
            amount = Objects.requireNonNull(etAmount.getText()).toString().trim();
            operation = Objects.requireNonNull(etOperation.getText()).toString().trim();
            visitNumber = Objects.requireNonNull(etVisitNumber.getText()).toString().trim();
            if (!amount.isEmpty() && !visitNumber.isEmpty() && visitDate != null && retrieveIDPatient == null) {
                updateHitData = new RetrievePatientsData(visitNumber, visitDate, amount, operation);
                databaseReferenceVisit.child("VisitsPatient").child(idPatient).child(visitNumber).setValue(updateHitData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PatientDetails.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PatientDetails.this, "" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }

            if (!amount.isEmpty() && !visitNumber.isEmpty() && visitDate != null && retrieveIDPatient != null) {
                updateHitData = new RetrievePatientsData(visitNumber, visitDate, amount, operation);
                databaseReferenceVisit.child("VisitsPatient").child(retrieveIDPatient).child(visitNumber).setValue(updateHitData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PatientDetails.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PatientDetails.this, "" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }


            if (visitNumber.isEmpty()) {
                etVisitNumber.setError("Please enter visit number");
            }
            if (amount.isEmpty()) {
                etAmount.setError("Please enter amount");
            }

            if (visitDate == null) {
                Toast.makeText(PatientDetails.this, "Please insert date", Toast.LENGTH_SHORT).show();
            }

        });


        imgPickerDate.setOnClickListener(v -> showDatePickerDialog());

        imgUploadImage.setOnClickListener(v -> imageChooser());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(retrieveIDPatient != null) {
            getMenuInflater().inflate(R.menu.sub_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_view_visits && retrieveIDPatient !=null) {
            Intent intent = new Intent(PatientDetails.this, VisitedData.class);
            intent.putExtra("idPatient", retrieveIDPatient);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_view_images && retrieveIDPatient !=null) {
            Intent intent = new Intent(PatientDetails.this, ImagesPatients.class);
            intent.putExtra("idPatient", retrieveIDPatient);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        visitDate = dayOfMonth + " - " + (month + 1) + " - " + year;
        tvHintDate.setVisibility(View.VISIBLE);
        tvHintDate.setText(visitDate);
    }

    public void imageChooser() {
        Intent intentImage = new Intent();
        intentImage.setType("image/*");
        intentImage.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intentImage);

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            imgUri = data.getData();
                            Picasso.get().load(imgUri).into(imgUploadImage);
                        }
                    }
                }
            });


    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void addItemToSheet() {

        patientName = Objects.requireNonNull(etPatientName.getText()).toString().trim();
        age = Objects.requireNonNull(etAge.getText()).toString().trim();
        phoneNumber = Objects.requireNonNull(etPhoneNumber.getText()).toString().trim();
        address = Objects.requireNonNull(etAddress.getText()).toString().trim();
        job = Objects.requireNonNull(etJob.getText()).toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzE_4p-Ql6SMaGsbjcakggpjqS9Z9f_VTQa9BMe-lzLItino2_p-Er-UVCax2jUfPUh/exec",
                response -> Toast.makeText(PatientDetails.this, response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(this, ""+error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();
                parmas.put("idPatient", idPatient);
                parmas.put("patientName", patientName);
                parmas.put("age", age);
                parmas.put("phoneNumber", phoneNumber);
                parmas.put("address", address);
                parmas.put("job", job);
                parmas.put("notes", notes);
                parmas.put("chronicDiseases", chronicDiseases);

                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    public void updateData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            //Background work here
            retrieveIDPatient = updatePatientsData.getIdPatient();
            patientName = Objects.requireNonNull(etPatientName.getText()).toString().trim();
            age = Objects.requireNonNull(etAge.getText()).toString().trim();
            phoneNumber = Objects.requireNonNull(etPhoneNumber.getText()).toString().trim();
            address = Objects.requireNonNull(etAddress.getText()).toString().trim();
            job = Objects.requireNonNull(etJob.getText()).toString().trim();
            chronicDiseases = Objects.requireNonNull(etChronicDiseases.getText()).toString().trim();
            notes = Objects.requireNonNull(etNotes.getText()).toString().trim();
            JSONObject jsonObject = Controller.updateData(parentLayout, retrieveIDPatient, patientName, age, phoneNumber, address, job,notes,chronicDiseases);


            try {
                if (jsonObject != null) {
                    result = jsonObject.getString("result");
                }
            } catch (JSONException je) {
                Toast.makeText(PatientDetails.this, "" + je.getMessage(), Toast.LENGTH_SHORT).show();

            }

            handler.post(() -> {
                //UI Thread work here
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            });
        });
    }
}
