package com.example.bakalauras.ui.korepetitorius.sarasas;

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
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakalauras.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.RangeSlider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.KorepetitoriausKortele;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link korepetitoriu_sarasas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class korepetitoriu_sarasas extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<KorepetitoriausKortele> arrayList;
    private korepetitoriusCardAdapter adapter;
    private ArrayList<KorepetitoriausKortele> originalList;
    private TextView ieskoti, nera;
    private String radioButtonValue = "";
    private Float minValue, maxValue, minValue2, maxValue2;
    private String selectedItem = "";
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
                showFilterDialog();
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
        nera = v.findViewById(R.id.neraKorepetitoriuSarase);

        originalList = new ArrayList<>();
        arrayList = new ArrayList<KorepetitoriausKortele>();
        adapter = new korepetitoriusCardAdapter(arrayList, getContext());

        GautiProfilioDuomenis task = new GautiProfilioDuomenis();
        task.execute();

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

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            selectedItem = bundle.getString("dalykas");
            ieskoti.setText(selectedItem);
        }

        return v;
    }

    private void filterData(String text) {
        ArrayList<KorepetitoriausKortele> filteredList = new ArrayList<>();
        for (KorepetitoriausKortele item : originalList) {
            if (item.getDalykai().toLowerCase().contains(text.toLowerCase())
                    || item.getVardas().toLowerCase().contains(text.toLowerCase())
                    || item.getMokymoBudas().toLowerCase().contains(text.toLowerCase())
                    || item.getKaina().toLowerCase().contains(text.toLowerCase())
                    || String.valueOf(item.getIvertinimas()).toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    private class GautiProfilioDuomenis extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://192.168.0.103/PHPscriptai/gautiKorepetitoriausKorteleiDuomenis.php");
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
                        URL url2 = new URL("http://192.168.0.103/PHPscriptai/gautiVidurkiKortelei.php?korepetitoriaus_id="+id);
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
                        arrayList.add(new KorepetitoriausKortele(vardas, kaina, dalykaiJoined, id, average,"Gyvai", profilioId));
                        average = 0.0;
                    }
                    else if (mokymoBudas == 2)
                    {
                        arrayList.add(new KorepetitoriausKortele(vardas, kaina, dalykaiJoined, id, average,"Nuotolinis", profilioId));
                        average = 0.0;
                    }
                    else
                    {
                        arrayList.add(new KorepetitoriausKortele(vardas, kaina, dalykaiJoined, id, average,"Gyvai ir nuotoliniu", profilioId));
                        average = 0.0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            originalList = new ArrayList<>(arrayList);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (arrayList.isEmpty()) {
                nera.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                nera.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new korepetitoriusCardAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }
            if (selectedItem != null && !selectedItem.isEmpty()) {
                ieskoti.setText(selectedItem);
                filterData(selectedItem);
            }
        }
    }

    private void showFilterDialog() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.filter_window, null);
        Button applyButton = bottomSheetView.findViewById(R.id.patvirtintiFiltra);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup radioGroup = bottomSheetView.findViewById(R.id.MokymoBūdasRadioGroup);
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Pasirinkite mokymosi tipą!", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                } else {
                    RangeSlider rangeSlider = bottomSheetView.findViewById(R.id.rangas);
                    RangeSlider rangeSlider2 = bottomSheetView.findViewById(R.id.rangasIvertinimas);
                    Spinner spinner = bottomSheetDialog.findViewById(R.id.spinnerDalykaiFiltras);

                    minValue = rangeSlider.getValues().get(0);
                    maxValue = rangeSlider.getValues().get(1);

                    minValue2 = rangeSlider2.getValues().get(0);
                    maxValue2 = rangeSlider2.getValues().get(1);
                    String dalykasPasirinktas = spinner.getSelectedItem().toString();

                    int radioButtonId = radioGroup.getCheckedRadioButtonId();
                    if (radioButtonId != -1) {
                        RadioButton radioButton = bottomSheetView.findViewById(radioButtonId);
                        radioButtonValue = radioButton.getText().toString();
                    }

                    ArrayList<KorepetitoriausKortele> filteredList = new ArrayList<>();
                    for (KorepetitoriausKortele item : arrayList) {
                        if (Integer.parseInt(item.getKaina()) >= Math.round(minValue) && Integer.parseInt(item.getKaina()) <= Math.round(maxValue) && item.getMokymoBudas().equals(radioButtonValue) && item.getDalykai().contains(dalykasPasirinktas) && item.getIvertinimas() >= minValue2 && item.getIvertinimas() <= maxValue2) {
                            filteredList.add(item);
                        }
                    }

                    adapter.filterList(filteredList);
                    bottomSheetDialog.dismiss();
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}


