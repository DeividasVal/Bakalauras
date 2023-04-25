package com.example.bakalauras.ui.failai.korepetitoriaus;

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
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.ui.failai.mokinio.MokinioFailaiAdapter;
import com.example.bakalauras.ui.failai.mokinio.mokinio_failai;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.KorepetitoriausFailai;
import Model.MokinioFailai;

public class korepetitoriaus_failai extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<KorepetitoriausFailai> arrayList;
    private KorepetitoriausFailaiAdapter adapter;
    private TextView emptyRecycler;

    public korepetitoriaus_failai() {
        // Required empty public constructor
    }

    public static korepetitoriaus_failai newInstance() {
        korepetitoriaus_failai fragment = new korepetitoriaus_failai();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_korepetitoriaus_failai, container, false);

        recyclerView = v.findViewById(R.id.recyclerKorepetitoriausFailai);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        emptyRecycler = v.findViewById(R.id.neraFailuKorepetitoriui);

        arrayList = new ArrayList<KorepetitoriausFailai>();

        GautiFailusKorepetitoriui task = new GautiFailusKorepetitoriui(prisijungti.currentKorepetitorius.getId());
        task.execute();

        return v;
    }

    public class GautiFailusKorepetitoriui extends AsyncTask<Void, Void, Void> {
        private int korepetitoriausId;

        public GautiFailusKorepetitoriui(int korepetitoriausId) {
            this.korepetitoriausId = korepetitoriausId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.0.105/PHPscriptai/gautiSarasaFailuKorepetitoriui.php?korepetitoriaus_id=" + korepetitoriausId);
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
                    String failas = obj.getString("failas");
                    String pavadinimas = obj.getString("pavadinimas");
                    String laikas_issiusta = obj.getString("laikas_issiusta");
                    String mokinioVardas = obj.getString("pilnas_mokinio_vardas");
                    int failoId = obj.getInt("pam_mok_id");

                    arrayList.add(new KorepetitoriausFailai(pavadinimas, laikas_issiusta, failas, mokinioVardas, failoId));
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
                adapter = new KorepetitoriausFailaiAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }
}