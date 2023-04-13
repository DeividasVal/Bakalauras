package com.example.bakalauras.ui.uzklausos.korepetitoriausMokiniai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.ui.uzklausos.korepetitoriaus.KorepetitoriausUzklausosCardHolder;

import java.util.ArrayList;

import Model.KorepetitoriausUzklausaKortele;
import Model.KorepetitoriuiPatvirtintasMokinysKortele;

public class KorepetitoriuiPatvirtintiMokiniaiCardAdapter extends RecyclerView.Adapter<KorepetitoriuiPatvirtintiMokiniaiCardHolder>{

    private ArrayList<KorepetitoriuiPatvirtintasMokinysKortele> list;
    private Context context;

    public KorepetitoriuiPatvirtintiMokiniaiCardAdapter(ArrayList<KorepetitoriuiPatvirtintasMokinysKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public KorepetitoriuiPatvirtintiMokiniaiCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.korepetitoriaus_mokinys_item, parent, false);

        return new KorepetitoriuiPatvirtintiMokiniaiCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KorepetitoriuiPatvirtintiMokiniaiCardHolder holder, int position) {
        KorepetitoriuiPatvirtintasMokinysKortele sarasas = list.get(position);

        KorepetitoriuiPatvirtintiMokiniaiCardHolder.mokinioVardas.setText(sarasas.getVardasMokinio());

        KorepetitoriuiPatvirtintiMokiniaiCardHolder.parasyti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
