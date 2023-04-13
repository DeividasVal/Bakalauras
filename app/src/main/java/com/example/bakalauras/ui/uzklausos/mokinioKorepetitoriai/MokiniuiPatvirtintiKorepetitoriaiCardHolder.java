package com.example.bakalauras.ui.uzklausos.mokinioKorepetitoriai;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;

public class MokiniuiPatvirtintiKorepetitoriaiCardHolder extends RecyclerView.ViewHolder{

    public static TextView korepetitoriausVardas, dalykai, adresas, kaina;
    public static Button parasyti, atsaukti;

    public MokiniuiPatvirtintiKorepetitoriaiCardHolder(@NonNull View itemView) {
        super(itemView);

        korepetitoriausVardas = itemView.findViewById(R.id.vardasKortelePatvirtintoKorepetitoriaus);
        dalykai = itemView.findViewById(R.id.dalykaiPatvirtinti);
        adresas = itemView.findViewById(R.id.adresasPatvirtintasKorepetitorius);
        kaina = itemView.findViewById(R.id.kainaPatvirtintasKorepetitorius);
        parasyti = itemView.findViewById(R.id.mokinysParasyti);
        atsaukti = itemView.findViewById(R.id.buttonAtšauktiPatvirtintasKorepetitorius);
    }
}
