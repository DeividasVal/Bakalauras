package com.example.bakalauras;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.ui.korepetitorius.Korepetitorius_profilis;

import java.util.ArrayList;

import Model.KorepetitoriausKortele;

public class korepetitoriusCardAdapter extends RecyclerView.Adapter<korepetitoriusCardHolder> {

    ArrayList<KorepetitoriausKortele> list;
    Context context;

    public korepetitoriusCardAdapter(ArrayList<KorepetitoriausKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public korepetitoriusCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_korepetitorius_item, parent, false);

        return new korepetitoriusCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull korepetitoriusCardHolder holder, int position) {
        KorepetitoriausKortele sarasas = list.get(position);
        if (prisijungti.currentKorepetitorius != null)
        {
            korepetitoriusCardHolder.favorite.setVisibility(View.GONE);
        }
        korepetitoriusCardHolder.vardas.setText(sarasas.getVardas());
        korepetitoriusCardHolder.dalykai.setText("Moko: " + sarasas.getDalykai());
        korepetitoriusCardHolder.kaina.setText(sarasas.getKaina() + " Eur/val.");
        korepetitoriusCardHolder.budasKortele.setText("Mokymo tipas:" + sarasas.getMokymoBudas());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, recyclerViewPaspaustasKorepetitorius.class);
                intent.putExtra("korepetitorius_id", sarasas.getId());
                intent.putExtra("korepetitorius_vardas", sarasas.getVardas());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<KorepetitoriausKortele> newList){
        list = newList;
        notifyDataSetChanged();
    }
}
