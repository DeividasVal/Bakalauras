package com.example.bakalauras.ui.redaguoti_korepetitorius;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.bakalauras.R;
import com.example.bakalauras.ui.prisijungti.Prisijungti;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class RedaguotiKorepetitorius extends AppCompatActivity {

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
    private Uri filepath;
    private ImageView pfp;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setTitle("Redaguoti profilį");
        setContentView(R.layout.activity_redaguoti_korepetitorius);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        pridetiDalyka = findViewById(R.id.buttonPridėtiRedaguoti);
        dalykuSarasas = findViewById(R.id.pasirinktiDalykaiRedaguoti);
        spinnerDalykai = findViewById(R.id.spinnerDalykaiRedaguoti);
        profilisAdresas = findViewById(R.id.profilisAdresasRedaguoti);
        profilisMiestas = findViewById(R.id.profilisMiestasRedaguoti);
        kainaPerVal = findViewById(R.id.kainaPerValProfilisRedaguoti);
        dalykas = findViewById(R.id.dalykasRedaguoti);
        uzpildyti = findViewById(R.id.lentelePasirinkimuRedaguoti);
        istaiga = findViewById(R.id.istaigosPavRedaguoti);
        gyvai = findViewById(R.id.checkBoxGyvaiRedaguoti);
        nuotoliniu = findViewById(R.id.checkBoxOnlineRedaguoti);
        bio = findViewById(R.id.bioProfilisRedaguoti);
        pfp = findViewById(R.id.redaguotiKor);
        dalykuSarasas.setNestedScrollingEnabled(true);
        bio.setNestedScrollingEnabled(true);
        pakeisti = findViewById(R.id.buttonRedaguotiKoreptitorius);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listItems);
        dalykuSarasas.setAdapter(adapter);

        vardasText = findViewById(R.id.vardasKorepetitoriusRedaguoti);
        emailText = findViewById(R.id.emailKorepetitoriusRedaguoti);
        vartotojoVardasText = findViewById(R.id.vartotojoKorepetitoriusRedaguoti);
        slaptazodisText = findViewById(R.id.slaptazodisRedaguotiKorepetitorius);
        patvirtintiPassText = findViewById(R.id.slaptazodisPatvirtintiRedaguotiKorepetitorius);

        UzpildytiProfili task = new UzpildytiProfili(Prisijungti.currentKorepetitorius.getId());
        task.execute();

        Picasso.get()
                .load("http://192.168.0.106/PHPscriptai/" + Prisijungti.currentKorepetitorius.getKorepetitoriausNuotrauka())
                .transform(new CircleTransform())
                .into(pfp);

        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
            }
        });

        pakeisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean[][] checkboxState = getCheckboxState();
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
                        || listItems.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Užpildykite visus laukus!", Toast.LENGTH_SHORT).show();
                } else if (!slaptazodisGet.isEmpty() && !patvirtintiGet.isEmpty() && !slaptazodisGet.equals(patvirtintiGet)) {
                    Toast.makeText(getApplicationContext(), "Slaptažodžiai nesutampa!", Toast.LENGTH_SHORT).show();
                } else {
                    if (gyvai.isChecked() && !nuotoliniu.isChecked())
                    {
                        new SukurtiProfili().execute(miestas, adresas, 1, listItems, kainaGet, bioGet, istaigaGet, dalykasGet, checkboxState, Prisijungti.currentKorepetitorius.getId(), vardasGet, emailGet, vartotojoGet, slaptazodisGet);
                        new PakeistiNuotrauka().execute(filepath, Prisijungti.currentKorepetitorius.getId());
                        Prisijungti.currentKorepetitorius = null;
                        Prisijungti.currentMokinys = null;
                        Intent intent = new Intent(getApplicationContext(), Prisijungti.class);
                        startActivity(intent);

                    }
                    else if (!gyvai.isChecked() && nuotoliniu.isChecked())
                    {
                        new SukurtiProfili().execute(miestas, adresas, 2, listItems, kainaGet, bioGet, istaigaGet, dalykasGet, checkboxState, Prisijungti.currentKorepetitorius.getId(), vardasGet, emailGet, vartotojoGet, slaptazodisGet);
                        new PakeistiNuotrauka().execute(filepath, Prisijungti.currentKorepetitorius.getId());
                        Prisijungti.currentKorepetitorius = null;
                        Prisijungti.currentMokinys = null;
                        Intent intent = new Intent(getApplicationContext(), Prisijungti.class);
                        startActivity(intent);
                    }
                    else
                    {
                        new SukurtiProfili().execute(miestas, adresas, 3, listItems, kainaGet, bioGet, istaigaGet, dalykasGet, checkboxState, Prisijungti.currentKorepetitorius.getId(), vardasGet, emailGet, vartotojoGet, slaptazodisGet);
                        new PakeistiNuotrauka().execute(filepath, Prisijungti.currentKorepetitorius.getId());
                        Prisijungti.currentKorepetitorius = null;
                        Prisijungti.currentMokinys = null;
                        Intent intent = new Intent(getApplicationContext(), Prisijungti.class);
                        startActivity(intent);
                    }
                }
            }
        });

        pridetiDalyka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItems.size() >= 3) {
                    Toast.makeText(getApplicationContext(), "Galite pasirinkti tik 3 dalykus arba mažiau!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Laikykite, kad pašalinti.", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int size = Math.min(width, height);
                Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);
                canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);
                pfp.setImageBitmap(output);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private boolean[][] getCheckboxState(){
        TableLayout table = findViewById(R.id.lentelePasirinkimuRedaguoti);
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

    private class PakeistiNuotrauka extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {
            Uri filepath = (Uri) params[0];
            int korepetitoriausId = (int) params[1];
            URL url;
            try {
                url = new URL("http://192.168.0.106/PHPscriptai/korepetitoriusAtnaujintiNuotrauka.php");
                String data = "korepetitoriaus_id=" + korepetitoriausId;
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                byte[] fileBytes = getBytes(inputStream);

                String boundary = "*****";
                String twoHyphens = "--";
                String lineEnd = "\r\n";
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" +
                        getFileName(filepath) + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.write(fileBytes);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"korepetitoriaus_id\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(String.valueOf(korepetitoriausId));
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"data\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(data);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                outputStream.flush();
                outputStream.close();

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
            Log.d("edit nuotrauka", result);
        }
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
                url = new URL("http://192.168.0.106/PHPscriptai/korepetitoriusAtnaujintiProfili.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data;

                data = "korepetitoriaus_id=" + korepetitoriausId + "&profilio_adresas=" + adresas + "&profilio_miestas=" + miestas + "&profilio_mokymo_tipas=" + mokymoTipas + "&profilio_val=" + kaina + "&profilio_mokymo_tipas=" + mokymoTipas + "&profilio_aprasymas=" + bio  + "&profilio_istaiga=" + istaiga + "&profilio_dalykai_istaigoj=" + dalykas
                        + "&pilnas_korepetitoriaus_vardas=" + vardas + "&korepetitoriaus_vartotojo_vardas=" + vartotojas + "&korepetitoriaus_slaptazodis=" + slaptazodis + "&korepetitoriaus_el_pastas=" + email;

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
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
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
                URL url = new URL("http://192.168.0.106/PHPscriptai/gautiRedagavimuiDuomenisKorepetitoriui.php?korepetitoriaus_id=" + korepetitoriaus_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output;
                while ((output = br.readLine()) != null) {
                    JSONObject obj = new JSONObject(output);
                    Log.d("response", output);

                    adresas = obj.getString("profilio_adresas");
                    miestas = obj.getString("profilio_miestas");
                    mokymo_tipas = obj.getInt("profilio_mokymo_tipas");
                    valString = obj.getString("profilio_val");
                    aprasymas = obj.getString("profilio_aprasymas");
                    istaigaString = obj.getString("profilio_istaiga");
                    dalykai_istaigoj = obj.getString("profilio_dalykai_istaigoj");

                    vartotojoVarads = obj.getString("korepetitoriaus_vartotojo_vardas");
                    email = obj.getString("korepetitoriaus_el_pastas");
                    vardas = obj.getString("pilnas_korepetitoriaus_vardas");
                    slaptazodis = obj.getString("korepetitoriaus_slaptazodis");

                    JSONArray prieinamumasJson = obj.getJSONArray("profilio_prieinamumas");
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
            vardasText.setText(Prisijungti.currentKorepetitorius.getName());
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

    public class CircleTransform implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}