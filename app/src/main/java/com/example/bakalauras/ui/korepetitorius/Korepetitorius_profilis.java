package com.example.bakalauras.ui.korepetitorius;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Korepetitorius_profilis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Korepetitorius_profilis extends Fragment {

    public TextView vardasText, adresasText, mokymoBudasText, bioText, istaigaText, dalykasText, dalykaiText, kainaText;
    public String adresas, miestas, tipas, val, bio, istaiga, dalykaiIst, dalykaiJoined;
    public TableLayout uzpildyti;
    public boolean[][] prieinamumas;

    public Korepetitorius_profilis() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Korepetitorius_profilis newInstance() {
        Korepetitorius_profilis fragment = new Korepetitorius_profilis();
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
        View v = inflater.inflate(R.layout.fragment_korepetitorius_profilis, container, false);

        vardasText = v.findViewById(R.id.vardasPavardeisDB);
        adresasText = v.findViewById(R.id.adresasIsDB);
        mokymoBudasText = v.findViewById(R.id.mokymoBudasIsDB);
        bioText = v.findViewById(R.id.bioIsDB);
        istaigaText = v.findViewById(R.id.istaigaIsDB);
        dalykasText = v.findViewById(R.id.dalykasIsDB);
        dalykaiText = v.findViewById(R.id.dalykaiIsDB);
        kainaText = v.findViewById(R.id.kainaIsDB);
        uzpildyti = v.findViewById(R.id.lentelePasirinkimu);

        UzpildytiProfili task = new UzpildytiProfili(prisijungti.currentKorepetitorius.getId());
        task.execute();

        return v;
    }

    private class UzpildytiProfili extends AsyncTask<Void, Void, Void> {

        private int korepetitoriaus_id;

        public UzpildytiProfili(int korepetitoriaus_id) {
            this.korepetitoriaus_id = korepetitoriaus_id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://192.168.1.150/PHPscriptai/gautiKorepetitoriusProfilisVienamLange.php?korepetitoriaus_id=" + korepetitoriaus_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output;
                while ((output = br.readLine()) != null) {
                    JSONObject obj = new JSONObject(output);
                    Log.d("response", output);
                    adresas = obj.getString("korepetitoriaus_adresas");
                    miestas = obj.getString("korepetitoriaus_miestas");
                    tipas = obj.getString("korepetitoriaus_mokymo_tipas");
                    val = obj.getString("korepetitoriaus_val");
                    bio = obj.getString("korepetitoriaus_aprasymas");
                    istaiga = obj.getString("korepetitoriaus_istaiga");
                    dalykaiIst = obj.getString("korepetitoriaus_dalykai_istaigoj");

                    JSONArray dalykaiJson = obj.getJSONArray("korepetitoriaus_dalykai");
                    ArrayList<String> dalykai = new ArrayList<String>();
                    for (int i = 0; i < dalykaiJson.length(); i++) {
                        dalykai.add(dalykaiJson.getString(i));
                    }
                    dalykaiJoined = TextUtils.join(", ", dalykai);

                    JSONArray prieinamumasJson = obj.getJSONArray("korepetitoriaus_prieinamumas");
                    prieinamumas = new boolean[prieinamumasJson.length()][prieinamumasJson.getJSONArray(0).length()];
                    for (int i = 0; i < prieinamumasJson.length(); i++) {
                        JSONArray rowJson = prieinamumasJson.getJSONArray(i);
                        for (int j = 0; j < rowJson.length(); j++) {
                            prieinamumas[i][j] = rowJson.getBoolean(j);
                        }
                    }
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Use the prieinamumas and dalykai variables to update your UI here
            // ...
            vardasText.setText(prisijungti.currentKorepetitorius.getName());
            dalykaiText.setText("Dalykai: " + dalykaiJoined);
            adresasText.setText(adresas + ", " + miestas);
            if (Integer.parseInt(tipas) == 1)
            {
                mokymoBudasText.setText("Gyvai");
            }
            else if (Integer.parseInt(tipas) == 2)
            {
                mokymoBudasText.setText("Nuotoliniu");
            }
            else
            {
                mokymoBudasText.setText("Gyvai ir nuotoliniu");
            }
            kainaText.setText("Kaina: " + val + "€/val.");
            bioText.setText(bio);
            istaigaText.setText(istaiga);
            dalykasText.setText(dalykaiIst);

            for (int i = 1; i < uzpildyti.getChildCount(); i++) {
                TableRow row = (TableRow) uzpildyti.getChildAt(i);
                for (int j = 1; j < row.getChildCount(); j++) {
                    CheckBox checkBox = (CheckBox) row.getChildAt(j);
                    if (checkBox != null) {
                        if (prieinamumas[i-1][j-1]) {
                            checkBox.setChecked(true);
                        } else {
                            checkBox.setChecked(false);
                        }
                    }
                }
            }
        }
    }
}