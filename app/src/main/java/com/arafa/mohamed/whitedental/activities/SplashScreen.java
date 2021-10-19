package com.arafa.mohamed.whitedental.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.arafa.mohamed.whitedental.R;

public class SplashScreen extends AppCompatActivity {
    AppCompatImageView imgSplash;
    AppCompatTextView tvName;
    Animation topAnim,bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imgSplash=findViewById(R.id.image_splash);
        tvName=findViewById(R.id.text_name);


        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        imgSplash.setAnimation(topAnim);
        tvName.setAnimation(bottomAnim);

        int splash_Screen = 2000;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, Login.class));
            finish();
        }, splash_Screen);

    }
}