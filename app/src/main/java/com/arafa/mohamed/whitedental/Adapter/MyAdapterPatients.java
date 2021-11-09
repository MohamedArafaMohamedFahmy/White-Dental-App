package com.arafa.mohamed.whitedental.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.arafa.mohamed.whitedental.Model.Controller;
import com.arafa.mohamed.whitedental.Model.ImagesData;
import com.arafa.mohamed.whitedental.Model.RetrievePatientsData;
import com.arafa.mohamed.whitedental.activities.PatientDetails;
import com.arafa.mohamed.whitedental.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyAdapterPatients extends RecyclerView.Adapter<MyAdapterPatients.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<RetrievePatientsData> downloadData, dataListFilter;
    ArrayList<ImagesData> downloadImage;
    DatabaseReference databaseReference;
    ImagesData imgURL;
    View parentLayout;
    String result = null;

    public MyAdapterPatients(Context ct,ArrayList<RetrievePatientsData> downloadData,View parentLayout){
        context=ct;
        this.downloadData = downloadData;
        this.parentLayout=parentLayout;
        dataListFilter = new ArrayList<>(downloadData);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.list_of_patients,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyAdapterPatients.MyViewHolder holder, int position) {
         databaseReference= FirebaseDatabase.getInstance().getReference();
         downloadImage=new ArrayList<>();
         holder.tvPatientName.setText(downloadData.get(position).getPatientName());
         holder.tvPhoneNumber.setText(downloadData.get(position).getPhoneNumber());

         databaseReference.child("ImagesPatients").child(downloadData.get(position).getIdPatient()).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                     imgURL =postSnapshot.getValue(ImagesData.class);
                     downloadImage.add(imgURL);

                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

         holder.btDelete.setOnClickListener(v -> {

             Snackbar snackbar = Snackbar
                     .make(parentLayout, "Confirm delete?", Snackbar.LENGTH_LONG)
                     .setAction("YES", view -> {
                         deleteData(downloadData.get(position).getIdPatient());
                                 databaseReference.child("VisitsPatient").child(downloadData.get(position).getIdPatient()).removeValue();

                                 for(int i=0;i<downloadImage.size();i++) {
                                         StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(downloadImage.get(i).getImgURl());
                                     photoRef.delete().addOnCompleteListener(task -> {

                                     });
                                 }

                         databaseReference.child("ImagesPatients").child(downloadData.get(position).getIdPatient()).removeValue();

                         databaseReference.child("PatientData").child(downloadData.get(position).getSelectNameDoctor()).child(downloadData.get(position).getIdPatient()).removeValue().addOnSuccessListener(unused -> Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show());

                             });
             snackbar.show();

         });

        holder.btDetails.setOnClickListener(v -> {
            Intent intentDetails=new Intent(context, PatientDetails.class);
            intentDetails.putExtra("patientData",downloadData.get(position));
            context.startActivity(intentDetails);
        });

    }

    @Override
    public int getItemCount() {
        return downloadData.size();
    }

    @Override
    public Filter getFilter() {
        return patientFilter;
    }

    private Filter patientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<RetrievePatientsData> filteredList = new ArrayList<>();
                 if (constraint == null || constraint.length() == 0) {
                     filteredList.addAll(dataListFilter);
                 } else {
                     String filterPattern = constraint.toString().toLowerCase().trim();

                     for (RetrievePatientsData item : dataListFilter) {
                         if (item.getPatientName().toLowerCase().contains(filterPattern)) {
                             filteredList.add(item);
                         }
                         if (item.getPhoneNumber().contains(filterPattern)) {
                             filteredList.add(item);
                         }
                     }
                 }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            downloadData.clear();
            downloadData.addAll((Collection<? extends RetrievePatientsData>) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder{
        AppCompatTextView tvPatientName,tvPhoneNumber;
        AppCompatButton btDelete,btDetails;
        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            tvPatientName=itemView.findViewById(R.id.text_name_patient);
            tvPhoneNumber=itemView.findViewById(R.id.text_phone_number);
            btDelete=itemView.findViewById(R.id.button_delete);
            btDetails=itemView.findViewById(R.id.button_details);

        }
    }

    public void deleteData(String retrieveIDPatient) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            //Background work here

            JSONObject jsonObject = Controller.deleteData(parentLayout, retrieveIDPatient);

            try {
                if (jsonObject != null) {
                    result = jsonObject.getString("result");
                }
            } catch (JSONException je) {
                Toast.makeText(context, ""+je.getMessage(), Toast.LENGTH_SHORT).show();
            }

            handler.post(() -> {
                //UI Thread work here
                Toast.makeText(context,""+ result, Toast.LENGTH_LONG).show();
            });
        });

    }

}

