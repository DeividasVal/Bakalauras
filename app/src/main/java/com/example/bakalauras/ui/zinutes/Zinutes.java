package com.example.bakalauras.ui.zinutes;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakalauras.R;
import com.example.bakalauras.ui.prisijungti.Prisijungti;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.ZinutesKortele;

public class Zinutes extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ZinutesKortele> arrayList;
    private ZinutesCardAdapter adapter;
    private TextView emptyRecycler;

    public Zinutes() {
        // Required empty public constructor
    }

    public static Zinutes newInstance() {
        Zinutes fragment = new Zinutes();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_zinutes, container, false);

        recyclerView = v.findViewById(R.id.recyclerZinutesSarasas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        emptyRecycler = v.findViewById(R.id.nerazinuciuSarasas);

        arrayList = new ArrayList<ZinutesKortele>();

        if (Prisijungti.currentMokinys != null)
        {
            GautiZinuciuSarasaMokinys task = new GautiZinuciuSarasaMokinys(Prisijungti.currentMokinys.getId());
            task.execute();
        }
        else
        {
            GautiZinuciuSarasaKorepetitorius task = new GautiZinuciuSarasaKorepetitorius(Prisijungti.currentKorepetitorius.getId());
            task.execute();
        }

        return v;
    }

    public class GautiZinuciuSarasaMokinys extends AsyncTask<Void, Void, Void> {
        private int dabartinisId;

        public GautiZinuciuSarasaMokinys(int dabartinisId) {
            this.dabartinisId = dabartinisId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.0.106/PHPscriptai/gautiZinuciuSarasaMokiniui.php?gavejo_id=" + dabartinisId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                conn.disconnect();

                String jsonString = sb.toString();
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String vardas = obj.getString("pilnas_korepetitoriaus_vardas");
                    String zinutes = obj.getString("tekstas_zinutes");
                    String laikas = obj.getString("laikas_zinutes");
                    int siuntejoId = obj.getInt("siuntejo_id");
                    int gavejoId = obj.getInt("gavejo_id");
                    String korepetitoriausNuotrauka = obj.getString("korepetitoriaus_nuotrauka");

                    arrayList.add(new ZinutesKortele(vardas, zinutes, laikas, siuntejoId, gavejoId, korepetitoriausNuotrauka));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (arrayList.isEmpty()) {
                emptyRecycler.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyRecycler.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new ZinutesCardAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }

    public class GautiZinuciuSarasaKorepetitorius extends AsyncTask<Void, Void, Void> {
        private int dabartinisId;

        public GautiZinuciuSarasaKorepetitorius(int dabartinisId) {
            this.dabartinisId = dabartinisId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.0.106/PHPscriptai/gautiZinuciuSarasaKorepetitoriui.php?gavejo_id=" + dabartinisId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                conn.disconnect();

                String jsonString = sb.toString();
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String vardas = obj.getString("pilnas_mokinio_vardas");
                    String zinutes = obj.getString("tekstas_zinutes");
                    String laikas = obj.getString("laikas_zinutes");
                    int siuntejoId = obj.getInt("siuntejo_id");
                    int gavejoId = obj.getInt("gavejo_id");
                    String mokinioNuotrauka = obj.getString("mokinio_nuotrauka");

                    arrayList.add(new ZinutesKortele(vardas, zinutes, laikas, siuntejoId, gavejoId, mokinioNuotrauka));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (arrayList.isEmpty()) {
                emptyRecycler.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyRecycler.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new ZinutesCardAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }
}