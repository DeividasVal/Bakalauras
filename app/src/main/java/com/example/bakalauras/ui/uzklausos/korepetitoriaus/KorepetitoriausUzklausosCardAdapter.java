package com.example.bakalauras.ui.uzklausos.korepetitoriaus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.KorepetitoriausUzklausaKortele;

public class KorepetitoriausUzklausosCardAdapter extends RecyclerView.Adapter<KorepetitoriausUzklausosCardHolder>{

    private ArrayList<KorepetitoriausUzklausaKortele> list;
    private Context context;

    public KorepetitoriausUzklausosCardAdapter(ArrayList<KorepetitoriausUzklausaKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public KorepetitoriausUzklausosCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_uzklausa_item, parent, false);

        return new KorepetitoriausUzklausosCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KorepetitoriausUzklausosCardHolder holder, @SuppressLint("RecyclerView") int position) {
        KorepetitoriausUzklausaKortele sarasas = list.get(position);

        KorepetitoriausUzklausosCardHolder.mokinioVardas.setText("Mokinys: " + sarasas.getVardasMokinio());

        if (sarasas.getMokinioNuotrauka().isEmpty())
        {
            KorepetitoriausUzklausosCardHolder.pfp.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }
        else
        {
            Picasso.get()
                    .load("http://192.168.0.106/PHPscriptai/" + sarasas.getMokinioNuotrauka())
                    .transform(new CircleTransform())
                    .into(KorepetitoriausUzklausosCardHolder.pfp);
        }

        KorepetitoriausUzklausosCardHolder.atsaukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UzklausosVeiksmasTask task = new UzklausosVeiksmasTask(sarasas.getId(), 2, position);
                task.execute();
            }
        });
        KorepetitoriausUzklausosCardHolder.patvirtinti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UzklausosVeiksmasTask task = new UzklausosVeiksmasTask(sarasas.getId(), 1, position);
                task.execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        else
        {
            return 0;
        }
    }

    private class UzklausosVeiksmasTask extends AsyncTask<String, Void, String> {

        private int Id;
        private int busena;
        private int position;

        public UzklausosVeiksmasTask(int Id, int busena, int position) {
            this.Id = Id;
            this.busena = busena;
            this.position = position;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.0.106/PHPscriptai/uzklausaKorepetitoriuiPasirinkti.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data = "id=" + Id + "&būsena=" + busena;
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
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            Log.d("kor", response);
            if (position < list.size()) {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
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
