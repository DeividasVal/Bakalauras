package com.example.bakalauras.ui.uzklausos.mokinioKorepetitoriai;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import com.example.bakalauras.popup_atsiliepimas;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.ui.zinutes.susirasyti;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.MokiniuiPatvirtintasKorepetitoriusKortele;

public class MokiniuiPatvirtintiKorepetitoriaiCardAdapter extends RecyclerView.Adapter<MokiniuiPatvirtintiKorepetitoriaiCardHolder>{

    private ArrayList<MokiniuiPatvirtintasKorepetitoriusKortele> list;
    private Context context;
    private Dialog popup;

    public MokiniuiPatvirtintiKorepetitoriaiCardAdapter(ArrayList<MokiniuiPatvirtintasKorepetitoriusKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MokiniuiPatvirtintiKorepetitoriaiCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mokinio_korepetitorius_item, parent, false);

        return new MokiniuiPatvirtintiKorepetitoriaiCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MokiniuiPatvirtintiKorepetitoriaiCardHolder holder, @SuppressLint("RecyclerView") int position) {
        MokiniuiPatvirtintasKorepetitoriusKortele sarasas = list.get(position);

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.korepetitoriausVardas.setText(sarasas.getVardasKorepetitoriaus());
        MokiniuiPatvirtintiKorepetitoriaiCardHolder.adresas.setText(sarasas.getAdresas());
        MokiniuiPatvirtintiKorepetitoriaiCardHolder.kaina.setText(sarasas.getKaina() + " Eur/val.");
        MokiniuiPatvirtintiKorepetitoriaiCardHolder.dalykai.setText("Dalykai: " + sarasas.getDalykai());
        popup = new Dialog(context);

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.atsaukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasalintiKorepetitoriuTask task = new PasalintiKorepetitoriuTask(prisijungti.currentMokinys.getId());
                task.execute();
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        });

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.parasyti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, susirasyti.class);
                intent.putExtra("dabartinis_id", prisijungti.currentMokinys.getId());
                intent.putExtra("gavejas_id", sarasas.getKorepetitoriausId());
                context.startActivity(intent);
            }
        });

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.ivertinti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, popup_atsiliepimas.class);
                intent.putExtra("mokinio_id", prisijungti.currentMokinys.getId());
                intent.putExtra("profilio_id", sarasas.getProfiliausId());
                intent.putExtra("korepetitoriaus_id", sarasas.getKorepetitoriausId());
                context.startActivity(intent);
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

    private class PasalintiKorepetitoriuTask extends AsyncTask<String, Void, String> {

        private int mokinioId;

        public PasalintiKorepetitoriuTask(int mokinioId) {
            this.mokinioId = mokinioId;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.0.103/PHPscriptai/pasalintiUzklausaMokinys.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data = "mokinio_id=" + mokinioId;
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
