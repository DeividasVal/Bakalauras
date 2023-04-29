package com.example.bakalauras;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.IsimintasKorepetitoriusKortele;

public class IsimintiKorepetitoriai extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<IsimintasKorepetitoriusKortele> arrayList;
    private IsimintasKorepetitoriusAdapter adapter;
    private TextView emptyRecycler;

    public IsimintiKorepetitoriai() {
        // Required empty public constructor
    }
    public static IsimintiKorepetitoriai newInstance() {
        IsimintiKorepetitoriai fragment = new IsimintiKorepetitoriai();
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
       View v = inflater.inflate(R.layout.fragment_isiminti_korepetitoriai, container, false);

        recyclerView = v.findViewById(R.id.recyclerIsimintiKor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        emptyRecycler = v.findViewById(R.id.neraIsimintuKorepetitoriu);

        arrayList = new ArrayList<IsimintasKorepetitoriusKortele>();

        GautiIsimintusKorepetitorius task = new GautiIsimintusKorepetitorius(Prisijungti.currentMokinys.getId());
        task.execute();
       return v;
    }

    private class GautiIsimintusKorepetitorius extends AsyncTask<String, String, String> {

        private int mokinioId;

        public GautiIsimintusKorepetitorius(int mokinioId) {
            this.mokinioId = mokinioId;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.0.101/PHPscriptai/gautiIsimintusKorepetitorius.php?mokinio_id=" + mokinioId);
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
                    String kaina = obj.getString("profilio_val");
                    String korepetitoriausNuotrauka = obj.getString("korepetitoriaus_nuotrauka");

                    JSONArray dalykaiJson = obj.getJSONArray("profilio_dalykai");
                    ArrayList<String> dalykai = new ArrayList<String>();
                    for (int j = 0; j < dalykaiJson.length(); j++) {
                        dalykai.add(dalykaiJson.getString(j));
                    }
                    String dalykaiJoined = TextUtils.join(", ", dalykai);
                    int id = obj.getInt("korepetitoriaus_id");
                    int mokymoBudas = obj.getInt("profilio_mokymo_tipas");
                    int profilioId = obj.getInt("profilio_id");

                    Double average = 0.0;
                    try {
                        URL url2 = new URL("http://192.168.0.101/PHPscriptai/gautiVidurkiKortelei.php?korepetitoriaus_id="+id);
                        HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                        conn2.setRequestMethod("GET");
                        conn2.setRequestProperty("Accept", "application/json");

                        BufferedReader br2 = new BufferedReader(new InputStreamReader((conn2.getInputStream())));
                        StringBuilder sb2 = new StringBuilder();
                        String output2;
                        while ((output2 = br2.readLine()) != null) {
                            sb2.append(output2);
                        }
                        conn2.disconnect();

                        String jsonString2 = sb2.toString();
                        JSONArray jsonArray2 = new JSONArray(jsonString2);

                        for (int j = 0; j < jsonArray2.length(); j++) {
                            JSONObject obj2 = jsonArray2.getJSONObject(j);

                            if (!obj2.isNull("average_ivertinimas")) {
                                average = obj2.getDouble("average_ivertinimas");
                            } else {
                                average = 0.0;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (mokymoBudas == 1)
                    {
                        arrayList.add(new IsimintasKorepetitoriusKortele(vardas, kaina, dalykaiJoined, id, average,"Gyvai", profilioId, korepetitoriausNuotrauka));
                        average = 0.0;
                    }
                    else if (mokymoBudas == 2)
                    {
                        arrayList.add(new IsimintasKorepetitoriusKortele(vardas, kaina, dalykaiJoined, id, average,"Nuotolinis", profilioId, korepetitoriausNuotrauka));
                        average = 0.0;
                    }
                    else
                    {
                        arrayList.add(new IsimintasKorepetitoriusKortele(vardas, kaina, dalykaiJoined, id, average,"Gyvai ir nuotoliniu", profilioId, korepetitoriausNuotrauka));
                        average = 0.0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (arrayList.isEmpty()) {
                emptyRecycler.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyRecycler.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new IsimintasKorepetitoriusAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }
}