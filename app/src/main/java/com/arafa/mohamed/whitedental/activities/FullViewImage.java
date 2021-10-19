package com.arafa.mohamed.whitedental.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.arafa.mohamed.whitedental.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class FullViewImage extends AppCompatActivity {
    PhotoView fullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view_image);
        fullImage=findViewById(R.id.photo_view);

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#184967"));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("View Image");
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        Bundle extra=getIntent().getExtras();

        Picasso.get().load(Uri.parse(extra.getString("imgURL"))).into(fullImage);

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