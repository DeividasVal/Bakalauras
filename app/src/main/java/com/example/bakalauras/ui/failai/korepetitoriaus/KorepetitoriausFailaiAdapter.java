package com.example.bakalauras.ui.failai.korepetitoriaus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.ui.failai.mokinio.MokinioFailaiAdapter;
import com.example.bakalauras.ui.failai.mokinio.MokinioFailaiCardHolder;
import com.example.bakalauras.ui.uzklausos.mokinioKorepetitoriai.MokiniuiPatvirtintiKorepetitoriaiCardAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.KorepetitoriausFailai;
import Model.MokinioFailai;

public class KorepetitoriausFailaiAdapter extends RecyclerView.Adapter<KorepetitoriausFailaiCardHolder>{

    private ArrayList<KorepetitoriausFailai> list;
    private Context context;

    public KorepetitoriausFailaiAdapter(ArrayList<KorepetitoriausFailai> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public KorepetitoriausFailaiCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_failas_korepetitoriui, parent, false);

        return new KorepetitoriausFailaiCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KorepetitoriausFailaiCardHolder holder, @SuppressLint("RecyclerView") int position) {
        KorepetitoriausFailai sarasas = list.get(position);

        KorepetitoriausFailaiCardHolder.vardas.setText("Mokiniui: "+sarasas.getVardas());
        KorepetitoriausFailaiCardHolder.laikas.setText(sarasas.getLaikas());
        KorepetitoriausFailaiCardHolder.pavadinimas.setText(sarasas.getPavadinimas());
        KorepetitoriausFailaiCardHolder.failoPav.setText(sarasas.getFailas());
        KorepetitoriausFailaiCardHolder.pasalinti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasalintiFaila task = new PasalintiFaila(sarasas.getFailoId());
                task.execute();
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class PasalintiFaila extends AsyncTask<String, Void, String> {

        private int failoId;

        public PasalintiFaila(int failoId) {
            this.failoId = failoId;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.0.103/PHPscriptai/pasalintiFaila.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data = "pam_mok_id=" + failoId;
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                reader.close();
                response = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error!";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
    }
}
