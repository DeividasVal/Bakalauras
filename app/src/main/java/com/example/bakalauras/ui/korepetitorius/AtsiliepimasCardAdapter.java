package com.example.bakalauras.ui.korepetitorius;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import Model.Atsiliepimas;

public class AtsiliepimasCardAdapter extends RecyclerView.Adapter<AtsiliepimasCardHolder>{

    private ArrayList<Atsiliepimas> list;
    private Context context;

    public AtsiliepimasCardAdapter(ArrayList<Atsiliepimas> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AtsiliepimasCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.atsiliepimo_item, parent, false);

        return new AtsiliepimasCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtsiliepimasCardHolder holder, int position) {
        Atsiliepimas sarasas = list.get(position);

        AtsiliepimasCardHolder.vardas.setText(sarasas.getVardas());
        AtsiliepimasCardHolder.laikas.setText(sarasas.getLaikas());
        AtsiliepimasCardHolder.tekstas.setText(sarasas.getTekstas());
        AtsiliepimasCardHolder.tekstas.setAutoSizeTextTypeUniformWithConfiguration(12, 80, 2, TypedValue.COMPLEX_UNIT_SP);
        AtsiliepimasCardHolder.ivertinimas.setNumStars(5);
        AtsiliepimasCardHolder.ivertinimas.setIsIndicator(true);
        AtsiliepimasCardHolder.ivertinimas.setRating(sarasas.getIvertinimas().floatValue());

        if (sarasas.getMokinioNuotrauka().isEmpty())
        {
            holder.pfp.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }
        else
        {
            Picasso.get()
                    .load("http://192.168.0.101/PHPscriptai/" + sarasas.getMokinioNuotrauka())
                    .transform(new CircleTransform())
                    .into(AtsiliepimasCardHolder.pfp);
        }
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
