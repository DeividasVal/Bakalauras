package com.example.bakalauras.ui.uzklausos.korepetitoriausMokiniai;

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
import com.example.bakalauras.IkeltiFailaPopup;
import com.example.bakalauras.ui.zinutes.Susirasyti;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import Model.KorepetitoriuiPatvirtintasMokinysKortele;

public class KorepetitoriuiPatvirtintiMokiniaiCardAdapter extends RecyclerView.Adapter<KorepetitoriuiPatvirtintiMokiniaiCardHolder> {

    private ArrayList<KorepetitoriuiPatvirtintasMokinysKortele> list;
    private Context context;

    public KorepetitoriuiPatvirtintiMokiniaiCardAdapter(ArrayList<KorepetitoriuiPatvirtintasMokinysKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public KorepetitoriuiPatvirtintiMokiniaiCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.korepetitoriaus_mokinys_item, parent, false);

        return new KorepetitoriuiPatvirtintiMokiniaiCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KorepetitoriuiPatvirtintiMokiniaiCardHolder holder, int position) {
        KorepetitoriuiPatvirtintasMokinysKortele sarasas = list.get(position);

        KorepetitoriuiPatvirtintiMokiniaiCardHolder.mokinioVardas.setText(sarasas.getVardasMokinio());

        if (sarasas.getMokinioNuotrauka().isEmpty())
        {
            KorepetitoriuiPatvirtintiMokiniaiCardHolder.pfp.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }
        else
        {
            Picasso.get()
                    .load("http://192.168.0.108/PHPscriptai/" + sarasas.getMokinioNuotrauka())
                    .transform(new CircleTransform())
                    .into(KorepetitoriuiPatvirtintiMokiniaiCardHolder.pfp);
        }

        KorepetitoriuiPatvirtintiMokiniaiCardHolder.parasyti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Susirasyti.class);
                intent.putExtra("dabartinis_id", Prisijungti.currentKorepetitorius.getId());
                intent.putExtra("gavejas_id", sarasas.getMokinioId());
                context.startActivity(intent);
            }
        });

        KorepetitoriuiPatvirtintiMokiniaiCardHolder.pridetiMedziaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, IkeltiFailaPopup.class);
                intent.putExtra("korepetitorius_id", Prisijungti.currentKorepetitorius.getId());
                intent.putExtra("mokinio_id", sarasas.getMokinioId());
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
