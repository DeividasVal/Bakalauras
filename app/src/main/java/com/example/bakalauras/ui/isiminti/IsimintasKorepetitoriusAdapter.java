package com.example.bakalauras.ui.isiminti;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.ui.prisijungti.Prisijungti;
import com.example.bakalauras.R;
import com.example.bakalauras.ui.korepetitorius.sarasas.RecyclerViewPaspaustasKorepetitorius;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import Model.IsimintasKorepetitorius;

class IsimintasKorepetitoriusCardHolder extends RecyclerView.ViewHolder {

    public TextView vardas, kaina, dalykai, budasKortele, ivertinimas;
    public Button favorite;
    public ImageView zvaigzde, pfp;

    public IsimintasKorepetitoriusCardHolder(@NonNull View itemView) {
        super(itemView);

        vardas = itemView.findViewById(R.id.vardasKortele);
        kaina = itemView.findViewById(R.id.kainaKortele);
        dalykai = itemView.findViewById(R.id.dalykaiKortele);
        budasKortele = itemView.findViewById(R.id.budasKortele);
        favorite = itemView.findViewById(R.id.favoriteButton);
        ivertinimas = itemView.findViewById(R.id.atsiliepimaiVidurkisKortele);
        zvaigzde = itemView.findViewById(R.id.imageView2);
        pfp = itemView.findViewById(R.id.korKortelePFP);
    }
}

public class IsimintasKorepetitoriusAdapter extends RecyclerView.Adapter<IsimintasKorepetitoriusCardHolder> {

    private ArrayList<IsimintasKorepetitorius> list;
    private Context context;

    public IsimintasKorepetitoriusAdapter(ArrayList<IsimintasKorepetitorius> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public IsimintasKorepetitoriusCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_korepetitorius_item, parent, false);

        return new IsimintasKorepetitoriusCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IsimintasKorepetitoriusCardHolder holder, int position) {
        IsimintasKorepetitorius sarasas = list.get(position);
        DecimalFormat df = new DecimalFormat("#0.0");
        new CheckFavoriteAsyncTask(holder, sarasas.getProfilioId()).execute();
        holder.vardas.setText(sarasas.getVardas());
        holder.dalykai.setText("Moko: " + sarasas.getDalykai());
        holder.kaina.setText(sarasas.getKaina() + " Eur/val.");
        holder.budasKortele.setText("Mokymo tipas: " + sarasas.getMokymoBudas());

        Picasso.get()
                .load("http://192.168.0.106/PHPscriptai/" + sarasas.getKorepetitoriausNuotrauka())
                .transform(new CircleTransform())
                .into(holder.pfp);

        if (sarasas.getIvertinimas() == 0.0)
        {
            holder.zvaigzde.setImageResource(R.drawable.ic_baseline_star_rate_24_gray);
            holder.ivertinimas.setText(sarasas.getIvertinimas().toString());
        }
        else
        {
            holder.ivertinimas.setText(df.format(sarasas.getIvertinimas()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecyclerViewPaspaustasKorepetitorius.class);
                intent.putExtra("korepetitorius_id", sarasas.getId());
                intent.putExtra("korepetitorius_vardas", sarasas.getVardas());
                context.startActivity(intent);
            }
        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                new PaziuretiArIsimintaAsyncTask(holder, adapterPosition, sarasas.getProfilioId()).execute();
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

    class PaziuretiArIsimintaAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private IsimintasKorepetitoriusCardHolder holder;
        private int profilioId;
        private int position;

        public PaziuretiArIsimintaAsyncTask(IsimintasKorepetitoriusCardHolder holder, int position, int profilioId) {
            this.holder = holder;
            this.profilioId = profilioId;
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return paziuretiArIsiminta(profilioId, Prisijungti.currentMokinys.getId());
        }

        @Override
        protected void onPostExecute(Boolean isFavorited) {
            if (isFavorited) {
                new PasalintiIssaugotaKorepetitoriu().execute(profilioId, Prisijungti.currentMokinys.getId());
                holder.favorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            } else {
                new IssaugotiKorepetitoriu().execute(profilioId, Prisijungti.currentMokinys.getId());
                holder.favorite.setBackgroundResource(R.drawable.ic_baseline_favorite_24_red);
            }
        }
    }

    private class IssaugotiKorepetitoriu extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int profilioId = params[0];
            int mokinioId = params[1];

            try {
                URL url = new URL("http://192.168.0.106/PHPscriptai/isimenaKorepetitoriu.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data;
                data = "profilio_id=" + profilioId + "&mokinio_id=" + mokinioId;
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
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    }

    private class PasalintiIssaugotaKorepetitoriu extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int profilioId = params[0];
            int mokinioId = params[1];

            try {
                URL url = new URL("http://192.168.0.106/PHPscriptai/pasalintiIsimintaKorepetitoriu.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data;
                data = "profilio_id=" + profilioId + "&mokinio_id=" + mokinioId;
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
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean paziuretiArIsiminta(int profilioId, int mokinioId) {
        try {
            URL url = new URL("http://192.168.0.106/PHPscriptai/arMokinysIsimineKorepetitoriu.php");;
            String requestBody = "profilio_id=" + profilioId + "&mokinio_id=" + mokinioId;

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(requestBody);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();
            reader.close();

            return response.equals("true");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private class CheckFavoriteAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private IsimintasKorepetitoriusCardHolder holder;
        private int profileId;

        public CheckFavoriteAsyncTask(IsimintasKorepetitoriusCardHolder holder, int profileId) {
            this.holder = holder;
            this.profileId = profileId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return paziuretiArIsiminta(profileId, Prisijungti.currentMokinys.getId());
        }

        @Override
        protected void onPostExecute(Boolean isFavorited) {
            if (Prisijungti.currentKorepetitorius != null) {
                holder.favorite.setVisibility(View.GONE);
            } else {
                if (isFavorited) {
                    holder.favorite.setBackgroundResource(R.drawable.ic_baseline_favorite_24_red);
                } else {
                    holder.favorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
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