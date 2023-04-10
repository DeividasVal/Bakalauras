package com.example.bakalauras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.bakalauras.ui.korepetitorius.Korepetitorius_profilis;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.Mokinys;

public class recyclerViewPaspaustasKorepetitorius extends AppCompatActivity {

    private TextView vardasText, adresasText, mokymoBudasText, bioText, istaigaText, dalykasText, dalykaiText, kainaText;
    private String adresas, miestas, tipas, val, bio, istaiga, dalykaiIst, dalykaiJoined;
    private TableLayout uzpildyti;
    private boolean[][] prieinamumas;
    private Button susisiekti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_paspaustas_korepetitorius);

        Intent intent = getIntent();
        int korepetitorius_id = (int) intent.getSerializableExtra("korepetitorius_id");
        String vardas = (String) intent.getSerializableExtra("korepetitorius_vardas");
        susisiekti = findViewById(R.id.susisiektiButton);
        if (prisijungti.currentKorepetitorius != null)
        {
            susisiekti.setVisibility(View.GONE);
        }

        susisiekti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        vardasText = findViewById(R.id.vardasPavardeisDBActivity);
        adresasText = findViewById(R.id.adresasIsDBActivity);
        mokymoBudasText = findViewById(R.id.mokymoBudasIsDBActivity);
        bioText = findViewById(R.id.bioIsDBActivity);
        istaigaText = findViewById(R.id.istaigaIsDBActivity);
        dalykasText = findViewById(R.id.dalykasIsDBActivity);
        dalykaiText = findViewById(R.id.dalykaiIsDBActivity);
        kainaText = findViewById(R.id.kainaIsDBActivity);
        uzpildyti = findViewById(R.id.lentelePasirinkimuActivity);


        UzpildytiProfili task = new UzpildytiProfili(korepetitorius_id, vardas);
        task.execute();
    }

    private class UzpildytiProfili extends AsyncTask<Void, Void, Void> {

        private int korepetitoriaus_id;
        private String vardas;

        public UzpildytiProfili(int korepetitoriaus_id, String vardas) {
            this.korepetitoriaus_id = korepetitoriaus_id;
            this.vardas = vardas;
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
            vardasText.setText(vardas);
            dalykaiText.setText("Dalykai: " + dalykaiJoined);
            adresasText.setText(adresas + ", " + miestas);
            if (Integer.parseInt(tipas) == 1)
            {
                mokymoBudasText.setText("Mokymo tipas: Gyvai");
            }
            else if (Integer.parseInt(tipas) == 2)
            {
                mokymoBudasText.setText("Mokymo tipas: Nuotoliniu");
            }
            else
            {
                mokymoBudasText.setText("Mokymo tipas: Gyvai ir nuotoliniu");
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