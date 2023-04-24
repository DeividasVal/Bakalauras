package com.example.bakalauras.ui.failai.mokinio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.ui.uzklausos.korepetitoriaus.KorepetitoriausUzklausos;
import com.example.bakalauras.ui.uzklausos.korepetitoriaus.KorepetitoriausUzklausosCardAdapter;
import com.example.bakalauras.ui.zinutes.zinutesCardAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.util.ArrayList;

import Model.KorepetitoriausUzklausaKortele;
import Model.MokinioFailai;
import Model.ZinutesKortele;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mokinio_failai#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mokinio_failai extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<MokinioFailai> arrayList;
    private MokinioFailaiAdapter adapter;
    private TextView emptyRecycler;

    public mokinio_failai() {
        // Required empty public constructor
    }

    public static mokinio_failai newInstance() {
        mokinio_failai fragment = new mokinio_failai();
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
        View v = inflater.inflate(R.layout.fragment_mokinio_failai, container, false);

        recyclerView = v.findViewById(R.id.recyclerMokinioFailai);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        emptyRecycler = v.findViewById(R.id.neraFailuMokiniui);

        arrayList = new ArrayList<MokinioFailai>();

        GautiFailusMokiniui task = new GautiFailusMokiniui(prisijungti.currentMokinys.getId());
        task.execute();

        return v;
    }

    public class GautiFailusMokiniui extends AsyncTask<Void, Void, Void> {
        private int mokinioId;

        public GautiFailusMokiniui(int mokinioId) {
            this.mokinioId = mokinioId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.0.104/PHPscriptai/gautiSarasaFailuMokiniui.php?mokinio_id=" + mokinioId);
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
                    String korepetitoriausVardas = obj.getString("pilnas_korepetitoriaus_vardas");

                    arrayList.add(new MokinioFailai(pavadinimas, laikas_issiusta, failas, korepetitoriausVardas));
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
                adapter = new MokinioFailaiAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }
}