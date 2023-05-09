package com.example.bakalauras.ui.korepetitorius;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.bakalauras.Prisijungti;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KorKurtiProfili#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KorKurtiProfili extends Fragment {

    private Button pridetiDalyka, kurtiProfili;
    private ListView dalykuSarasas;
    private Spinner spinnerDalykai;
    private EditText bio, profilisAdresas, profilisMiestas, kainaPerVal, dalykas, istaiga;
    private CheckBox gyvai, nuotoliniu;
    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayAdapter adapter;

    public KorKurtiProfili() {
        // Required empty public constructor
    }

    private boolean[][] getCheckboxState(View v){
        TableLayout table = v.findViewById(R.id.lentelePasirinkimu);
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

    public static KorKurtiProfili newInstance() {
        KorKurtiProfili fragment = new KorKurtiProfili();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_kor_kurti_profili, container, false);

        pridetiDalyka = v.findViewById(R.id.buttonPridėti);
        dalykuSarasas = v.findViewById(R.id.pasirinktiDalykai);
        spinnerDalykai = v.findViewById(R.id.spinnerDalykai);
        profilisAdresas = v.findViewById(R.id.profilisAdresas);
        profilisMiestas = v.findViewById(R.id.profilisMiestas);
        kainaPerVal = v.findViewById(R.id.kainaPerValProfilis);
        dalykas = v.findViewById(R.id.dalykas);
        istaiga = v.findViewById(R.id.istaigosPav);
        gyvai = v.findViewById(R.id.checkBoxGyvai);
        nuotoliniu = v.findViewById(R.id.checkBoxOnline);
        bio = v.findViewById(R.id.bioProfilis);
        dalykuSarasas.setNestedScrollingEnabled(true);
        bio.setNestedScrollingEnabled(true);
        kurtiProfili = v.findViewById(R.id.buttonSukurtiProfili);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listItems);
        dalykuSarasas.setAdapter(adapter);

        kurtiProfili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean[][] checkboxState = getCheckboxState(v);
                String miestas, adresas, istaigaGet, bioGet, dalykasGet, kainaGet;
                miestas = String.valueOf(profilisMiestas.getText());
                adresas = String.valueOf(profilisAdresas.getText());
                istaigaGet = String.valueOf(istaiga.getText());
                bioGet = String.valueOf(bio.getText());
                dalykasGet = String.valueOf(dalykas.getText());
                kainaGet = String.valueOf(kainaPerVal.getText());

                if (!miestas.equals("") && !adresas.equals("")
                        && !istaigaGet.equals("") && !dalykasGet.equals("")
                        && !bioGet.equals("") && !kainaGet.equals(""))
                {
                    if (gyvai.isChecked() && !nuotoliniu.isChecked())
                    {
                            new SukurtiProfili().execute(miestas, adresas, 1, listItems, kainaGet, bioGet, istaigaGet, dalykasGet, checkboxState, Prisijungti.currentKorepetitorius.getId());

                    }
                    else if (!gyvai.isChecked() && nuotoliniu.isChecked())
                    {
                            new SukurtiProfili().execute(miestas, adresas, 2, listItems, kainaGet, bioGet, istaigaGet, dalykasGet, checkboxState, Prisijungti.currentKorepetitorius.getId());
                    }
                    else
                    {
                            new SukurtiProfili().execute(miestas, adresas, 3, listItems, kainaGet, bioGet, istaigaGet, dalykasGet, checkboxState, Prisijungti.currentKorepetitorius.getId());
                   }
                }
                else
                {
                    Toast.makeText(getContext(), "Ne visi privalomi laukai užpildyti!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pridetiDalyka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItems.size() >= 3) {
                    Toast.makeText(getContext(), "Galite pasirinkti tik 3 dalykus arba mažiau!", Toast.LENGTH_SHORT).show();
                    return;
                }

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
            URL url;
            try {
                url = new URL("http://192.168.0.108/PHPscriptai/korepetitoriusKurtiProfili.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data;

                data = "korepetitoriaus_id=" + korepetitoriausId + "&profilio_adresas=" + adresas + "&profilio_miestas=" + miestas + "&profilio_mokymo_tipas=" + mokymoTipas + "&profilio_val=" + kaina + "&profilio_mokymo_tipas=" + mokymoTipas + "&profilio_aprasymas=" + bio  + "&profilio_istaiga=" + istaiga + "&profilio_dalykai_istaigoj=" + dalykas;

                Gson gson = new Gson();
                String selectionArrayString = gson.toJson(selectionArray);
                data += "&profilio_prieinamumas=" + URLEncoder.encode(selectionArrayString, "UTF-8");

                String listViewDataString = gson.toJson(listViewData);
                data += "&profilio_dalykai=" + URLEncoder.encode(listViewDataString, "UTF-8");

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
            Log.d("result", result);
        }
    }
}