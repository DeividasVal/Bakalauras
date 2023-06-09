package com.example.bakalauras.ui.uzklausos.mokinioKorepetitoriai;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.bakalauras.ui.prisijungti.Prisijungti;
import com.example.bakalauras.ui.korepetitorius.sarasas.RecyclerViewPaspaustasKorepetitorius;
import com.example.bakalauras.ui.zinutes.Susirasyti;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.MokiniuiPatvirtintasKorepetitoriusKortele;

public class MokiniuiPatvirtintiKorepetitoriaiCardAdapter extends RecyclerView.Adapter<MokiniuiPatvirtintiKorepetitoriaiCardHolder>{

    private ArrayList<MokiniuiPatvirtintasKorepetitoriusKortele> list;
    private Context context;
    private Dialog popup;

    public MokiniuiPatvirtintiKorepetitoriaiCardAdapter(ArrayList<MokiniuiPatvirtintasKorepetitoriusKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MokiniuiPatvirtintiKorepetitoriaiCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mokinio_korepetitorius_item, parent, false);

        return new MokiniuiPatvirtintiKorepetitoriaiCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MokiniuiPatvirtintiKorepetitoriaiCardHolder holder, @SuppressLint("RecyclerView") int position) {
        MokiniuiPatvirtintasKorepetitoriusKortele sarasas = list.get(position);

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.korepetitoriausVardas.setText(sarasas.getVardasKorepetitoriaus());
        MokiniuiPatvirtintiKorepetitoriaiCardHolder.adresas.setText(sarasas.getAdresas());
        MokiniuiPatvirtintiKorepetitoriaiCardHolder.kaina.setText(sarasas.getKaina() + " Eur/val.");
        MokiniuiPatvirtintiKorepetitoriaiCardHolder.dalykai.setText("Dalykai: " + sarasas.getDalykai());

        Picasso.get()
                .load("http://192.168.0.106/PHPscriptai/" + sarasas.getKorepetitoriausNuotrauka())
                .transform(new CircleTransform())
                .into(MokiniuiPatvirtintiKorepetitoriaiCardHolder.pfp);

        popup = new Dialog(context);

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.atsaukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasalintiKorepetitoriuTask task = new PasalintiKorepetitoriuTask(Prisijungti.currentMokinys.getId(), sarasas.getPatvirtintasId(), position);
                task.execute();
            }
        });

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.parasyti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Susirasyti.class);
                intent.putExtra("dabartinis_id", Prisijungti.currentMokinys.getId());
                intent.putExtra("gavejas_id", sarasas.getKorepetitoriausId());
                context.startActivity(intent);
            }
        });

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.ivertinti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PopupAtsiliepimas.class);
                intent.putExtra("mokinio_id", Prisijungti.currentMokinys.getId());
                intent.putExtra("profilio_id", sarasas.getProfiliausId());
                intent.putExtra("korepetitoriaus_id", sarasas.getKorepetitoriausId());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecyclerViewPaspaustasKorepetitorius.class);
                intent.putExtra("korepetitorius_id", sarasas.getKorepetitoriausId());
                intent.putExtra("korepetitorius_vardas", sarasas.getVardasKorepetitoriaus());
                context.startActivity(intent);
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

    private class PasalintiKorepetitoriuTask extends AsyncTask<String, Void, String> {

        private int mokinioId;
        private int id;
        private int position;

        public PasalintiKorepetitoriuTask(int mokinioId, int id, int position) {
            this.mokinioId = mokinioId;
            this.id = id;
            this.position = position;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.0.106/PHPscriptai/pasalintiUzklausaMokinys.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data = "mokinio_id=" + mokinioId + "&id=" + id;
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
