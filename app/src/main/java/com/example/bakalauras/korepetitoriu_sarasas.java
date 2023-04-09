package com.example.bakalauras;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import Model.KorepetitoriausKortele;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link korepetitoriu_sarasas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class korepetitoriu_sarasas extends Fragment {

    public RecyclerView recyclerView;
    ArrayList<KorepetitoriausKortele> arrayList;
    korepetitoriusCardAdapter adapter;
    TextView ieskoti;

    public korepetitoriu_sarasas() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.korepetitoriu_sarasas, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filtras:
                FilterBottomSheetDialog dialog = new FilterBottomSheetDialog();
                dialog.show(getChildFragmentManager(), "FilterBottomSheetDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static korepetitoriu_sarasas newInstance() {
        korepetitoriu_sarasas fragment = new korepetitoriu_sarasas();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_korepetitoriu_sarasas, container, false);

        recyclerView = v.findViewById(R.id.recyclerKorepetitoriu);
        ieskoti = v.findViewById(R.id.ieskoti);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        ieskoti.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterData(editable.toString());
            }
        });

        arrayList = new ArrayList<KorepetitoriausKortele>();

        GautiProfilioDuomenis task = new GautiProfilioDuomenis();
        task.execute();

        return v;
    }

    private void filterData(String text){
        ArrayList<KorepetitoriausKortele> temp = new ArrayList<>();
        if(arrayList.size()>0){
            for (KorepetitoriausKortele newList : arrayList){
                if(newList.getDalykai().toLowerCase().contains(text.toLowerCase())){
                    temp.add(newList);
                }
            }
            adapter.filterList(temp);
        }
    }

    private class GautiProfilioDuomenis extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.1.150/PHPscriptai/gautiKorepetitoriausKorteleiDuomenis.php");
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
                    String dalykaiJoined = TextUtils.join(", ", dalykai);
                    int id = obj.getInt("korepetitoriaus_id");
                    int mokymoBudas = obj.getInt("korepetitoriaus_mokymo_tipas");

                    if (mokymoBudas == 1)
                    {
                        arrayList.add(new KorepetitoriausKortele(vardas, kaina, dalykaiJoined, id, "Gyvai"));
                    }
                    else if (mokymoBudas == 2)
                    {
                        arrayList.add(new KorepetitoriausKortele(vardas, kaina, dalykaiJoined, id, "Nuotolinis"));
                    }
                    else
                    {
                        arrayList.add(new KorepetitoriausKortele(vardas, kaina, dalykaiJoined, id, "Gyvai ir nuotoliniu"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            adapter = new korepetitoriusCardAdapter(arrayList, getContext());
            recyclerView.setAdapter(adapter);
        }
    }

    public static class FilterBottomSheetDialog extends BottomSheetDialogFragment {

        public Button patvirtinti;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.filter_window, container, false);

            patvirtinti = v.findViewById(R.id.patvirtintiFiltra);

            patvirtinti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            return v;
        }
    }
}


