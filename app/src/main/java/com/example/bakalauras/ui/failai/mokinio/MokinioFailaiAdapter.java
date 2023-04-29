package com.example.bakalauras.ui.failai.mokinio;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.ui.korepetitorius.AtsiliepimasCardHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.Atsiliepimas;
import Model.MokinioFailai;

public class MokinioFailaiAdapter extends RecyclerView.Adapter<MokinioFailaiCardHolder>{

    private ArrayList<MokinioFailai> list;
    private Context context;

    public MokinioFailaiAdapter(ArrayList<MokinioFailai> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MokinioFailaiCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_failas_mokiniui, parent, false);

        return new MokinioFailaiCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MokinioFailaiCardHolder holder, int position) {
        MokinioFailai sarasas = list.get(position);

        MokinioFailaiCardHolder.vardas.setText("Korepetitorius: " + sarasas.getVardas());
        MokinioFailaiCardHolder.laikas.setText(sarasas.getLaikas());
        MokinioFailaiCardHolder.pavadinimas.setText(sarasas.getPavadinimas());
        MokinioFailaiCardHolder.failoPav.setText(sarasas.getFailas());

        MokinioFailaiCardHolder.atsisiusti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadFile().execute("http://192.168.0.101/PHPscriptai/" + sarasas.getFailas());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class DownloadFile extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String fileUrl = params[0];
            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.connect();

                InputStream inputStream = conn.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), fileName));

                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(context, "Failas atsisiųstas sėkmingai!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Nepavyko atsisiųsti failo.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
