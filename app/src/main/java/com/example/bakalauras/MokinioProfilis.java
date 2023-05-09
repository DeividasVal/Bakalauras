package com.example.bakalauras;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakalauras.ui.redaguoti_mokinys.RedaguotiMokinys;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MokinioProfilis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MokinioProfilis extends Fragment {

    private TextView vardasText, pastasText, vartotojoVardasText;
    private String vardas, pastas, vartotojoVardas;
    private ImageView pfp;
    private Button redaguoti;

    public MokinioProfilis() {
        // Required empty public constructor
    }

    public static MokinioProfilis newInstance() {
        MokinioProfilis fragment = new MokinioProfilis();
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
        pfp = v.findViewById(R.id.mokinioPFPProfilis);
        redaguoti = v.findViewById(R.id.RedaguotiButton);

        UzpildytiProfiliMokinio task = new UzpildytiProfiliMokinio(Prisijungti.currentMokinys.getId());
        task.execute();

        if (Prisijungti.currentMokinys.getMokinioNuotrauka().isEmpty())
        {
            pfp.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }
        else
        {
            Picasso.get()
                    .load("http://192.168.0.108/PHPscriptai/" + Prisijungti.currentMokinys.getMokinioNuotrauka())
                    .transform(new CircleTransform())
                    .into(pfp);
        }

        redaguoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RedaguotiMokinys.class);
                startActivity(intent);
            }
        });

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
                URL url = new URL("http://192.168.0.108/PHPscriptai/gautiMokinioProfilisVienameLange.php?mokinio_id=" + mokinio_id);
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