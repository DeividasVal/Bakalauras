package com.example.bakalauras.ui.korepetitorius;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.bakalauras.R;
import com.example.bakalauras.ui.redaguoti_korepetitorius.RedaguotiKorepetitorius;
import com.example.bakalauras.ui.prisijungti.Prisijungti;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import Model.Atsiliepimas;
import Model.KorepetitoriusProfilis;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Korepetitorius_profilis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Korepetitorius_profilis extends Fragment {

    private TextView vardasText, adresasText, mokymoBudasText, bioText, istaigaText, dalykasText, dalykaiText, kainaText, vidurkisIsDB, countisDB, emptyRecycler;
    private String adresas, miestas, tipas, val, bio, istaiga, dalykaiIst, dalykaiJoined, pfpNuotrauka;
    private TableLayout uzpildyti;
    private boolean[][] prieinamumas;
    private RecyclerView recyclerView;
    private ArrayList<Atsiliepimas> arrayList;
    private AtsiliepimasCardAdapter adapter;
    private Double ivertinimasVidurkis;
    private int invertinimasCount;
    private RatingBar ratingBar;
    private ImageView pfp;
    private Button redaguoti;
    public static KorepetitoriusProfilis profilis;

    public Korepetitorius_profilis() {
    }

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
        vidurkisIsDB = v.findViewById(R.id.vidurkisIsDB);
        countisDB = v.findViewById(R.id.atsiliepimuKiekisIsDB);
        ratingBar = v.findViewById(R.id.ratingBarProfilis);
        emptyRecycler = v.findViewById(R.id.neraAtsiliepimu);
        pfp = v.findViewById(R.id.korepetitoriausNuotraukaVienameLange);
        redaguoti = v.findViewById(R.id.redaguotiButtonKorepetitorius);

        recyclerView = v.findViewById(R.id.recyclerViewProfilisReviews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        arrayList = new ArrayList<Atsiliepimas>();

        UzpildytiProfili task = new UzpildytiProfili(Prisijungti.currentKorepetitorius.getId());
        task.execute();

        UzkrautiAtsiliepimus task2 = new UzkrautiAtsiliepimus(Prisijungti.currentKorepetitorius.getId());
        task2.execute();

        redaguoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RedaguotiKorepetitorius.class);
                startActivity(intent);
            }
        });

        return v;
    }

    private class UzkrautiAtsiliepimus extends AsyncTask<Void, Void, Void> {

        private int korepetitoriaus_id;

        public UzkrautiAtsiliepimus(int korepetitoriaus_id) {
            this.korepetitoriaus_id = korepetitoriaus_id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.0.106/PHPscriptai/gautiAtsiliepimus.php?korepetitoriaus_id=" + korepetitoriaus_id);
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
                    String mokinioNuotrauka = obj.getString("mokinio_nuotrauka");

                    arrayList.add(new Atsiliepimas(vardasMokinio, mokinioId, profilioId, korId, atsiliepimoTekstas, ivertinimas, laikas, mokinioNuotrauka));

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
                adapter = new AtsiliepimasCardAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }
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
                URL url = new URL("http://192.168.0.106/PHPscriptai/gautiKorepetitoriusProfilisVienamLange.php?korepetitoriaus_id=" + korepetitoriaus_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output;
                while ((output = br.readLine()) != null) {
                    JSONObject obj = new JSONObject(output);
                    Log.d("response", output);
                    adresas = obj.getString("profilio_adresas");
                    pfpNuotrauka = obj.getString("korepetitoriaus_nuotrauka");
                    miestas = obj.getString("profilio_miestas");
                    tipas = obj.getString("profilio_mokymo_tipas");
                    val = obj.getString("profilio_val");
                    bio = obj.getString("profilio_aprasymas");
                    istaiga = obj.getString("profilio_istaiga");
                    dalykaiIst = obj.getString("profilio_dalykai_istaigoj");
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

                    JSONArray dalykaiJson = obj.getJSONArray("profilio_dalykai");
                    ArrayList<String> dalykai = new ArrayList<String>();
                    for (int i = 0; i < dalykaiJson.length(); i++) {
                        dalykai.add(dalykaiJson.getString(i));
                    }
                    dalykaiJoined = TextUtils.join(", ", dalykai);

                    JSONArray prieinamumasJson = obj.getJSONArray("profilio_prieinamumas");
                    prieinamumas = new boolean[prieinamumasJson.length()][prieinamumasJson.getJSONArray(0).length()];
                    for (int i = 0; i < prieinamumasJson.length(); i++) {
                        JSONArray rowJson = prieinamumasJson.getJSONArray(i);
                        for (int j = 0; j < rowJson.length(); j++) {
                            prieinamumas[i][j] = rowJson.getBoolean(j);
                        }
                    }
                }
                profilis = new KorepetitoriusProfilis(adresas, pfpNuotrauka, miestas, tipas, val, bio, istaiga, dalykaiIst, ivertinimasVidurkis, invertinimasCount, dalykaiJoined, prieinamumas);
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            vardasText.setText(Prisijungti.currentKorepetitorius.getName());
            dalykaiText.setText("Dalykai: " + profilis.getDalykaiMokymosi());
            adresasText.setText(profilis.getAdresas() + ", " + profilis.getMiestas());
            if (Integer.parseInt(profilis.getTipas()) == 1)
            {
                mokymoBudasText.setText("Mokymo tipas: Gyvai");
            }
            else if (Integer.parseInt(profilis.getTipas()) == 2)
            {
                mokymoBudasText.setText("Mokymo tipas: Nuotoliniu");
            }
            else
            {
                mokymoBudasText.setText("Mokymo tipas: Gyvai ir nuotoliniu");
            }
            if (profilis.getIvertinimuKiekis() == 0)
            {
                countisDB.setText("(0 atsiliepimų)");

            }
            else
            {
                countisDB.setText("("+ profilis.getIvertinimuKiekis() + " atsiliepimai)");
            }
            DecimalFormat df = new DecimalFormat("#0.0");
            vidurkisIsDB.setText(df.format(profilis.getIvertinimas()));
            ratingBar.setNumStars(5);
            ratingBar.setIsIndicator(true);
            ratingBar.setRating(profilis.getIvertinimas().floatValue());
            kainaText.setText("Kaina: " + val + " Eur/val.");
            bioText.setText(profilis.getBio());
            istaigaText.setText(profilis.getIstaiga());
            dalykasText.setText(profilis.getDalykaiIstaigoje());

            Picasso.get()
                    .load("http://192.168.0.106/PHPscriptai/" + profilis.getNuotrauka())
                    .transform(new CircleTransform())
                    .into(pfp);

            for (int i = 1; i < uzpildyti.getChildCount(); i++) {
                TableRow row = (TableRow) uzpildyti.getChildAt(i);
                for (int j = 1; j < row.getChildCount(); j++) {
                    CheckBox checkBox = (CheckBox) row.getChildAt(j);
                    if (checkBox != null) {
                        if (profilis.getPrieinamumas()[i-1][j-1]) {
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