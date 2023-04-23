package com.example.bakalauras.ui.uzklausos.korepetitoriausMokiniai;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;

public class KorepetitoriuiPatvirtintiMokiniaiCardHolder extends RecyclerView.ViewHolder{

    public static TextView mokinioVardas;
    public static Button parasyti;

    public KorepetitoriuiPatvirtintiMokiniaiCardHolder(@NonNull View itemView) {
        super(itemView);

        mokinioVardas = itemView.findViewById(R.id.vardasKorteleMokinioPatvirtintas);
        parasyti = itemView.findViewById(R.id.korepetitoriusParasyti);
    }
}