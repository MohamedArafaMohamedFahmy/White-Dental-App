package com.arafa.mohamed.whitedental.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.arafa.mohamed.whitedental.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;


public class ForgotPassword extends AppCompatActivity {
    TextInputEditText etEmailAddress;
    AppCompatButton btFindAccount;
    FirebaseAuth authCase;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmailAddress=findViewById(R.id.editText_email);
        btFindAccount=findViewById(R.id.button_findAccount);
        authCase=FirebaseAuth.getInstance();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#184967"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        btFindAccount.setOnClickListener(v -> {
            email= Objects.requireNonNull(etEmailAddress.getText()).toString();

            if (!email.isEmpty()){
                authCase.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(ForgotPassword.this, "Reset Password Successful", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(ForgotPassword.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
            }

            if (email.isEmpty()) {
                etEmailAddress.setError("Please Enter email address");
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