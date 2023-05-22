package com.example.bakalauras.ui.pagrindinis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.ui.prisijungti.Prisijungti;
import com.example.bakalauras.ui.korepetitorius.sarasas.RecyclerViewPaspaustasKorepetitorius;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Random;

import Model.KorepetitoriusKortelePagrindinis;

class korepetitoriusPagrindinisCardHolder extends RecyclerView.ViewHolder {

    public TextView vardas, kaina, dalykai, budasKortele, ivertinimas;
    public Button favorite;
    public ImageView zvaigzde, pfp;

    public korepetitoriusPagrindinisCardHolder(@NonNull View itemView) {
        super(itemView);

        vardas = itemView.findViewById(R.id.vardasKortelePagrindinis);
        kaina = itemView.findViewById(R.id.kainaKortelePagrindinis);
        dalykai = itemView.findViewById(R.id.dalykaiKortelePagrindinis);
        budasKortele = itemView.findViewById(R.id.budasKortelePagrindinis);
        favorite = itemView.findViewById(R.id.favoriteButtonPagrindinis);
        ivertinimas = itemView.findViewById(R.id.atsiliepimaiVidurkisKortelePagrindinis);
        zvaigzde = itemView.findViewById(R.id.imageView2Pagrindinis);
        pfp = itemView.findViewById(R.id.korKortelePagrindinisPFP);
    }
}

public class PagrindinisCardAdapter extends RecyclerView.Adapter<korepetitoriusPagrindinisCardHolder> {

    private ArrayList<KorepetitoriusKortelePagrindinis> list;
    private Context context;

    public PagrindinisCardAdapter(ArrayList<KorepetitoriusKortelePagrindinis> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public korepetitoriusPagrindinisCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_korepetitorius_pagrindinis_item, parent, false);

        return new korepetitoriusPagrindinisCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull korepetitoriusPagrindinisCardHolder holder, int position) {
        KorepetitoriusKortelePagrindinis sarasas = list.get(getRandomPosition());
        if (Prisijungti.currentKorepetitorius != null)
        {
            holder.favorite.setVisibility(View.GONE);
        }
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
            holder.ivertinimas.setText(sarasas.getIvertinimas().toString());
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
    }

    private int getRandomPosition() {
        return new Random().nextInt(list.size());
    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return 1;
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