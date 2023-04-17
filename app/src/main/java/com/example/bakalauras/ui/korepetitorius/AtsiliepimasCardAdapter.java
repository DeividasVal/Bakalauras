package com.example.bakalauras.ui.korepetitorius;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;

import java.util.ArrayList;

import Model.Atsiliepimas;

public class AtsiliepimasCardAdapter extends RecyclerView.Adapter<AtsiliepimasCardHolder>{

    private ArrayList<Atsiliepimas> list;
    private Context context;

    public AtsiliepimasCardAdapter(ArrayList<Atsiliepimas> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AtsiliepimasCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.atsiliepimo_item, parent, false);

        return new AtsiliepimasCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtsiliepimasCardHolder holder, int position) {
        Atsiliepimas sarasas = list.get(position);

        AtsiliepimasCardHolder.vardas.setText(sarasas.getVardas());
        AtsiliepimasCardHolder.laikas.setText(sarasas.getLaikas());
        AtsiliepimasCardHolder.tekstas.setText(sarasas.getTekstas());
        AtsiliepimasCardHolder.tekstas.setAutoSizeTextTypeUniformWithConfiguration(12, 80, 2, TypedValue.COMPLEX_UNIT_SP);
        AtsiliepimasCardHolder.ivertinimas.setNumStars(5);
        AtsiliepimasCardHolder.ivertinimas.setIsIndicator(true);
        AtsiliepimasCardHolder.ivertinimas.setRating(sarasas.getIvertinimas().floatValue());
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        else
        {
            return 0;
        }
    }
}
