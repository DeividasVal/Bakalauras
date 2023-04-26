package com.example.bakalauras.ui.uzklausos.korepetitoriaus;

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
import com.example.bakalauras.ui.uzklausos.mokinio.MokinioCardAdapter;
import com.example.bakalauras.ui.uzklausos.mokinio.MokinioUzklausos;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.KorepetitoriausUzklausaKortele;
import Model.MokinioUzklausaKortele;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KorepetitoriausUzklausos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KorepetitoriausUzklausos extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<KorepetitoriausUzklausaKortele> arrayList;
    private KorepetitoriausUzklausosCardAdapter adapter;
    private TextView emptyRecycler;

    public KorepetitoriausUzklausos() {
        // Required empty public constructor
    }

    public static KorepetitoriausUzklausos newInstance() {
        KorepetitoriausUzklausos fragment = new KorepetitoriausUzklausos();
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
        View view = inflater.inflate(R.layout.fragment_korepetitoriaus_uzklausos, container, false);

        recyclerView = view.findViewById(R.id.recyclerKorepetitoriausUzklausos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        emptyRecycler = view.findViewById(R.id.neraKorepetitoriausUzklausu);

        arrayList = new ArrayList<KorepetitoriausUzklausaKortele>();

        GautiUzklausosKorepetitoriuiDuomenis task = new GautiUzklausosKorepetitoriuiDuomenis(prisijungti.currentKorepetitorius.getId());
        task.execute();

        return view;
    }

    public class GautiUzklausosKorepetitoriuiDuomenis extends AsyncTask<Void, Void, Void> {
        private int korepetitoriausId;

        public GautiUzklausosKorepetitoriuiDuomenis(int korepetitoriausId) {
            this.korepetitoriausId = korepetitoriausId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.0.103/PHPscriptai/gautiUzklausosDuomenisKorepetitoriui.php?korepetitoriaus_id=" + korepetitoriausId);
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
                    String vardasMokinio = obj.getString("pilnas_mokinio_vardas");
                    int busena = obj.getInt("būsena");

                    arrayList.add(new KorepetitoriausUzklausaKortele(vardasMokinio, busena));
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
                adapter = new KorepetitoriausUzklausosCardAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }
}