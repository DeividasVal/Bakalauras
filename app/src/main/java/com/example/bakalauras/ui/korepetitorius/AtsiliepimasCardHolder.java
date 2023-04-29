package com.example.bakalauras.ui.korepetitorius;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;

public class AtsiliepimasCardHolder extends RecyclerView.ViewHolder
{
    public static TextView laikas, tekstas, vardas;
    public static RatingBar ivertinimas;
    public static ImageView pfp;

    public AtsiliepimasCardHolder(@NonNull View itemView) {
        super(itemView);

        laikas = itemView.findViewById(R.id.atsiliepimoLaikas);
        tekstas = itemView.findViewById(R.id.atsiliepimoTekstas);
        vardas = itemView.findViewById(R.id.vardasKorteleAtsiliepimo);
        ivertinimas = itemView.findViewById(R.id.ratingBarCard);
        pfp = itemView.findViewById(R.id.atsiliepimasKortelePFP);
    }
}
