package com.example.bakalauras.ui.uzklausos.korepetitoriaus;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.recyclerViewPaspaustasKorepetitorius;
import com.example.bakalauras.ui.uzklausos.mokinio.MokinioUzklausosCardHolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Model.KorepetitoriausUzklausaKortele;
import Model.MokinioUzklausaKortele;

public class KorepetitoriausUzklausosCardAdapter extends RecyclerView.Adapter<KorepetitoriausUzklausosCardHolder>{

    private ArrayList<KorepetitoriausUzklausaKortele> list;
    private Context context;

    public KorepetitoriausUzklausosCardAdapter(ArrayList<KorepetitoriausUzklausaKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public KorepetitoriausUzklausosCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_uzklausa_item, parent, false);

        return new KorepetitoriausUzklausosCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KorepetitoriausUzklausosCardHolder holder, int position) {
        KorepetitoriausUzklausaKortele sarasas = list.get(position);

        KorepetitoriausUzklausosCardHolder.mokinioVardas.setText("Mokinys: " + sarasas.getVardasMokinio());

        KorepetitoriausUzklausosCardHolder.atsaukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        KorepetitoriausUzklausosCardHolder.patvirtinti.setOnClickListener(new View.OnClickListener() {
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
