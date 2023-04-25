package com.example.bakalauras.ui.korepetitorius.sarasas;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.recyclerViewPaspaustasKorepetitorius;
import com.example.bakalauras.ui.korepetitorius.sarasas.korepetitoriusCardHolder;
import com.example.bakalauras.ui.zinutes.susirasyti;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.KorepetitoriausKortele;

class korepetitoriusCardHolder extends RecyclerView.ViewHolder {

    public TextView vardas, kaina, dalykai, budasKortele, ivertinimas;
    public Button favorite;
    public ImageView zvaigzde;

    public korepetitoriusCardHolder(@NonNull View itemView) {
        super(itemView);

        vardas = itemView.findViewById(R.id.vardasKortele);
        kaina = itemView.findViewById(R.id.kainaKortele);
        dalykai = itemView.findViewById(R.id.dalykaiKortele);
        budasKortele = itemView.findViewById(R.id.budasKortele);
        favorite = itemView.findViewById(R.id.favoriteButton);
        ivertinimas = itemView.findViewById(R.id.atsiliepimaiVidurkisKortele);
        zvaigzde = itemView.findViewById(R.id.imageView2);
    }
}

public class korepetitoriusCardAdapter extends RecyclerView.Adapter<korepetitoriusCardHolder> {

    private ArrayList<KorepetitoriausKortele> list;
    private Context context;

    public korepetitoriusCardAdapter(ArrayList<KorepetitoriausKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public korepetitoriusCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_korepetitorius_item, parent, false);

        return new korepetitoriusCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull korepetitoriusCardHolder holder, int position) {
        KorepetitoriausKortele sarasas = list.get(position);

        if (prisijungti.currentMokinys != null)
        {
            new CheckFavoriteAsyncTask(holder, sarasas.getProfilioId()).execute();

            holder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PaziuretiArIsimintaAsyncTask(holder, sarasas.getProfilioId()).execute();
                }
            });
        }

        if (prisijungti.currentKorepetitorius != null)
        {
            holder.favorite.setVisibility(View.GONE);
        }

        holder.vardas.setText(sarasas.getVardas());
        holder.dalykai.setText("Moko: " + sarasas.getDalykai());
        holder.kaina.setText(sarasas.getKaina() + " Eur/val.");
        holder.budasKortele.setText("Mokymo tipas: " + sarasas.getMokymoBudas());

        if (sarasas.getIvertinimas() == 0.0)
        {
            holder.zvaigzde.setImageResource(R.drawable.ic_baseline_star_rate_24_gray);
            holder.ivertinimas.setText(sarasas.getIvertinimas().toString());
        }
        else
        {
            holder.ivertinimas.setText(sarasas.getIvertinimas().toString());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, recyclerViewPaspaustasKorepetitorius.class);
                intent.putExtra("korepetitorius_id", sarasas.getId());
                intent.putExtra("korepetitorius_vardas", sarasas.getVardas());
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

    public void filterList(ArrayList<KorepetitoriausKortele> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    class PaziuretiArIsimintaAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private korepetitoriusCardHolder holder;
        private int profilioId;

        public PaziuretiArIsimintaAsyncTask(korepetitoriusCardHolder holder, int profilioId) {
            this.holder = holder;
            this.profilioId = profilioId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return paziuretiArIsiminta(profilioId, prisijungti.currentMokinys.getId());
        }

        @Override
        protected void onPostExecute(Boolean isFavorited) {
            if (isFavorited) {
                new PasalintiIssaugotaKorepetitoriu().execute(profilioId, prisijungti.currentMokinys.getId());
                holder.favorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
            } else {
                new IssaugotiKorepetitoriu().execute(profilioId, prisijungti.currentMokinys.getId());
                holder.favorite.setBackgroundResource(R.drawable.ic_baseline_favorite_24_red);
            }
        }
    }

    private class IssaugotiKorepetitoriu extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int profilioId = params[0];
            int mokinioId = params[1];

            try {
                URL url = new URL("http://192.168.0.105/PHPscriptai/isimenaKorepetitoriu.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data;
                data = "profilio_id=" + profilioId + "&mokinio_id=" + mokinioId;
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    }

    private class PasalintiIssaugotaKorepetitoriu extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int profilioId = params[0];
            int mokinioId = params[1];

            try {
                URL url = new URL("http://192.168.0.105/PHPscriptai/pasalintiIsimintaKorepetitoriu.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data;
                data = "profilio_id=" + profilioId + "&mokinio_id=" + mokinioId;
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean paziuretiArIsiminta(int profilioId, int mokinioId) {
        try {
            URL url = new URL("http://192.168.0.105/PHPscriptai/arMokinysIsimineKorepetitoriu.php");;
            String requestBody = "profilio_id=" + profilioId + "&mokinio_id=" + mokinioId;

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(requestBody);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();
            reader.close();

            return response.equals("true");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private class CheckFavoriteAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private korepetitoriusCardHolder holder;
        private int profileId;

        public CheckFavoriteAsyncTask(korepetitoriusCardHolder holder, int profileId) {
            this.holder = holder;
            this.profileId = profileId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return paziuretiArIsiminta(profileId, prisijungti.currentMokinys.getId());
        }

        @Override
        protected void onPostExecute(Boolean isFavorited) {
            if (prisijungti.currentKorepetitorius != null) {
                holder.favorite.setVisibility(View.GONE);
            } else {
                if (isFavorited) {
                    holder.favorite.setBackgroundResource(R.drawable.ic_baseline_favorite_24_red);
                } else {
                    holder.favorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                }
            }
        }
    }
}

