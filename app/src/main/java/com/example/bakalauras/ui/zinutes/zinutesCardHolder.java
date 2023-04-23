package com.example.bakalauras.ui.zinutes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;

public class zinutesCardHolder extends RecyclerView.ViewHolder{

    public static TextView vardas, laikas, zinutesTekstas;

    public zinutesCardHolder(@NonNull View itemView) {
        super(itemView);

        vardas = itemView.findViewById(R.id.vardasKorteleZinuciu);
        laikas = itemView.findViewById(R.id.zinutesLaikas);
        zinutesTekstas = itemView.findViewById(R.id.zinutesTekstas);
    }
}
