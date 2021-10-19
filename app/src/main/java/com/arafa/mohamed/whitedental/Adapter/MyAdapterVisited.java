package com.arafa.mohamed.whitedental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.arafa.mohamed.whitedental.Model.RetrievePatientsData;
import com.arafa.mohamed.whitedental.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class MyAdapterVisited extends RecyclerView.Adapter<MyAdapterVisited.MyViewHolder> {
        Context context;
        ArrayList<RetrievePatientsData> downloadData;
        DatabaseReference databaseReference;
        View parentLayout;
        String series;

public MyAdapterVisited(Context ct,ArrayList<RetrievePatientsData> downloadData,View parentLayout,String series){
        context=ct;
        this.downloadData=downloadData;
        this.parentLayout=parentLayout;
        this.series=series;
        }


@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.list_of_visits,parent,false);
        return new MyViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull  MyAdapterVisited.MyViewHolder holder, int position) {
        databaseReference= FirebaseDatabase.getInstance().getReference();

        holder.tvVisitDate.setText(downloadData.get(position).getVisitDate());
        holder.tvAmount.setText(downloadData.get(position).getAmount());
        holder.tvVisitNumber.setText(downloadData.get(position).getVisitNumber());
        holder.tvOperation.setText(downloadData.get(position).getOperation());

        holder.relativeViewVisits.setOnClickListener(v -> {
            Snackbar snackbar = Snackbar
                    .make(parentLayout, "Confirm delete?", Snackbar.LENGTH_LONG)
                    .setAction("YES", view -> databaseReference.child("VisitsPatient").child(series).child(downloadData.get(position).getVisitNumber())
                            .removeValue().addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();
                                }

                            }));

            snackbar.show();

        });
    }

    @Override
    public int getItemCount() {

        return downloadData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvVisitDate, tvAmount,tvVisitNumber,tvOperation;
        RelativeLayout relativeViewVisits;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVisitNumber=itemView.findViewById(R.id.text_visit_number);
            tvVisitDate = itemView.findViewById(R.id.text_visit_date);
            tvAmount = itemView.findViewById(R.id.text_amount);
            tvOperation=itemView.findViewById(R.id.text_operation);
            relativeViewVisits=itemView.findViewById(R.id.relative_view_visits);
        }
    }
}
