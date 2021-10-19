package com.arafa.mohamed.whitedental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.arafa.mohamed.whitedental.Model.RetrievePatientsData;
import com.arafa.mohamed.whitedental.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class MyAdapterArchives extends RecyclerView.Adapter<MyAdapterArchives.MyViewHolder>{

    Context context;
    ArrayList<RetrievePatientsData> downloadData;
    DatabaseReference databaseReference;


    public MyAdapterArchives(Context context, ArrayList<RetrievePatientsData> downloadData ){
        this.context=context;
        this.downloadData=downloadData;

    }

    @NonNull
    @Override
    public MyAdapterArchives.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_of_patients, parent, false);
        return new MyAdapterArchives.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterArchives.MyViewHolder holder, int position) {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        holder.tvPatientName.setText(downloadData.get(position).getPatientName());
        holder.tvPhoneNumber.setText(downloadData.get(position).getPhoneNumber());
    }


    @Override
    public int getItemCount () {

        return downloadData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvPatientName,tvPhoneNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName=itemView.findViewById(R.id.text_name_patient);
            tvPhoneNumber=itemView.findViewById(R.id.text_phone_number);
        }
    }
}
