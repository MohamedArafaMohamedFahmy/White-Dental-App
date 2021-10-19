package com.arafa.mohamed.whitedental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.arafa.mohamed.whitedental.Model.DoctorData;
import com.arafa.mohamed.whitedental.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class MyAdapterDoctors extends RecyclerView.Adapter<MyAdapterDoctors.MyViewHolder> {
    Context context;
    ArrayList<DoctorData> downloadData;
    DatabaseReference databaseReference;


    public MyAdapterDoctors(Context context, ArrayList<DoctorData> downloadData ){
        this.context=context;
        this.downloadData=downloadData;

    }

    @NonNull
    @Override
    public MyAdapterDoctors.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_of_doctors, parent, false);
        return new MyAdapterDoctors.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterDoctors.MyViewHolder holder, int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        holder.tvRetrieveDoctorName.setText(downloadData.get(position).getDoctorName());
    }

        @Override
        public int getItemCount () {

            return downloadData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            AppCompatTextView tvRetrieveDoctorName;
            LinearLayout linearViewImages;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tvRetrieveDoctorName = itemView.findViewById(R.id.text_doctor_name);
                linearViewImages = itemView.findViewById(R.id.linear_view_doctors);

            }
        }
    }

