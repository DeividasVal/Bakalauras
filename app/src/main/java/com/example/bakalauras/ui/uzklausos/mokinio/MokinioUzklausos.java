package com.example.bakalauras.ui.uzklausos.mokinio;

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

import Model.MokinioUzklausaKortele;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MokinioUzklausos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MokinioUzklausos extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<MokinioUzklausaKortele> arrayList;
    private MokinioCardAdapter adapter;
    private TextView emptyRecycler;

    public MokinioUzklausos() {
        // Required empty public constructor
    }

    public static MokinioUzklausos newInstance(String param1, String param2) {
        MokinioUzklausos fragment = new MokinioUzklausos();
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
        View view = inflater.inflate(R.layout.fragment_mokinio_uzklausos, container, false);

        recyclerView = view.findViewById(R.id.recyclerMokinioUzklausos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        emptyRecycler = view.findViewById(R.id.neraUzklausuMokiniui);

        arrayList = new ArrayList<MokinioUzklausaKortele>();

        GautiUzklausosDuomenis task = new GautiUzklausosDuomenis(Prisijungti.currentMokinys.getId());
        task.execute();

        return view;
    }

    public class GautiUzklausosDuomenis extends AsyncTask<Void, Void, Void> {
        private int mokinysId;

        public GautiUzklausosDuomenis(int mokinysId) {
            this.mokinysId = mokinysId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://192.168.0.106/PHPscriptai/gautiUzklausosDuomenisMokiniui.php?mokinio_id=" + mokinysId);
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
                        int Id =  obj.getInt("id");
                        String vardasKorepetitoriaus = obj.getString("pilnas_korepetitoriaus_vardas");
                        int busena = obj.getInt("būsena");
                        String korepetitoriausNuotrauka = obj.getString("korepetitoriaus_nuotrauka");

                        arrayList.add(new MokinioUzklausaKortele(vardasKorepetitoriaus, busena, korepetitoriausNuotrauka, Id));
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
                adapter = new MokinioCardAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }
}