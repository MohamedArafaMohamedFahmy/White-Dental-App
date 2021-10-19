package com.arafa.mohamed.whitedental.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.arafa.mohamed.whitedental.activities.FullViewImage;
import com.arafa.mohamed.whitedental.Model.ImagesData;
import com.arafa.mohamed.whitedental.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MyAdapterImages extends RecyclerView.Adapter<MyAdapterImages.MyViewHolder> {
    Context context;
    ArrayList<ImagesData> downloadData;
    DatabaseReference databaseReference;
    String series;
    View parentLayout;

    public MyAdapterImages(Context ct,ArrayList<ImagesData> downloadData,String series,View parentLayout){
        context=ct;
        this.downloadData=downloadData;
        this.series=series;
        this.parentLayout=parentLayout;
    }


    @NonNull
    @Override
    public MyAdapterImages.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.list_of_images,parent,false);
        return new MyAdapterImages.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyAdapterImages.MyViewHolder holder, int position) {
        databaseReference= FirebaseDatabase.getInstance().getReference();

        holder.tvRetrieveDate.setText(downloadData.get(position).getCurrentDate());
        Picasso.get().load(Uri.parse(downloadData.get(position).getImgURl())).into(holder.imgPatient);

        holder.linearViewImages.setOnClickListener(v -> {
            Intent intent=new Intent(context, FullViewImage.class);
            intent.putExtra("imgURL",downloadData.get(position).getImgURl());
            context.startActivity(intent);
        });

        holder.linearViewImages.setOnLongClickListener(v -> {

            Snackbar snackbar = Snackbar
                    .make(parentLayout, "Confirm delete?", Snackbar.LENGTH_LONG)
                    .setAction("YES", view -> {
                        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(downloadData.get(position).getImgURl());
                        photoRef.delete().addOnSuccessListener(unused -> databaseReference.child("ImagesPatients").child(series).child(downloadData.get(position).getIdImage()).removeValue().addOnSuccessListener(unused1 -> Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show()));
                    });
            snackbar.show();
            return true;
        });

    }

    @Override
    public int getItemCount() {

        return downloadData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        AppCompatTextView tvRetrieveDate;
        AppCompatImageView imgPatient;
        LinearLayout linearViewImages;

        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            tvRetrieveDate=itemView.findViewById(R.id.text_date_image);
            imgPatient=itemView.findViewById(R.id.image_patient);
            linearViewImages=itemView.findViewById(R.id.linear_view_images);


        }
    }

}
