package com.arafa.mohamed.whitedental.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.arafa.mohamed.whitedental.R;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class Home extends AppCompatActivity {
    AppCompatButton btSearch,btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btSearch=findViewById(R.id.button_search);
        btRegister=findViewById(R.id.button_register);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#184967"));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        btRegister.setOnClickListener(v -> startActivity(new Intent(Home.this, PatientDetails.class)));

        btSearch.setOnClickListener(v -> startActivity(new Intent(Home.this, ViewPatientData.class)));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home.this, Login.class));
            finish();
        }
        else if (item.getItemId() == R.id.action_doctor){
            Intent intent=new Intent(Home.this, AddDoctor.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}