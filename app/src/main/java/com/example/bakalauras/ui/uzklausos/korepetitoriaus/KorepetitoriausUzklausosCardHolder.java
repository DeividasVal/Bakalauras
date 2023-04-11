package com.example.bakalauras.ui.uzklausos.korepetitoriaus;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;

public class KorepetitoriausUzklausosCardHolder extends RecyclerView.ViewHolder{

    public static TextView mokinioVardas;
    public static Button atsaukti, patvirtinti;

    public KorepetitoriausUzklausosCardHolder(@NonNull View itemView){
        super(itemView);

        mokinioVardas = itemView.findViewById(R.id.vardasKorteleMokinio);
        atsaukti = itemView.findViewById(R.id.buttonAtšaukti);
        patvirtinti = itemView.findViewById(R.id.buttonPatvirtinti);
    }
}
