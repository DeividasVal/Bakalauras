package com.example.bakalauras;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mokinio_profilis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mokinio_profilis extends Fragment {

    private TextView vardasText, pastasText, vartotojoVardasText;
    private String vardas, pastas, vartotojoVardas;


    public mokinio_profilis() {
        // Required empty public constructor
    }

    public static mokinio_profilis newInstance() {
        mokinio_profilis fragment = new mokinio_profilis();
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
        View v = inflater.inflate(R.layout.fragment_mokinio_profilis, container, false);

        vardasText = v.findViewById(R.id.vardasMokinys);
        pastasText = v.findViewById(R.id.emailMokinys);
        vartotojoVardasText = v.findViewById(R.id.vartotojoMokinys);

        UzpildytiProfiliMokinio task = new UzpildytiProfiliMokinio(prisijungti.currentMokinys.getId());
        task.execute();

        return v;
    }

    private class UzpildytiProfiliMokinio extends AsyncTask<Void, Void, Void> {

        private int mokinio_id;

        public UzpildytiProfiliMokinio(int mokinio_id) {
            this.mokinio_id = mokinio_id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://192.168.0.103/PHPscriptai/gautiMokinioProfilisVienameLange.php?mokinio_id=" + mokinio_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output;
                while ((output = br.readLine()) != null) {
                    JSONObject obj = new JSONObject(output);
                    Log.d("response", output);
                    pastas = obj.getString("mokinio_el_pastas");
                    vartotojoVardas = obj.getString("mokinio_vartotojo_vardas");
                    vardas = obj.getString("pilnas_mokinio_vardas");
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
            pastasText.setText(pastas);
            vartotojoVardasText.setText(vartotojoVardas);
        }
    }
}