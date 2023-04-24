package com.example.bakalauras.ui.failai.korepetitoriaus;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;

public class KorepetitoriausFailaiCardHolder extends RecyclerView.ViewHolder{

    public static TextView pavadinimas, vardas, laikas, failoPav;
    public static ImageView pasalinti;

    public KorepetitoriausFailaiCardHolder(@NonNull View itemView) {
        super(itemView);

        pavadinimas = itemView.findViewById(R.id.failoPavKorepetitoriui);
        vardas = itemView.findViewById(R.id.vardasKorteleFailoKorepetitoriui);
        laikas = itemView.findViewById(R.id.failoLaikasKorepetitoriui);
        failoPav = itemView.findViewById(R.id.failoPavadinimasKorepetitoriui);
        pasalinti = itemView.findViewById(R.id.pasalintiFaila);
    }
}
