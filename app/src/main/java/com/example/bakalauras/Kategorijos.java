package com.example.bakalauras;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Kategorijos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Kategorijos extends Fragment {

    private LinearLayout matematika, anglu, informatika, lietuviu, vokieciu, istorija, chemija, fizika, geografija, muzika;

    public Kategorijos() {
        // Required empty public constructor
    }

    public static Kategorijos newInstance() {
        Kategorijos fragment = new Kategorijos();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_kategorijos, container, false);

        matematika = v.findViewById(R.id.matematikaKategorijaFilter);
        anglu = v.findViewById(R.id.angluKategorijaFilter);
        informatika = v.findViewById(R.id.informatikaKategorijaFilter);
        lietuviu = v.findViewById(R.id.lietuviuKategorijaFilter);
        vokieciu = v.findViewById(R.id.vokieciuKategorijaFilter);
        istorija = v.findViewById(R.id.istorijaKategorijaFilter);
        chemija = v.findViewById(R.id.chemijaKategorijaFilter);
        fizika = v.findViewById(R.id.fizikaKategorijaFilter);
        geografija = v.findViewById(R.id.geografijaKategorijaFilter);
        muzika = v.findViewById(R.id.muzikaKategorijaFilter);

        matematika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dalykas","matematika");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_ziureti, bundle);
            }
        });

        anglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dalykas","anglu kalba");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_ziureti, bundle);
            }
        });

        informatika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dalykas","informatika");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_ziureti, bundle);
            }
        });

        lietuviu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dalykas","lietuviu kalba");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_ziureti, bundle);
            }
        });

        vokieciu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dalykas","vokieciu kalba");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_ziureti, bundle);
            }
        });

        istorija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dalykas","istorija");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_ziureti, bundle);
            }
        });

        chemija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dalykas","chemija");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_ziureti, bundle);
            }
        });

        fizika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dalykas","fizika");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_ziureti, bundle);
            }
        });

        geografija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dalykas","geografija");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_ziureti, bundle);
            }
        });

        muzika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dalykas","muzika");

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.nav_ziureti, bundle);
            }
        });

        return v;
    }
}