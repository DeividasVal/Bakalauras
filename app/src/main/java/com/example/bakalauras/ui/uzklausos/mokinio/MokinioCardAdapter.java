package com.example.bakalauras.ui.uzklausos.mokinio;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.recyclerViewPaspaustasKorepetitorius;
import com.example.bakalauras.ui.korepetitorius.sarasas.korepetitoriusCardHolder;
import com.example.bakalauras.ui.uzklausos.korepetitoriaus.KorepetitoriausUzklausosCardAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.KorepetitoriausKortele;
import Model.MokinioUzklausaKortele;

public class MokinioCardAdapter extends RecyclerView.Adapter<MokinioUzklausosCardHolder>{

    private ArrayList<MokinioUzklausaKortele> list;
    private Context context;

    public MokinioCardAdapter(ArrayList<MokinioUzklausaKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MokinioUzklausosCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_mokiniui_rodoma_uzklausa_item, parent, false);

        return new MokinioUzklausosCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MokinioUzklausosCardHolder holder, int position) {
        MokinioUzklausaKortele sarasas = list.get(position);

        MokinioUzklausosCardHolder.korepetitoriausVardas.setText("Korepetitorius: "+ sarasas.getVardasKorepetitoriaus());
        if (sarasas.getStausas() == 0)
        {
            MokinioUzklausosCardHolder.statusas.setText("Laukiama patvirtinimo.");
        }
        else if (sarasas.getStausas() == 1)
        {
            MokinioUzklausosCardHolder.statusas.setText("Užklausa patvirtinta.");
        }
        else
        {
            MokinioUzklausosCardHolder.statusas.setText("Užklausa atšaukta");
        }

        MokinioUzklausosCardHolder.atsaukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasalintiUzklausaTask task = new PasalintiUzklausaTask(prisijungti.currentMokinys.getId());
                task.execute();
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        else
        {
            return 0;
        }
    }
    private class PasalintiUzklausaTask extends AsyncTask<String, Void, String> {

        private int mokinysId;

        public PasalintiUzklausaTask(int mokinysId) {
            this.mokinysId = mokinysId;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.1.150/PHPscriptai/pasalintiUzklausaMokinys.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data = "mokinio_id=" + mokinysId;
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

                // Read the response from the PHP script
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