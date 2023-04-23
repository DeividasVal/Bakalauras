package com.example.bakalauras.ui.uzklausos.korepetitoriausMokiniai;

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
import com.example.bakalauras.ui.uzklausos.korepetitoriaus.KorepetitoriausUzklausos;
import com.example.bakalauras.ui.uzklausos.korepetitoriaus.KorepetitoriausUzklausosCardAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.KorepetitoriausUzklausaKortele;
import Model.KorepetitoriuiPatvirtintasMokinysKortele;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KorepetitoriuiPatvirtintiMokiniai#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KorepetitoriuiPatvirtintiMokiniai extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<KorepetitoriuiPatvirtintasMokinysKortele> arrayList;
    private KorepetitoriuiPatvirtintiMokiniaiCardAdapter adapter;
    private TextView emptyRecycler;

    public KorepetitoriuiPatvirtintiMokiniai() {
        // Required empty public constructor
    }

    public static KorepetitoriuiPatvirtintiMokiniai newInstance() {
        KorepetitoriuiPatvirtintiMokiniai fragment = new KorepetitoriuiPatvirtintiMokiniai();
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
        View view = inflater.inflate(R.layout.fragment_korepetitoriui_patvirtinti_mokiniai, container, false);

        recyclerView = view.findViewById(R.id.recyclerKorepetitoriuiPatvirtintiMokiniai);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        emptyRecycler = view.findViewById(R.id.neraMokiniuKorepetitoriui);

        arrayList = new ArrayList<KorepetitoriuiPatvirtintasMokinysKortele>();

        GautiPatvirtintusMokiniusTask task = new GautiPatvirtintusMokiniusTask(prisijungti.currentKorepetitorius.getId());
        task.execute();

        return view;
    }

    public class GautiPatvirtintusMokiniusTask extends AsyncTask<Void, Void, Void> {
        private int korepetitoriausId;

        public GautiPatvirtintusMokiniusTask(int korepetitoriausId) {
            this.korepetitoriausId = korepetitoriausId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.0.104/PHPscriptai/gautiKorepetitoriuiPatvirtintusMokinius.php?korepetitoriaus_id=" + korepetitoriausId);
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
                    int mokinioId = obj.getInt("mokinio_id");

                    arrayList.add(new KorepetitoriuiPatvirtintasMokinysKortele(vardasMokinio, mokinioId));
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
                adapter = new KorepetitoriuiPatvirtintiMokiniaiCardAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }
}