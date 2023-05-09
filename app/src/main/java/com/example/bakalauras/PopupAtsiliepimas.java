package com.example.bakalauras;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PopupAtsiliepimas extends AppCompatActivity {

    private int mokinio_id, korepetitoriaus_id, profilio_id;
    private Button skelbti;
    private Button uzdaryti;
    private RatingBar ivertinimas;
    private EditText aprasymas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_atsiliepimas);

        Intent intent = getIntent();
        mokinio_id = (int) intent.getSerializableExtra("mokinio_id");
        korepetitoriaus_id = (int) intent.getSerializableExtra("korepetitoriaus_id");
        profilio_id = (int) intent.getSerializableExtra("profilio_id");

        skelbti = findViewById(R.id.skelbtiAtsiliepima);
        uzdaryti = findViewById(R.id.uzdarytiAtsiliepimoLanga);
        ivertinimas = findViewById(R.id.ratingBarPopup);
        aprasymas = findViewById(R.id.aprasymasPopup);

        skelbti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aprasymas.getText() != null && aprasymas.getText().length() > 0) {
                    SkelbtiAtsiliepima task = new SkelbtiAtsiliepima(mokinio_id, korepetitoriaus_id, profilio_id, ivertinimas.getRating(), aprasymas);
                    task.execute();
                    finish();
                } else {
                    Toast.makeText(PopupAtsiliepimas.this, "Parašykite atsiliepimą!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        uzdaryti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class SkelbtiAtsiliepima extends AsyncTask<Void, Void, String> {
        private int mokinioId;
        private int korepetitoriausId;
        private int profilioId;
        private float ivertinimas;
        private EditText aprasymas;

        public SkelbtiAtsiliepima(int mokinioId, int korepetitoriausId, int profilioId, float ivertinimas, EditText aprasymas) {
            this.mokinioId = mokinioId;
            this.korepetitoriausId = korepetitoriausId;
            this.profilioId = profilioId;
            this.ivertinimas = ivertinimas;
            this.aprasymas = aprasymas;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            String aprasymasGet = String.valueOf(aprasymas.getText());
            try {
                long currentTimeMillis = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedTime = sdf.format(new Date(currentTimeMillis));
                URL url = new URL("http://192.168.0.108/PHPscriptai/skelbtiAtsiliepima.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                String data = "mokinio_id=" + mokinioId + "&korepetitoriaus_id=" + korepetitoriausId + "&profilio_id=" + profilioId + "&ivertinimas=" + ivertinimas + "&atsiliepimo_tekstas=" + aprasymasGet + "&laikas=" + formattedTime;
                writer.write(data);
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                reader.close();
                response = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error!";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(PopupAtsiliepimas.this, response, Toast.LENGTH_SHORT).show();
        }
    }
}