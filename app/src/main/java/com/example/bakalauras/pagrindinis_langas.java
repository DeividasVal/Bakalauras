package com.example.bakalauras;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bakalauras.databinding.ActivityPagrindinisLangasBinding;

import Model.Korepetitorius;
import Model.Mokinys;

public class pagrindinis_langas extends AppCompatActivity {

    public TextView prisijungesVardas, prisijungesPastas;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPagrindinisLangasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPagrindinisLangasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarPagrindinisLangas.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_pagrindinis, R.id.nav_zinutes, R.id.nav_ziureti, R.id.nav_kategorija ,R.id.nav_ziureti,R.id.nav_issaugotikop,R.id.nav_manokor,R.id.nav_manomok,
                R.id.nav_pammedziaga, R.id.nav_redaguotiKor, R.id.nav_redaguotiMokinys, R.id.nav_atsijungti)
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
            prisijungesVardas = header.findViewById(R.id.prisijungesVardas);
            prisijungesPastas = header.findViewById(R.id.prisijungesPastas);
            prisijungesVardas.setText(korepetitorius.getName());
            prisijungesPastas.setText(korepetitorius.getEmail());
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_manokor).setVisible(false);
            nav_Menu.findItem(R.id.nav_redaguotiMokinys).setVisible(false);
            nav_Menu.findItem(R.id.nav_issaugotikop).setVisible(false);
        }
        else
        {
            prisijungesVardas = header.findViewById(R.id.prisijungesVardas);
            prisijungesPastas = header.findViewById(R.id.prisijungesPastas);
            prisijungesVardas.setText(mokinys.getName());
            prisijungesPastas.setText(mokinys.getEmail());
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_manomok).setVisible(false);
            nav_Menu.findItem(R.id.nav_redaguotiKor).setVisible(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pagrindinis_langas, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_pagrindinis_langas);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}