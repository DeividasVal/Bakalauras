package com.example.bakalauras.ui.zinutes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;

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
        }
        else
        {
            zinutesCardHolder.vardas.setText("Korepetitorius: " + sarasas.getVardas());
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
}
