package com.example.bakalauras.ui.failai.mokinio;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
        File file = new File(sarasas.getFailas());
        String fileName1 = file.getName();
        MokinioFailaiCardHolder.failoPav.setText(fileName1);

        MokinioFailaiCardHolder.atsisiusti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileUrl = "http://192.168.0.106/PHPscriptai/" + sarasas.getFailas();
                String fileName = sarasas.getFailas();
                File file = new File(fileName);
                String fileName2 = file.getName();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                request.setTitle("korepetitoriu medziaga/" + fileName2);
                request.setDescription(fileName2);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "korepetitoriu medziaga/" + fileName2);

                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);
                Toast.makeText(context, "Failas atsisiųstas sėkmingai!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
