package com.example.bakalauras.ui.pagrindinis;

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

import java.util.ArrayList;
import java.util.Random;

import Model.KorepetitoriausKortele;
import Model.KorepetitoriusKortelePagrindinis;

class korepetitoriusPagrindinisCardHolder extends RecyclerView.ViewHolder {

    public TextView vardas, kaina, dalykai, budasKortele, ivertinimas;
    public Button favorite;
    public ImageView zvaigzde;

    public korepetitoriusPagrindinisCardHolder(@NonNull View itemView) {
        super(itemView);

        vardas = itemView.findViewById(R.id.vardasKortelePagrindinis);
        kaina = itemView.findViewById(R.id.kainaKortelePagrindinis);
        dalykai = itemView.findViewById(R.id.dalykaiKortelePagrindinis);
        budasKortele = itemView.findViewById(R.id.budasKortelePagrindinis);
        favorite = itemView.findViewById(R.id.favoriteButtonPagrindinis);
        ivertinimas = itemView.findViewById(R.id.atsiliepimaiVidurkisKortelePagrindinis);
        zvaigzde = itemView.findViewById(R.id.imageView2Pagrindinis);
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
}