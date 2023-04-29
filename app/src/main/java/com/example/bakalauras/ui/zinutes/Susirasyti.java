package com.example.bakalauras.ui.zinutes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.bakalauras.R;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Model.Zinutes;

public class Susirasyti extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Zinutes> arrayList;
    private SusirasytiCardAdapter adapter;
    private int dabartinis_id, gavejo_id;
    private EditText editTextChatbox;
    private Button buttonSend;
    private TextView emptyRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_susirasyti);

        recyclerView = findViewById(R.id.zinuciuRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        buttonSend = findViewById(R.id.button_send);
        editTextChatbox= findViewById(R.id.edittext_chatbox);
        emptyRecycler = findViewById(R.id.neraZinuciuActivity);

        arrayList = new ArrayList<Zinutes>();

        Intent intent = getIntent();
        dabartinis_id = (int) intent.getSerializableExtra("dabartinis_id");
        gavejo_id = (int) intent.getSerializableExtra("gavejas_id");

        adapter = new SusirasytiCardAdapter(arrayList, dabartinis_id, this);
        recyclerView.setAdapter(adapter);

        GautiZinuciuDuomenis task = new GautiZinuciuDuomenis(dabartinis_id, gavejo_id);
        task.execute();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextChatbox.getText().toString().trim();
                long currentTimeMillis = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedTime = sdf.format(new Date(currentTimeMillis));
                if (!message.isEmpty()) {
                    Zinutes zinute = new Zinutes(gavejo_id, dabartinis_id, formattedTime, message);
                    arrayList.add(zinute);
                    adapter.notifyItemInserted(arrayList.size() - 1);
                    recyclerView.smoothScrollToPosition(arrayList.size() - 1);
                    editTextChatbox.setText("");
                    new IssaugotiZinute(adapter).execute(Integer.toString(dabartinis_id), Integer.toString(gavejo_id), message, formattedTime);
                    updateEmptyRecycler();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Įveskite žinutę!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateEmptyRecycler() {
        if (arrayList.isEmpty()) {
            emptyRecycler.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyRecycler.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private class IssaugotiZinute extends AsyncTask<String, Void, String> {

        private SusirasytiCardAdapter adapter;

        public IssaugotiZinute(SusirasytiCardAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        protected String doInBackground(String... params) {
            int dabartinis_id = Integer.parseInt(params[0]);
            int gavejo_id = Integer.parseInt(params[1]);
            String zinute = params[2];
            String laikas = params[3];
            URL url;
            try {
                url = new URL("http://192.168.0.101/PHPscriptai/iterptiZinute.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data;
                data = "gavejo_id=" + dabartinis_id + "&siuntejo_id=" + gavejo_id + "&tekstas_zinutes=" + zinute + "&laikas_zinutes=" + laikas;
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

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
            adapter.notifyDataSetChanged();
        }
    }

    public class GautiZinuciuDuomenis extends AsyncTask<Void, Void, Void> {
        private int prisijungesVartotojas;
        private int gavejoId;

        public GautiZinuciuDuomenis(int prisijungesVartotojas, int gavejoId) {
            this.prisijungesVartotojas = prisijungesVartotojas;
            this.gavejoId = gavejoId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.0.101/PHPscriptai/gautiZinutes.php?" +
                        "gavejo_id=" + gavejoId + "&dabartinis_id=" + prisijungesVartotojas);
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
                    int siuntejo_id = obj.getInt("siuntejo_id");
                    int gavejo_id = obj.getInt("gavejo_id");
                    String laikas_zinutes = obj.getString("laikas_zinutes");
                    String tekstas_zinutes = obj.getString("tekstas_zinutes");

                    arrayList.add(new Zinutes(siuntejo_id, gavejo_id, laikas_zinutes, tekstas_zinutes));
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
                adapter = new SusirasytiCardAdapter(arrayList, dabartinis_id, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }
        }
    }
}