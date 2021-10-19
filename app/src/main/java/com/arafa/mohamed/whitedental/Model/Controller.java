package com.arafa.mohamed.whitedental.Model;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.content.ContentValues.TAG;

public class Controller {

    public static okhttp3.Response response;

    public static final String URL="https://script.google.com/macros/s/AKfycbzE_4p-Ql6SMaGsbjcakggpjqS9Z9f_VTQa9BMe-lzLItino2_p-Er-UVCax2jUfPUh/exec?action";

    public static JSONObject updateData(View parentLayout, String retrieveID, String patientName, String age, String phoneNumber, String address, String job, String notes , String chronicDiseases) {
        try {
            OkHttpClient client = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(URL+"=update&idPatient="+retrieveID+"&patientName="+patientName+"&age="+age+"&phoneNumber="+phoneNumber+"&address="+address+"&job="+job+"&notes="+notes+"&chronicDiseases="+chronicDiseases)
                    .build();
            response = client.newCall(request).execute();
            assert response.body() != null;
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Snackbar.make(parentLayout,""+e.getMessage(),Snackbar.LENGTH_LONG).show();

        }
        return null;
    }

    public static JSONObject deleteData(View parentLayout , String idPatient) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URL+"=delete&idPatient="+idPatient)
                    .build();
            response = client.newCall(request).execute();

            assert response.body() != null;
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            Snackbar.make(parentLayout,""+e.getMessage(),Snackbar.LENGTH_LONG).show();
        }
        return null;
    }

    public static JSONObject readAllData() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URL+"=readAll")
                    .build();
            response = client.newCall(request).execute();
            assert response.body() != null;
            return new JSONObject(response.body().string());
        } catch (@NonNull IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

}
