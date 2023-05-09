package com.example.bakalauras.ui.uzklausos.mokinio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.Prisijungti;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.MokinioUzklausaKortele;

public class MokinioCardAdapter extends RecyclerView.Adapter<MokinioUzklausosCardHolder>{

    private ArrayList<MokinioUzklausaKortele> list;
    private Context context;

    public MokinioCardAdapter(ArrayList<MokinioUzklausaKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MokinioUzklausosCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_mokiniui_rodoma_uzklausa_item, parent, false);

        return new MokinioUzklausosCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MokinioUzklausosCardHolder holder, @SuppressLint("RecyclerView") int position) {
        MokinioUzklausaKortele sarasas = list.get(position);

        MokinioUzklausosCardHolder.korepetitoriausVardas.setText("Korepetitorius: "+ sarasas.getVardasKorepetitoriaus());
        if (sarasas.getStausas() == 0)
        {
            MokinioUzklausosCardHolder.statusas.setText("Laukiama patvirtinimo.");
        }
        else if (sarasas.getStausas() == 1)
        {
            MokinioUzklausosCardHolder.statusas.setText("Užklausa patvirtinta.");
            MokinioUzklausosCardHolder.atsaukti.setVisibility(View.GONE);
        }
        else
        {
            MokinioUzklausosCardHolder.statusas.setText("Užklausa atšaukta");
            MokinioUzklausosCardHolder.atsaukti.setVisibility(View.GONE);
        }

        Picasso.get()
                .load("http://192.168.0.108/PHPscriptai/" + sarasas.getKorepetitoriausNuotrauka())
                .transform(new CircleTransform())
                .into(MokinioUzklausosCardHolder.pfp);

        MokinioUzklausosCardHolder.atsaukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasalintiUzklausaTask task = new PasalintiUzklausaTask(Prisijungti.currentMokinys.getId(), sarasas.getId());
                task.execute();
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
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
    private class PasalintiUzklausaTask extends AsyncTask<String, Void, String> {

        private int mokinysId;
        private int id;

        public PasalintiUzklausaTask(int mokinysId, int id) {
            this.mokinysId = mokinysId;
            this.id = id;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.0.108/PHPscriptai/pasalintiUzklausaMokinys.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data = "mokinio_id=" + mokinysId + "&id=" + id;
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

                // Read the response from the PHP script
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
            notifyDataSetChanged();
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
