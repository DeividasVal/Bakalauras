package com.example.bakalauras.ui.uzklausos.mokinioKorepetitoriai;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;
import com.example.bakalauras.prisijungti;
import com.example.bakalauras.recyclerViewPaspaustasKorepetitorius;
import com.example.bakalauras.registruotis;
import com.example.bakalauras.ui.uzklausos.korepetitoriausMokiniai.KorepetitoriuiPatvirtintiMokiniaiCardHolder;
import com.example.bakalauras.ui.uzklausos.mokinio.MokinioCardAdapter;
import com.example.bakalauras.ui.zinutes.susirasyti;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Model.KorepetitoriuiPatvirtintasMokinysKortele;
import Model.MokiniuiPatvirtintasKorepetitoriusKortele;

public class MokiniuiPatvirtintiKorepetitoriaiCardAdapter extends RecyclerView.Adapter<MokiniuiPatvirtintiKorepetitoriaiCardHolder>{

    private ArrayList<MokiniuiPatvirtintasKorepetitoriusKortele> list;
    private Context context;
    private Dialog popup;

    public MokiniuiPatvirtintiKorepetitoriaiCardAdapter(ArrayList<MokiniuiPatvirtintasKorepetitoriusKortele> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MokiniuiPatvirtintiKorepetitoriaiCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mokinio_korepetitorius_item, parent, false);

        return new MokiniuiPatvirtintiKorepetitoriaiCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MokiniuiPatvirtintiKorepetitoriaiCardHolder holder, @SuppressLint("RecyclerView") int position) {
        MokiniuiPatvirtintasKorepetitoriusKortele sarasas = list.get(position);

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.korepetitoriausVardas.setText(sarasas.getVardasKorepetitoriaus());
        MokiniuiPatvirtintiKorepetitoriaiCardHolder.adresas.setText(sarasas.getAdresas());
        MokiniuiPatvirtintiKorepetitoriaiCardHolder.kaina.setText(sarasas.getKaina() + " Eur/val.");
        MokiniuiPatvirtintiKorepetitoriaiCardHolder.dalykai.setText("Dalykai: " + sarasas.getDalykai());
        popup = new Dialog(context);

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.atsaukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasalintiKorepetitoriuTask task = new PasalintiKorepetitoriuTask(prisijungti.currentMokinys.getId());
                task.execute();
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        });

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.parasyti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, susirasyti.class);
                intent.putExtra("dabartinis_id", prisijungti.currentMokinys.getId());
                intent.putExtra("gavejas_id", sarasas.getKorepetitoriausId());
                context.startActivity(intent);
            }
        });

        MokiniuiPatvirtintiKorepetitoriaiCardHolder.ivertinti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = LayoutInflater.from(context).inflate(R.layout.popup_atsiliepimas, null);

                Button skelbti = popupView.findViewById(R.id.skelbtiAtsiliepima);
                Button uzdaryti = popupView.findViewById(R.id.uzdarytiAtsiliepimoLanga);
                RatingBar ivertinimas = popupView.findViewById(R.id.ratingBarPopup);
                EditText aprasymas = popupView.findViewById(R.id.aprasymasPopup);

                skelbti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (aprasymas.getText() != null && aprasymas.getText().length() > 0) {
                            SkelbtiAtsiliepima task = new SkelbtiAtsiliepima(prisijungti.currentMokinys.getId(), sarasas.getKorepetitoriausId(), sarasas.getProfiliausId(), ivertinimas.getRating(), aprasymas);
                            task.execute();
                            popup.dismiss();
                        } else {
                            Toast.makeText(context, "Parašykite atsiliepimą!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                uzdaryti.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                    }
                });

                popup = new Dialog(context);
                popup.setContentView(popupView);
                popup.show();
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

    private class SkelbtiAtsiliepima extends AsyncTask<Void, Void, String> {
        private int mokinioId;
        private int korepetitoriausId;
        private int profilioId;
        private float ivertinimas;
        private EditText aprasymas;

        public SkelbtiAtsiliepima(int mokinioId, int korepetitoriausId, int profilioId, float ivertinimas, EditText aprasymas) {
            this.mokinioId = mokinioId;
            this.korepetitoriausId = korepetitoriausId;
            this.profilioId = profilioId;
            this.ivertinimas = ivertinimas;
            this.aprasymas = aprasymas;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            String aprasymasGet = String.valueOf(aprasymas.getText());
            try {
                long currentTimeMillis = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedTime = sdf.format(new Date(currentTimeMillis));
                URL url = new URL("http://192.168.0.105/PHPscriptai/skelbtiAtsiliepima.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                String data = "mokinio_id=" + mokinioId + "&korepetitoriaus_id=" + korepetitoriausId + "&profilio_id=" + profilioId + "&ivertinimas=" + ivertinimas + "&atsiliepimo_tekstas=" + aprasymasGet + "&laikas=" + formattedTime;
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
        }
    }

    private class PasalintiKorepetitoriuTask extends AsyncTask<String, Void, String> {

        private int mokinioId;

        public PasalintiKorepetitoriuTask(int mokinioId) {
            this.mokinioId = mokinioId;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.0.105/PHPscriptai/pasalintiUzklausaMokinys.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data = "mokinio_id=" + mokinioId;
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
            notifyDataSetChanged();
        }
    }
}
