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

import com.arafa.mohamed.whitedental.Model.ImagesData;
import com.arafa.mohamed.whitedental.Adapter.MyAdapterImages;
import com.arafa.mohamed.whitedental.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ImagesPatients extends AppCompatActivity {
    RecyclerView listImages;
    ArrayList<ImagesData> downloadImage;
    ProgressBar progressBar;
    RelativeLayout rlProgressBar;
    DatabaseReference databaseReference;
    String retrieveIDPatient;
    MyAdapterImages adapterImages;
    ImagesData imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_patients);
        Bundle extra=getIntent().getExtras();
        listImages=findViewById(R.id.recyclerView_images);
        downloadImage=new ArrayList<>();
        progressBar=findViewById(R.id.progress_bar);
        rlProgressBar=findViewById(R.id.relative_layout);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        retrieveIDPatient=extra.getString("idPatient");

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#184967"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Images Patient");
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        View parentLayout=findViewById(android.R.id.content);

        databaseReference.child("ImagesPatients").child(retrieveIDPatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                downloadImage.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    imgURL =postSnapshot.getValue(ImagesData.class);
                    downloadImage.add(imgURL);

                }
                if (!downloadImage.isEmpty()){
                    adapterImages = new MyAdapterImages(ImagesPatients.this,downloadImage,retrieveIDPatient,parentLayout);
                    listImages.setAdapter(adapterImages);
                    listImages.setLayoutManager(new LinearLayoutManager(ImagesPatients.this));
                    rlProgressBar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }

                if (downloadImage.isEmpty()){
                    rlProgressBar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    downloadImage.clear();
                    adapterImages = new MyAdapterImages(ImagesPatients.this,downloadImage,retrieveIDPatient,parentLayout);
                    listImages.setAdapter(adapterImages);
                    listImages.setLayoutManager(new LinearLayoutManager(ImagesPatients.this));
                    Toast.makeText(ImagesPatients.this,"Not Images ",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImagesPatients.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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