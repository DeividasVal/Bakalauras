package com.example.bakalauras.ui.uzklausos.mokinioKorepetitoriai;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.ui.uzklausos.korepetitoriausMokiniai.KorepetitoriuiPatvirtintiMokiniai;
import com.example.bakalauras.ui.uzklausos.korepetitoriausMokiniai.KorepetitoriuiPatvirtintiMokiniaiCardAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.KorepetitoriausKortele;
import Model.KorepetitoriuiPatvirtintasMokinysKortele;
import Model.MokiniuiPatvirtintasKorepetitoriusKortele;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MokiniuiPatvirtintiKorepetitoriai#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MokiniuiPatvirtintiKorepetitoriai extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<MokiniuiPatvirtintasKorepetitoriusKortele> arrayList;
    private MokiniuiPatvirtintiKorepetitoriaiCardAdapter adapter;
    private String dalykaiJoined;

    public MokiniuiPatvirtintiKorepetitoriai() {
    }

    public static MokiniuiPatvirtintiKorepetitoriai newInstance() {
        MokiniuiPatvirtintiKorepetitoriai fragment = new MokiniuiPatvirtintiKorepetitoriai();
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
        View view = inflater.inflate(R.layout.fragment_mokiniui_patvirtinti_korepetitoriai, container, false);

        recyclerView = view.findViewById(R.id.recyclerMokiniuiPatvirtintiKorepetitoriai);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        arrayList = new ArrayList<MokiniuiPatvirtintasKorepetitoriusKortele>();

        GautiPatvirtintusKorepetitoriusTask task = new GautiPatvirtintusKorepetitoriusTask(prisijungti.currentMokinys.getId());
        task.execute();

        return view;
    }

    public class GautiPatvirtintusKorepetitoriusTask extends AsyncTask<Void, Void, Void> {
        private int mokinioId;

        public GautiPatvirtintusKorepetitoriusTask(int mokinioId) {
            this.mokinioId = mokinioId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.0.104/PHPscriptai/gautiMokiniuiPatvirtintusKorepetitorius.php?mokinio_id=" + mokinioId);
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
                    int kaina = obj.getInt("korepetitoriaus_val");

                    JSONArray dalykaiJson = obj.getJSONArray("korepetitoriaus_dalykai");
                    ArrayList<String> dalykai = new ArrayList<String>();
                    for (int j = 0; j < dalykaiJson.length(); j++) {
                        dalykai.add(dalykaiJson.getString(j));
                    }
                    dalykaiJoined = TextUtils.join(", ", dalykai);
                    String adresas = obj.getString("korepetitoriaus_adresas");
                    int id = obj.getInt("korepetitoriaus_id");
                    int idProfiliaus = obj.getInt("profilio_id");

                    arrayList.add(new MokiniuiPatvirtintasKorepetitoriusKortele(kaina, vardas, dalykaiJoined, adresas, id, idProfiliaus));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter = new MokiniuiPatvirtintiKorepetitoriaiCardAdapter(arrayList, getContext());
            recyclerView.setAdapter(adapter);
        }
    }
}