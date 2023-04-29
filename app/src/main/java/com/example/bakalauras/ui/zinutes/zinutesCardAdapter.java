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
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.ui.uzklausos.korepetitoriausMokiniai.KorepetitoriuiPatvirtintiMokiniaiCardAdapter;
import com.example.bakalauras.ui.uzklausos.korepetitoriausMokiniai.KorepetitoriuiPatvirtintiMokiniaiCardHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import Model.ZinutesKortele;

public class zinutesCardAdapter extends RecyclerView.Adapter<zinutesCardHolder>{
    private ArrayList<ZinutesKortele> list;
    private Context context;

    public zinutesCardAdapter(ArrayList<ZinutesKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public zinutesCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_zinutes, parent, false);

        return new zinutesCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull zinutesCardHolder holder, @SuppressLint("RecyclerView") int position) {
        ZinutesKortele sarasas = list.get(position);

        if (prisijungti.currentKorepetitorius != null)
        {
            zinutesCardHolder.vardas.setText("Mokinys: " + sarasas.getVardas());
            if (sarasas.getNuotrauka().isEmpty())
            {
                zinutesCardHolder.pfp.setImageResource(R.drawable.ic_baseline_account_circle_24);
            }
            else
            {
                Picasso.get()
                        .load("http://192.168.0.101/PHPscriptai/" + sarasas.getNuotrauka())
                        .transform(new CircleTransform())
                        .into(zinutesCardHolder.pfp);
            }
        }
        else
        {
            zinutesCardHolder.vardas.setText("Korepetitorius: " + sarasas.getVardas());
            Picasso.get()
                    .load("http://192.168.0.101/PHPscriptai/" + sarasas.getNuotrauka())
                    .transform(new CircleTransform())
                    .into(zinutesCardHolder.pfp);
        }

        if (prisijungti.currentKorepetitorius != null)
        {
            if (prisijungti.currentKorepetitorius.getId() == sarasas.getSiuntejoId())
            {
                zinutesCardHolder.zinutesTekstas.setText(sarasas.getZinutes());
            }
            else
            {
                zinutesCardHolder.zinutesTekstas.setText("Jūs: " + sarasas.getZinutes());
            }
        }
        else
        {
            if (prisijungti.currentMokinys.getId() == sarasas.getSiuntejoId())
            {
                zinutesCardHolder.zinutesTekstas.setText(sarasas.getZinutes());

            }
            else
            {
                zinutesCardHolder.zinutesTekstas.setText("Jūs: " + sarasas.getZinutes());
            }
        }

        zinutesCardHolder.laikas.setText(sarasas.getLaikas());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prisijungti.currentKorepetitorius != null)
                {
                    if (prisijungti.currentKorepetitorius.getId() == sarasas.getSiuntejoId())
                    {
                        Intent intent = new Intent(context, susirasyti.class);
                        intent.putExtra("dabartinis_id", prisijungti.currentKorepetitorius.getId());
                        intent.putExtra("gavejas_id", sarasas.getGavejoId());
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(context, susirasyti.class);
                        intent.putExtra("dabartinis_id", prisijungti.currentKorepetitorius.getId());
                        intent.putExtra("gavejas_id", sarasas.getSiuntejoId());
                        context.startActivity(intent);
                    }
                }
                else
                {
                    if (prisijungti.currentMokinys.getId() == sarasas.getSiuntejoId())
                    {
                        Intent intent = new Intent(context, susirasyti.class);
                        intent.putExtra("dabartinis_id", prisijungti.currentMokinys.getId());
                        intent.putExtra("gavejas_id", sarasas.getGavejoId());
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(context, susirasyti.class);
                        intent.putExtra("dabartinis_id", prisijungti.currentMokinys.getId());
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
