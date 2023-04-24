package com.example.bakalauras.ui.failai.mokinio;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;

public class MokinioFailaiCardHolder extends RecyclerView.ViewHolder{

    public static TextView pavadinimas, vardas, laikas, failoPav;
    public static ImageView atsisiusti;

    public MokinioFailaiCardHolder(@NonNull View itemView) {
        super(itemView);

        pavadinimas = itemView.findViewById(R.id.failoPav);
        vardas = itemView.findViewById(R.id.vardasKorteleFailoMokiniui);
        laikas = itemView.findViewById(R.id.failoLaikasMokiniui);
        failoPav = itemView.findViewById(R.id.failoPavadinimasMokiniui);
        atsisiusti = itemView.findViewById(R.id.atsisiustiFaila);
    }
}
