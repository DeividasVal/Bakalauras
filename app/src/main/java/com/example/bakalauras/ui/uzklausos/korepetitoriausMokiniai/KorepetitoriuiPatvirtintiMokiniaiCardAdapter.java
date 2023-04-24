package com.example.bakalauras.ui.uzklausos.korepetitoriausMokiniai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.ikeltiFailaPopup;
import com.example.bakalauras.ui.zinutes.susirasyti;

import java.util.ArrayList;

import Model.KorepetitoriuiPatvirtintasMokinysKortele;

public class KorepetitoriuiPatvirtintiMokiniaiCardAdapter extends RecyclerView.Adapter<KorepetitoriuiPatvirtintiMokiniaiCardHolder> {

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
                Intent intent = new Intent(context, susirasyti.class);
                intent.putExtra("dabartinis_id", prisijungti.currentKorepetitorius.getId());
                intent.putExtra("gavejas_id", sarasas.getMokinioId());
                context.startActivity(intent);
            }
        });

        KorepetitoriuiPatvirtintiMokiniaiCardHolder.pridetiMedziaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ikeltiFailaPopup.class);
                intent.putExtra("korepetitorius_id", prisijungti.currentKorepetitorius.getId());
                intent.putExtra("mokinio_id", sarasas.getMokinioId());
                context.startActivity(intent);
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
