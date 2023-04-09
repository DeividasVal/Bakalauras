package com.example.bakalauras;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class korepetitoriusCardHolder extends RecyclerView.ViewHolder {

    public static TextView vardas, kaina, dalykai, budasKortele;
    public static Button favorite;

    public korepetitoriusCardHolder(@NonNull View itemView) {
        super(itemView);

        vardas = itemView.findViewById(R.id.vardasKortele);
        kaina = itemView.findViewById(R.id.kainaKortele);
        dalykai = itemView.findViewById(R.id.dalykaiKortele);
        budasKortele = itemView.findViewById(R.id.budasKortele);
        favorite = itemView.findViewById(R.id.favoriteButton);
    }
}
