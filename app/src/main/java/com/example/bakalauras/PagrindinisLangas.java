package com.example.bakalauras;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakalauras.databinding.ActivityPagrindinisLangasBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import Model.Korepetitorius;
import Model.Mokinys;

public class PagrindinisLangas extends AppCompatActivity {

    private TextView prisijungesVardas, prisijungesPastas;
    private ImageView pfp;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPagrindinisLangasBinding binding;
    private boolean arTuriprofili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPagrindinisLangasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarPagrindinisLangas.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_sukurti_profili, R.id.nav_pagrindinis, R.id.nav_zinutes, R.id.nav_ziureti, R.id.nav_kategorija ,R.id.nav_ziureti,R.id.nav_issaugotikop,R.id.nav_manokor,R.id.nav_manomok,
                R.id.nav_pammedziagaMokinys, R.id.nav_atsijungti, R.id.nav_profilisKorepetitorius, R.id.nav_uzklausosMokiniai, R.id.nav_profilisMokinys, R.id.nav_kategorija, R.id.nav_issaugotikop,
                R.id.nav_zinutes, R.id.nav_uzklausosKorepetitoriai, R.id.nav_pammedziagaKorepetitorius)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_pagrindinis_langas);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Intent intent = getIntent();
        Mokinys mokinys = (Mokinys) intent.getSerializableExtra("mokinys");
        Korepetitorius korepetitorius = (Korepetitorius) intent.getSerializableExtra("korepetitorius");
        View header = navigationView.getHeaderView(0);
        if (korepetitorius != null)
        {
            pfp = header.findViewById(R.id.headerPFP);
            prisijungesVardas = header.findViewById(R.id.prisijungesVardas);
            prisijungesPastas = header.findViewById(R.id.prisijungesPastas);
            prisijungesVardas.setText(korepetitorius.getName());
            prisijungesPastas.setText(korepetitorius.getEmail());
            Picasso.get()
                    .load("http://192.168.0.108/PHPscriptai/" + Prisijungti.currentKorepetitorius.getKorepetitoriausNuotrauka())
                    .transform(new CircleTransform())
                    .into(pfp);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_manokor).setVisible(false);
            nav_Menu.findItem(R.id.nav_profilisMokinys).setVisible(false);
            nav_Menu.findItem(R.id.nav_pammedziagaMokinys).setVisible(false);
            nav_Menu.findItem(R.id.nav_issaugotikop).setVisible(false);
            nav_Menu.findItem(R.id.nav_uzklausosMokiniai).setVisible(false);
        }
        else
        {
            pfp = header.findViewById(R.id.headerPFP);
            if (Prisijungti.currentMokinys.getMokinioNuotrauka().isEmpty())
            {
                pfp.setImageResource(R.drawable.ic_baseline_account_circle_24);
            }
            else
            {
                Picasso.get()
                        .load("http://192.168.0.108/PHPscriptai/" + Prisijungti.currentMokinys.getMokinioNuotrauka())
                        .transform(new CircleTransform())
                        .into(pfp);
            }
            prisijungesVardas = header.findViewById(R.id.prisijungesVardas);
            prisijungesPastas = header.findViewById(R.id.prisijungesPastas);
            prisijungesVardas.setText(mokinys.getName());
            prisijungesPastas.setText(mokinys.getEmail());
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_profilisKorepetitorius).setVisible(false);
            nav_Menu.findItem(R.id.nav_manomok).setVisible(false);
            nav_Menu.findItem(R.id.nav_pammedziagaKorepetitorius).setVisible(false);
            nav_Menu.findItem(R.id.nav_sukurti_profili).setVisible(false);
            nav_Menu.findItem(R.id.nav_uzklausosKorepetitoriai).setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_pagrindinis:
                        navController.navigate(R.id.nav_pagrindinis);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_ziureti:
                        navController.navigate(R.id.nav_ziureti);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_kategorija:
                        navController.navigate(R.id.nav_kategorija);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_issaugotikop:
                        navController.navigate(R.id.nav_issaugotikop);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_manokor:
                        navController.navigate(R.id.nav_manokor);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_manomok:
                        navController.navigate(R.id.nav_manomok);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_uzklausosMokiniai:
                        navController.navigate(R.id.nav_uzklausosMokiniai);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_uzklausosKorepetitoriai:
                        navController.navigate(R.id.nav_uzklausosKorepetitoriai);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_zinutes:
                        navController.navigate(R.id.nav_zinutes);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_pammedziagaKorepetitorius:
                        navController.navigate(R.id.nav_pammedziagaKorepetitorius);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_profilisKorepetitorius:
                        PaziuretiArYraProfilis task = new PaziuretiArYraProfilis(Prisijungti.currentKorepetitorius.getId());
                        task.execute();
                        boolean result = false;
                        try {
                            result = task.get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        arTuriprofili = result;
                        if (arTuriprofili) {
                            navController.navigate(R.id.nav_profilisKorepetitorius);
                        } else {
                            Toast.makeText(getApplicationContext(), "Susikurkite profilį!", Toast.LENGTH_SHORT).show();
                        }
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_profilisMokinys:
                        navController.navigate(R.id.nav_profilisMokinys);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_pammedziagaMokinys:
                        navController.navigate(R.id.nav_pammedziagaMokinys);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_pasalinti:
                        AlertDialog.Builder builder = new AlertDialog.Builder(PagrindinisLangas.this);
                        builder.setTitle("Pašalinti profilį");
                        builder.setMessage("Ar jūs tikrai norite pašalinti savo profilį?");
                        builder.setPositiveButton("TAIP", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (Prisijungti.currentKorepetitorius != null) {
                                    PasalintiProfiliKorepetitorius task = new PasalintiProfiliKorepetitorius(Prisijungti.currentKorepetitorius.getId());
                                    task.execute();
                                } else {
                                    PasalintiProfiliMokinys task = new PasalintiProfiliMokinys(Prisijungti.currentMokinys.getId());
                                    task.execute();
                                }
                                Prisijungti.currentKorepetitorius = null;
                                Prisijungti.currentMokinys = null;
                                Intent intent = new Intent(PagrindinisLangas.this, Prisijungti.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        });
                        builder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;
                    case R.id.nav_atsijungti:
                        Prisijungti.currentKorepetitorius = null;
                        Prisijungti.currentMokinys = null;
                        Intent intent2 = new Intent(PagrindinisLangas.this, Prisijungti.class);
                        startActivity(intent2);
                        Toast.makeText(getApplicationContext(), "Sėkmingai atsijungtėte!", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        NavController navController = Navigation.findNavController(PagrindinisLangas.this, R.id.nav_host_fragment_content_pagrindinis_langas);
                        NavigationUI.onNavDestinationSelected(item, navController);
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_pagrindinis_langas);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private class PasalintiProfiliKorepetitorius extends AsyncTask<String, Void, String> {

        private int korepetitoriausId;

        public PasalintiProfiliKorepetitorius(int korepetitoriausId) {
            this.korepetitoriausId = korepetitoriausId;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.0.108/PHPscriptai/pasalintiProfiliKorepetitorius.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data = "korepetitoriaus_id=" + korepetitoriausId;
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
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
        }
    }

    private class PasalintiProfiliMokinys extends AsyncTask<String, Void, String> {

        private int mokinioId;

        public PasalintiProfiliMokinys(int mokinioId) {
            this.mokinioId = mokinioId;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL("http://192.168.0.108/PHPscriptai/pasalintiProfiliMokinys.php");
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
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
        }
    }

    private class PaziuretiArYraProfilis extends AsyncTask<Void, Void, Boolean> {

        private int korepetitoriausId;

        public PaziuretiArYraProfilis(int korepetitoriausId) {
            this.korepetitoriausId = korepetitoriausId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return paziuretiArYraProfilis(korepetitoriausId);
        }

        @Override
        protected void onPostExecute(Boolean atsakymas) {
            arTuriprofili = atsakymas;
        }
    }

    public static boolean paziuretiArYraProfilis(int korepetitoriausId) {
        try {
            URL url = new URL("http://192.168.0.108/PHPscriptai/arKorepetitoriusTuriProfili.php?korepetitoriaus_id=" + korepetitoriausId);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();
            reader.close();

            Log.d("Response string: ", response);  // add this line


            return response.equals("true");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public class CircleTransform implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}