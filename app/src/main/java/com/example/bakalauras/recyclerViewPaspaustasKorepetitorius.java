package com.example.bakalauras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakalauras.ui.korepetitorius.AtsiliepimasCardAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.Atsiliepimas;

public class recyclerViewPaspaustasKorepetitorius extends AppCompatActivity {

    private TextView vardasText, adresasText, mokymoBudasText, bioText, istaigaText, dalykasText, dalykaiText, kainaText, count, vidurkis, emptyRecycler, atsiliepimuTextView;
    private String adresas, miestas, tipas, val, bio, istaiga, dalykaiIst, dalykaiJoined;
    private TableLayout uzpildyti;
    private boolean[][] prieinamumas;
    private Button susisiekti;
    private RecyclerView recyclerView;
    private ArrayList<Atsiliepimas> arrayList;
    private AtsiliepimasCardAdapter adapter;
    private Double ivertinimasVidurkis;
    private int invertinimasCount;
    private RatingBar ratingBar;

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
                new UzklausaTask().execute(prisijungti.currentMokinys.getId(), korepetitorius_id, 0);
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
        count = findViewById(R.id.atsiliepimuKiekisIsDBActivity);
        vidurkis = findViewById(R.id.vidurkisIsDBActivity);
        ratingBar = findViewById(R.id.ratingBarActivity);
        emptyRecycler = findViewById(R.id.neraAtsiliepimuActivity);
        atsiliepimuTextView = findViewById(R.id.textView21);

        recyclerView = findViewById(R.id.recyclerViewProfilisReviewsPaspaustas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        arrayList = new ArrayList<Atsiliepimas>();

        UzkrautiAtsiliepimus task2 = new UzkrautiAtsiliepimus(korepetitorius_id);
        task2.execute();

        UzpildytiProfili task = new UzpildytiProfili(korepetitorius_id, vardas);
        task.execute();
    }

    private class UzkrautiAtsiliepimus extends AsyncTask<Void, Void, Void> {

        private int korepetitoriaus_id;

        public UzkrautiAtsiliepimus(int korepetitoriaus_id) {
            this.korepetitoriaus_id = korepetitoriaus_id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.0.104/PHPscriptai/gautiAtsiliepimus.php?korepetitoriaus_id=" + korepetitoriaus_id);
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
                    int korId = obj.getInt("korepetitoriaus_id");
                    int profilioId = obj.getInt("profilio_id");
                    int mokinioId = obj.getInt("mokinio_id");
                    String atsiliepimoTekstas = obj.getString("atsiliepimo_tekstas");
                    double ivertinimas = obj.getDouble("ivertinimas");
                    String laikas = obj.getString("laikas");
                    String vardasMokinio = obj.getString("pilnas_mokinio_vardas");

                    arrayList.add(new Atsiliepimas(vardasMokinio, mokinioId, profilioId, korId, atsiliepimoTekstas, ivertinimas, laikas));

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
                adapter = new AtsiliepimasCardAdapter(arrayList, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private class UzklausaTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int mokinys_id = params[0];
            int korepetitorius_id = params[1];
            int busena = params[2];

            try {
                URL url = new URL("http://192.168.0.104/PHPscriptai/uzklausaMokytis.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data;
                data = "mokinio_id=" + mokinys_id + "&korepetitoriaus_id=" + korepetitorius_id + "&būsena=" + busena;
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

                // Read the response from the PHP script
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(recyclerViewPaspaustasKorepetitorius.this, result, Toast.LENGTH_SHORT).show();
            Log.d("uzklausa", result);
        }
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
                URL url = new URL("http://192.168.0.104/PHPscriptai/gautiKorepetitoriusProfilisVienamLange.php?korepetitoriaus_id=" + korepetitoriaus_id);
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

                    if (!obj.isNull("average_ivertinimas")) {
                        ivertinimasVidurkis = obj.getDouble("average_ivertinimas");
                    } else {
                        ivertinimasVidurkis = 0.0;
                    }

                    if (!obj.isNull("count_korepetitoriaus_id")) {
                        invertinimasCount = obj.getInt("count_korepetitoriaus_id");
                    } else {
                        invertinimasCount = 0;
                    }

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
            count.setText("("+ invertinimasCount + " atsiliepimų)");
            vidurkis.setText(ivertinimasVidurkis.toString());
            ratingBar.setNumStars(5);
            ratingBar.setIsIndicator(true);
            ratingBar.setRating(ivertinimasVidurkis.floatValue());
            kainaText.setText("Kaina: " + val + " Eur/val.");
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
