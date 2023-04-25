package com.example.bakalauras.ui.uzklausos.korepetitoriaus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.recyclerViewPaspaustasKorepetitorius;
import com.example.bakalauras.ui.uzklausos.mokinio.MokinioCardAdapter;
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
    public void onBindViewHolder(@NonNull KorepetitoriausUzklausosCardHolder holder, @SuppressLint("RecyclerView") int position) {
        KorepetitoriausUzklausaKortele sarasas = list.get(position);

        KorepetitoriausUzklausosCardHolder.mokinioVardas.setText("Mokinys: " + sarasas.getVardasMokinio());

        KorepetitoriausUzklausosCardHolder.atsaukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UzklausosVeiksmasTask task = new UzklausosVeiksmasTask(prisijungti.currentKorepetitorius.getId(), 2);
                task.execute();
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        });
        KorepetitoriausUzklausosCardHolder.patvirtinti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UzklausosVeiksmasTask task = new UzklausosVeiksmasTask(prisijungti.currentKorepetitorius.getId(), 1);
                task.execute();
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
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

    private class UzklausosVeiksmasTask extends AsyncTask<String, Void, String> {

        private int korepetitoriausId;
        private int busena;

        public UzklausosVeiksmasTask(int korepetitoriausId, int busena) {
            this.korepetitoriausId = korepetitoriausId;
            this.busena = busena;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.0.105/PHPscriptai/uzklausaKorepetitoriuiPasirinkti.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data = "korepetitoriaus_id=" + korepetitoriausId + "&būsena=" + busena;
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                reader.close();
                response = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error!";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            Log.d("kor", response);
            notifyDataSetChanged();
        }
    }
}
