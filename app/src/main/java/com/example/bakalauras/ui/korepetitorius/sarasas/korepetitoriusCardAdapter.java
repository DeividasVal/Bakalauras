package com.example.bakalauras.ui.korepetitorius.sarasas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.recyclerViewPaspaustasKorepetitorius;
import com.example.bakalauras.ui.korepetitorius.sarasas.korepetitoriusCardHolder;

import java.util.ArrayList;

import Model.KorepetitoriausKortele;

class korepetitoriusCardHolder extends RecyclerView.ViewHolder {

    public TextView vardas, kaina, dalykai, budasKortele, ivertinimas;
    public Button favorite;
    public ImageView zvaigzde;

    public korepetitoriusCardHolder(@NonNull View itemView) {
        super(itemView);

        vardas = itemView.findViewById(R.id.vardasKortele);
        kaina = itemView.findViewById(R.id.kainaKortele);
        dalykai = itemView.findViewById(R.id.dalykaiKortele);
        budasKortele = itemView.findViewById(R.id.budasKortele);
        favorite = itemView.findViewById(R.id.favoriteButton);
        ivertinimas = itemView.findViewById(R.id.atsiliepimaiVidurkisKortele);
        zvaigzde = itemView.findViewById(R.id.imageView2);
    }
}

public class korepetitoriusCardAdapter extends RecyclerView.Adapter<korepetitoriusCardHolder> {

    private ArrayList<KorepetitoriausKortele> list;
    private Context context;

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
            holder.favorite.setVisibility(View.GONE);
        }
        holder.vardas.setText(sarasas.getVardas());
        holder.dalykai.setText("Moko: " + sarasas.getDalykai());
        holder.kaina.setText(sarasas.getKaina() + " Eur/val.");
        holder.budasKortele.setText("Mokymo tipas: " + sarasas.getMokymoBudas());

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
                Intent intent = new Intent(context, recyclerViewPaspaustasKorepetitorius.class);
                intent.putExtra("korepetitorius_id", sarasas.getId());
                intent.putExtra("korepetitorius_vardas", sarasas.getVardas());
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

    public void filterList(ArrayList<KorepetitoriausKortele> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }
}

