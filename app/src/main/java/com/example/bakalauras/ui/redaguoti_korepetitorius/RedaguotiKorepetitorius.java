package com.example.bakalauras.ui.redaguoti_korepetitorius;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.ui.korepetitorius.KorKurtiProfili;
import com.example.bakalauras.ui.korepetitorius.Korepetitorius_profilis;
import com.example.bakalauras.ui.redaguoti_mokinys.RedaguotiMokinys;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RedaguotiKorepetitorius#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RedaguotiKorepetitorius extends Fragment {

    private Button pridetiDalyka, pakeisti;
    private ListView dalykuSarasas;
    private Spinner spinnerDalykai;
    private EditText bio, profilisAdresas, profilisMiestas, kainaPerVal, dalykas, istaiga, vardasText, emailText, vartotojoVardasText, slaptazodisText, patvirtintiPassText;;
    private CheckBox gyvai, nuotoliniu;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter adapter;
    private boolean[][] prieinamumas;
    private TableLayout uzpildyti;
    private String adresas, miestas, valString, aprasymas, istaigaString, dalykai_istaigoj, vartotojoVarads, email, vardas, slaptazodis;
    private int mokymo_tipas;

    public RedaguotiKorepetitorius() {
        // Required empty public constructor
    }

    public static RedaguotiKorepetitorius newInstance() {
        RedaguotiKorepetitorius fragment = new RedaguotiKorepetitorius();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean[][] getCheckboxState(View v){
        TableLayout table = v.findViewById(R.id.lentelePasirinkimuRedaguoti);
        int numRows = table.getChildCount();
        int numCols = ((TableRow) table.getChildAt(0)).getChildCount() - 1; // Exclude first column
        boolean[][] checkboxState = new boolean[numRows][numCols];
        for (int i = 1; i < numRows; i++) { // Exclude first row
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j = 1; j <= numCols; j++) {
                CheckBox checkbox = (CheckBox) row.getChildAt(j);
                checkboxState[i - 1][j - 1] = checkbox.isChecked();
            }
        }
        return checkboxState;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_redaguoti_korepetitorius, container, false);

        pridetiDalyka = v.findViewById(R.id.buttonPridėtiRedaguoti);
        dalykuSarasas = v.findViewById(R.id.pasirinktiDalykaiRedaguoti);
        spinnerDalykai = v.findViewById(R.id.spinnerDalykaiRedaguoti);
        profilisAdresas = v.findViewById(R.id.profilisAdresasRedaguoti);
        profilisMiestas = v.findViewById(R.id.profilisMiestasRedaguoti);
        kainaPerVal = v.findViewById(R.id.kainaPerValProfilisRedaguoti);
        dalykas = v.findViewById(R.id.dalykasRedaguoti);
        uzpildyti = v.findViewById(R.id.lentelePasirinkimuRedaguoti);
        istaiga = v.findViewById(R.id.istaigosPavRedaguoti);
        gyvai = v.findViewById(R.id.checkBoxGyvaiRedaguoti);
        nuotoliniu = v.findViewById(R.id.checkBoxOnlineRedaguoti);
        bio = v.findViewById(R.id.bioProfilisRedaguoti);
        dalykuSarasas.setNestedScrollingEnabled(true);
        bio.setNestedScrollingEnabled(true);
        pakeisti = v.findViewById(R.id.buttonRedaguotiKoreptitorius);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listItems);
        dalykuSarasas.setAdapter(adapter);

        vardasText = v.findViewById(R.id.vardasKorepetitoriusRedaguoti);
        emailText = v.findViewById(R.id.emailKorepetitoriusRedaguoti);
        vartotojoVardasText = v.findViewById(R.id.vartotojoKorepetitoriusRedaguoti);
        slaptazodisText = v.findViewById(R.id.slaptazodisRedaguotiKorepetitorius);
        patvirtintiPassText = v.findViewById(R.id.slaptazodisPatvirtintiRedaguotiKorepetitorius);

        UzpildytiProfili task = new UzpildytiProfili(prisijungti.currentKorepetitorius.getId());
        task.execute();

        pakeisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean[][] checkboxState = getCheckboxState(v);
                String miestas, adresas, istaigaGet, bioGet, dalykasGet, kainaGet, vardasGet, emailGet, vartotojoGet, slaptazodisGet, patvirtintiGet;
                miestas = String.valueOf(profilisMiestas.getText());
                adresas = String.valueOf(profilisAdresas.getText());
                istaigaGet = String.valueOf(istaiga.getText());
                bioGet = String.valueOf(bio.getText());
                dalykasGet = String.valueOf(dalykas.getText());
                kainaGet = String.valueOf(kainaPerVal.getText());
                vardasGet = String.valueOf(vardasText.getText());
                emailGet = String.valueOf(emailText.getText());
                vartotojoGet = String.valueOf(vartotojoVardasText.getText());
                slaptazodisGet = String.valueOf(slaptazodisText.getText());
                patvirtintiGet = String.valueOf(patvirtintiPassText.getText());

                if (miestas.isEmpty() || adresas.isEmpty() || istaigaGet.isEmpty() || bioGet.isEmpty() || dalykasGet.isEmpty() ||
                        kainaGet.isEmpty() || vardasGet.isEmpty() || emailGet.isEmpty() || vartotojoGet.isEmpty() || slaptazodisGet.isEmpty() || patvirtintiGet.isEmpty()
                || listItems.isEmpty() || !gyvai.isChecked() || !nuotoliniu.isChecked()) {
                    Toast.makeText(getContext(), "Užpildykite visus laukus!", Toast.LENGTH_SHORT).show();
                } else if (!slaptazodisGet.isEmpty() && !patvirtintiGet.isEmpty() && !slaptazodisGet.equals(patvirtintiGet)) {
                    Toast.makeText(getContext(), "Slaptažodžiai nesutampa!", Toast.LENGTH_SHORT).show();
                } else {
                    if (gyvai.isChecked() && !nuotoliniu.isChecked())
                    {
                        new SukurtiProfili().execute(miestas, adresas, 1, listItems, kainaGet, bioGet, istaigaGet, dalykasGet, checkboxState, prisijungti.currentKorepetitorius.getId(), vardasGet, emailGet, vartotojoGet, slaptazodisGet);
                        prisijungti.currentKorepetitorius = null;
                        prisijungti.currentMokinys = null;
                        Intent intent = new Intent(getContext(), prisijungti.class);
                        startActivity(intent);
                    }
                    else if (!gyvai.isChecked() && nuotoliniu.isChecked())
                    {
                        new SukurtiProfili().execute(miestas, adresas, 2, listItems, kainaGet, bioGet, istaigaGet, dalykasGet, checkboxState, prisijungti.currentKorepetitorius.getId(), vardasGet, emailGet, vartotojoGet, slaptazodisGet);
                        prisijungti.currentKorepetitorius = null;
                        prisijungti.currentMokinys = null;
                        Intent intent = new Intent(getContext(), prisijungti.class);
                        startActivity(intent);
                    }
                    else
                    {
                        new SukurtiProfili().execute(miestas, adresas, 3, listItems, kainaGet, bioGet, istaigaGet, dalykasGet, checkboxState, prisijungti.currentKorepetitorius.getId(), vardasGet, emailGet, vartotojoGet, slaptazodisGet);
                        prisijungti.currentKorepetitorius = null;
                        prisijungti.currentMokinys = null;
                        Intent intent = new Intent(getContext(), prisijungti.class);
                        startActivity(intent);
                    }
                }
            }
        });

        pridetiDalyka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listItems.contains(spinnerDalykai.getSelectedItem().toString())) {
                    listItems.add(spinnerDalykai.getSelectedItem().toString());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        dalykuSarasas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Laikykite, kad pašalinti.", Toast.LENGTH_SHORT).show();
            }
        });

        dalykuSarasas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                listItems.remove(adapterView.getItemAtPosition(i));
                adapter.notifyDataSetChanged();

                return true;
            }
        });

        return v;
    }

    private class SukurtiProfili extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {
            String miestas = (String) params[0];
            String adresas = (String) params[1];
            int mokymoTipas = (int) params[2];
            ArrayList<String> listViewData = (ArrayList<String>) params[3];
            String kaina = (String) params[4];
            String bio = (String) params[5];
            String istaiga = (String) params[6];
            String dalykas = (String) params[7];
            boolean[][] selectionArray = (boolean[][]) params[8];
            int korepetitoriausId = (int) params[9];
            String vardas = (String) params[10];
            String email = (String) params[11];
            String vartotojas = (String) params[12];
            String slaptazodis = (String) params[13];

            URL url;
            try {
                url = new URL("http://192.168.0.104/PHPscriptai/korepetitoriusAtnaujintiProfili.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data;

                data = "korepetitoriaus_id=" + korepetitoriausId + "&korepetitoriaus_adresas=" + adresas + "&korepetitoriaus_miestas=" + miestas + "&korepetitoriaus_mokymo_tipas=" + mokymoTipas + "&korepetitoriaus_val=" + kaina + "&korepetitoriaus_mokymo_tipas=" + mokymoTipas + "&korepetitoriaus_aprasymas=" + bio  + "&korepetitoriaus_istaiga=" + istaiga + "&korepetitoriaus_dalykai_istaigoj=" + dalykas
                + "&pilnas_korepetitoriaus_vardas=" + vardas + "&korepetitoriaus_vartotojo_vardas=" + vartotojas + "&korepetitoriaus_slaptazodis=" + slaptazodis + "&korepetitoriaus_el_pastas=" + email;

                Gson gson = new Gson();
                String selectionArrayString = gson.toJson(selectionArray);
                data += "&korepetitoriaus_prieinamumas=" + URLEncoder.encode(selectionArrayString, "UTF-8");

                String listViewDataString = gson.toJson(listViewData);
                data += "&korepetitoriaus_dalykai=" + URLEncoder.encode(listViewDataString, "UTF-8");

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
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            Log.d("edit kor", result);
        }
    }

    private class UzpildytiProfili extends AsyncTask<Void, Void, Void> {

        private int korepetitoriaus_id;

        public UzpildytiProfili(int korepetitoriaus_id) {
            this.korepetitoriaus_id = korepetitoriaus_id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://192.168.0.104/PHPscriptai/gautiRedagavimuiDuomenisKorepetitoriui.php?korepetitoriaus_id=" + korepetitoriaus_id);
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
                    mokymo_tipas = obj.getInt("korepetitoriaus_mokymo_tipas");
                    valString = obj.getString("korepetitoriaus_val");
                    aprasymas = obj.getString("korepetitoriaus_aprasymas");
                    istaigaString = obj.getString("korepetitoriaus_istaiga");
                    dalykai_istaigoj = obj.getString("korepetitoriaus_dalykai_istaigoj");

                    vartotojoVarads = obj.getString("korepetitoriaus_vartotojo_vardas");
                    email = obj.getString("korepetitoriaus_el_pastas");
                    vardas = obj.getString("pilnas_korepetitoriaus_vardas");
                    slaptazodis = obj.getString("korepetitoriaus_slaptazodis");

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
            vardasText.setText(prisijungti.currentKorepetitorius.getName());
            profilisAdresas.setText(adresas);
            profilisMiestas.setText(miestas);
            if (mokymo_tipas == 1)
            {
                gyvai.setChecked(true);
            }
            else if (mokymo_tipas == 2)
            {
                nuotoliniu.setChecked(true);
            }
            else
            {
                gyvai.setChecked(true);
                nuotoliniu.setChecked(true);
            }
            kainaPerVal.setText(valString);
            bio.setText(aprasymas);
            istaiga.setText(istaigaString);
            dalykas.setText(dalykai_istaigoj);
            emailText.setText(email);
            vardasText.setText(vardas);
            vartotojoVardasText.setText(vartotojoVarads);
            vardasText.setText(vardas);

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