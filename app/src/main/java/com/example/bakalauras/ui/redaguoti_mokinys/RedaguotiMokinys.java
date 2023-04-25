package com.example.bakalauras.ui.redaguoti_mokinys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bakalauras.R;
import com.example.bakalauras.mokinio_profilis;
import com.example.bakalauras.pagrindinis_langas;
import com.example.bakalauras.prisijungti;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RedaguotiMokinys#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RedaguotiMokinys extends Fragment {

    private EditText vardasText, emailText, vartotojoVardasText, slaptazodisText, patvirtintiPassText;
    private Button pakeisti;
    private String vardas, email, vartotojoVardas, slaptazodis, patvirtinti;

    public RedaguotiMokinys() {
        // Required empty public constructor
    }

    public static RedaguotiMokinys newInstance() {
        RedaguotiMokinys fragment = new RedaguotiMokinys();
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
        View v = inflater.inflate(R.layout.fragment_redaguoti_mokinys, container, false);

        vardasText = v.findViewById(R.id.vardasMokinys);
        emailText = v.findViewById(R.id.emailMokinysRedaguoti);
        vartotojoVardasText = v.findViewById(R.id.vartotojoMokinys);
        slaptazodisText = v.findViewById(R.id.slaptazodisRedaguotiMokinys);
        patvirtintiPassText = v.findViewById(R.id.slaptazodisPatvirtintiRedaguotiMokinys);
        pakeisti = v.findViewById(R.id.redaguotiMokinysButton);

        pakeisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vardas = String.valueOf(vardasText.getText());
                email = String.valueOf(emailText.getText());
                vartotojoVardas = String.valueOf(vartotojoVardasText.getText());
                slaptazodis = String.valueOf(slaptazodisText.getText());
                patvirtinti = String.valueOf(patvirtintiPassText.getText());

                if (vardas.isEmpty() || email.isEmpty() || vartotojoVardas.isEmpty() || slaptazodis.isEmpty() || patvirtinti.isEmpty()) {
                    Toast.makeText(getContext(), "uzpildykite visus laukus", Toast.LENGTH_SHORT).show();
                } else if (!slaptazodis.isEmpty() && !patvirtinti.isEmpty() && !slaptazodis.equals(patvirtinti)) {
                    Toast.makeText(getContext(), "Slaptazodziai nesutampa", Toast.LENGTH_SHORT).show();
                } else {
                    PakeistiMokinioDuomenis task = new PakeistiMokinioDuomenis(prisijungti.currentMokinys.getId(), vardas, email, vartotojoVardas, slaptazodis);
                    task.execute();
                    prisijungti.currentKorepetitorius = null;
                    prisijungti.currentMokinys = null;
                    Intent intent = new Intent(getContext(), prisijungti.class);
                    startActivity(intent);
                }
            }
        });
        return v;
    }
    private class PakeistiMokinioDuomenis extends AsyncTask<String, Void, String> {

        private int mokinioId;
        private String vardas;
        private String email;
        private String vartotojoVardas;
        private String slaptazodis;

        public PakeistiMokinioDuomenis(int mokinioId, String vardas, String email, String vartotojoVardas, String slaptazodis) {
            this.mokinioId = mokinioId;
            this.vardas = vardas;
            this.email = email;
            this.vartotojoVardas = vartotojoVardas;
            this.slaptazodis = slaptazodis;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.0.105/PHPscriptai/mokinysAtnaujintiDuomenis.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data = "mokinio_id=" + mokinioId + "&pilnas_mokinio_vardas=" + vardas + "&mokinio_vartotojo_vardas=" + vartotojoVardas + "&mokinio_slaptazodis=" + slaptazodis
                        + "&mokinio_el_pastas=" + email;
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
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
            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
        }
    }
}