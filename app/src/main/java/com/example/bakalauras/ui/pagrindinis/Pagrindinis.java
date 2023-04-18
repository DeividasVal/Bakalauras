package com.example.bakalauras.ui.pagrindinis;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakalauras.R;
import com.example.bakalauras.kategorijos;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.ui.korepetitorius.sarasas.korepetitoriu_sarasas;
import com.example.bakalauras.ui.korepetitorius.sarasas.korepetitoriusCardAdapter;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.KorepetitoriausKortele;
import Model.KorepetitoriusKortelePagrindinis;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Pagrindinis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pagrindinis extends Fragment {

    private TextView vardasText;
    private RecyclerView recyclerView;
    private ArrayList<KorepetitoriusKortelePagrindinis> arrayList;
    private PagrindinisCardAdapter adapter;
    private TextView ziuretiKategorijas, ziuretiKorepetitorius;

    public Pagrindinis() {
        // Required empty public constructor
    }

    public static Pagrindinis newInstance() {
        Pagrindinis fragment = new Pagrindinis();
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
        View v = inflater.inflate(R.layout.fragment_pagrindinis, container, false);

        vardasText = v.findViewById(R.id.pagrindinisLangasVartVardas);
        ziuretiKategorijas = v.findViewById(R.id.ziuretiKategorijas);
        ziuretiKorepetitorius = v.findViewById(R.id.ziuretiKorepetitorius);

        ziuretiKategorijas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Pagrindinis.this)
                        .navigate(R.id.nav_kategorija);
            }
        });

        ziuretiKorepetitorius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Pagrindinis.this)
                        .navigate(R.id.nav_ziureti);
            }
        });

        if (prisijungti.currentMokinys != null)
        {
            vardasText.setText(prisijungti.currentMokinys.getName());
        }
        else
        {
            vardasText.setText(prisijungti.currentKorepetitorius.getName());
        }

        recyclerView = v.findViewById(R.id.recyclerViewPagrindinis);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        arrayList = new ArrayList<KorepetitoriusKortelePagrindinis>();
        adapter = new PagrindinisCardAdapter(arrayList, getContext());

        GautiProfilioDuomenis task = new GautiProfilioDuomenis();
        task.execute();

        return v;
    }

    private class GautiProfilioDuomenis extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.0.104/PHPscriptai/gautiKorepetitoriausKorteleiDuomenis.php");
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
                    String kaina = obj.getString("korepetitoriaus_val");

                    JSONArray dalykaiJson = obj.getJSONArray("korepetitoriaus_dalykai");
                    ArrayList<String> dalykai = new ArrayList<String>();
                    for (int j = 0; j < dalykaiJson.length(); j++) {
                        dalykai.add(dalykaiJson.getString(j));
                    }
                    String dalykaiJoined = TextUtils.join(", ", dalykai);
                    int id = obj.getInt("korepetitoriaus_id");
                    int mokymoBudas = obj.getInt("korepetitoriaus_mokymo_tipas");

                    Double average = 0.0;
                    try {
                        URL url2 = new URL("http://192.168.0.104/PHPscriptai/gautiVidurkiKortelei.php?korepetitoriaus_id="+id);
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
                        arrayList.add(new KorepetitoriusKortelePagrindinis(vardas, kaina, dalykaiJoined, id, average,"Gyvai"));
                        average = 0.0;
                    }
                    else if (mokymoBudas == 2)
                    {
                        arrayList.add(new KorepetitoriusKortelePagrindinis(vardas, kaina, dalykaiJoined, id, average,"Nuotolinis"));
                        average = 0.0;
                    }
                    else
                    {
                        arrayList.add(new KorepetitoriusKortelePagrindinis(vardas, kaina, dalykaiJoined, id, average,"Gyvai ir nuotoliniu"));
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
            adapter = new PagrindinisCardAdapter(arrayList, getContext());
            recyclerView.setAdapter(adapter);
        }
    }
}