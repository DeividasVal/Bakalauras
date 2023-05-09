package com.example.bakalauras.ui.zinutes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.Prisijungti;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import Model.ZinutesKortele;

public class ZinutesCardAdapter extends RecyclerView.Adapter<ZinutesCardHolder>{
    private ArrayList<ZinutesKortele> list;
    private Context context;

    public ZinutesCardAdapter(ArrayList<ZinutesKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ZinutesCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_zinutes, parent, false);

        return new ZinutesCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZinutesCardHolder holder, @SuppressLint("RecyclerView") int position) {
        ZinutesKortele sarasas = list.get(position);

        if (Prisijungti.currentKorepetitorius != null)
        {
            ZinutesCardHolder.vardas.setText("Mokinys: " + sarasas.getVardas());
            if (sarasas.getNuotrauka().isEmpty())
            {
                ZinutesCardHolder.pfp.setImageResource(R.drawable.ic_baseline_account_circle_24);
            }
            else
            {
                Picasso.get()
                        .load("http://192.168.0.108/PHPscriptai/" + sarasas.getNuotrauka())
                        .transform(new CircleTransform())
                        .into(ZinutesCardHolder.pfp);
            }
        }
        else
        {
            ZinutesCardHolder.vardas.setText("Korepetitorius: " + sarasas.getVardas());
            Picasso.get()
                    .load("http://192.168.0.108/PHPscriptai/" + sarasas.getNuotrauka())
                    .transform(new CircleTransform())
                    .into(ZinutesCardHolder.pfp);
        }

        if (Prisijungti.currentKorepetitorius != null)
        {
            if (Prisijungti.currentKorepetitorius.getId() == sarasas.getSiuntejoId())
            {
                ZinutesCardHolder.zinutesTekstas.setText(sarasas.getZinutes());
            }
            else
            {
                ZinutesCardHolder.zinutesTekstas.setText("Jūs: " + sarasas.getZinutes());
            }
        }
        else
        {
            if (Prisijungti.currentMokinys.getId() == sarasas.getSiuntejoId())
            {
                ZinutesCardHolder.zinutesTekstas.setText(sarasas.getZinutes());

            }
            else
            {
                ZinutesCardHolder.zinutesTekstas.setText("Jūs: " + sarasas.getZinutes());
            }
        }

        ZinutesCardHolder.laikas.setText(sarasas.getLaikas());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Prisijungti.currentKorepetitorius != null)
                {
                    if (Prisijungti.currentKorepetitorius.getId() == sarasas.getSiuntejoId())
                    {
                        Intent intent = new Intent(context, Susirasyti.class);
                        intent.putExtra("dabartinis_id", Prisijungti.currentKorepetitorius.getId());
                        intent.putExtra("gavejas_id", sarasas.getGavejoId());
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(context, Susirasyti.class);
                        intent.putExtra("dabartinis_id", Prisijungti.currentKorepetitorius.getId());
                        intent.putExtra("gavejas_id", sarasas.getSiuntejoId());
                        context.startActivity(intent);
                    }
                }
                else
                {
                    if (Prisijungti.currentMokinys.getId() == sarasas.getSiuntejoId())
                    {
                        Intent intent = new Intent(context, Susirasyti.class);
                        intent.putExtra("dabartinis_id", Prisijungti.currentMokinys.getId());
                        intent.putExtra("gavejas_id", sarasas.getGavejoId());
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(context, Susirasyti.class);
                        intent.putExtra("dabartinis_id", Prisijungti.currentMokinys.getId());
                        intent.putExtra("gavejas_id", sarasas.getSiuntejoId());
                        context.startActivity(intent);
                    }
                }
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
