package com.arafa.mohamed.whitedental.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.arafa.mohamed.whitedental.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

public class Login extends AppCompatActivity {

    AppCompatTextView btForgotPassword;
    AppCompatButton btSignIn;
    TextInputEditText etEmailAddress,etPassword;
    FirebaseAuth authCases;
    String email,password;
    ProgressBar progressBar;
    RelativeLayout rlProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btForgotPassword=findViewById(R.id.textview_forgot);
        btSignIn=findViewById(R.id.button_signin);
        etEmailAddress=findViewById(R.id.editText_email);
        etPassword=findViewById(R.id.editText_password);
        authCases= FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress_bar);
        rlProgressBar=findViewById(R.id.relative_layout);

        btForgotPassword.setOnClickListener(v -> startActivity(new Intent(Login.this, ForgotPassword.class)));

        btSignIn.setOnClickListener(v -> {
            email= Objects.requireNonNull(etEmailAddress.getText()).toString();
            password= Objects.requireNonNull(etPassword.getText()).toString();

            if (!email.isEmpty() && !password.isEmpty()) {

                rlProgressBar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                authCases.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        rlProgressBar.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, Home.class));
                        finish();
                    }
                    else{
                        rlProgressBar.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, ""+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

            }

            if (email.isEmpty()){
                etEmailAddress.setError("Please enter your email");
            }

            if (password.isEmpty()){
                etPassword.setError("Please enter password");
            }
        });


    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authCases.getCurrentUser();
        if (currentUser != null) {
            updateUI();
            finish();
        }

    }

    public void updateUI() {
        startActivity(new Intent(Login.this, Home.class));

    }
}