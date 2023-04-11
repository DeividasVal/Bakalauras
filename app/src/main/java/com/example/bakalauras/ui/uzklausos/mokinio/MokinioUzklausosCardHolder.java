package com.example.bakalauras.ui.uzklausos.mokinio;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;

public class MokinioUzklausosCardHolder extends RecyclerView.ViewHolder{

    public static TextView korepetitoriausVardas, statusas;
    public static Button atsaukti;

    public MokinioUzklausosCardHolder(@NonNull View itemView) {
        super(itemView);

        korepetitoriausVardas = itemView.findViewById(R.id.vardasKorteleLaukiamoKorepetitoriaus);
        statusas = itemView.findViewById(R.id.uzklausosStatusasMokiniui);
        atsaukti = itemView.findViewById(R.id.buttonAtšauktiMokiniui);
    }
}
