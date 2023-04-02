package com.example.bakalauras.ui.korepetitorius;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bakalauras.R;
import com.example.bakalauras.registruotis;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KorKurtiProfili#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KorKurtiProfili extends Fragment {

    public Button pridetiDalyka;
    public ListView dalykuSarasas;
    public Spinner spinnerDalykai;
    public EditText bio;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter adapter;

    public KorKurtiProfili() {
        // Required empty public constructor
    }

    public static KorKurtiProfili newInstance() {
        KorKurtiProfili fragment = new KorKurtiProfili();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_kor_kurti_profili, container, false);

        pridetiDalyka = v.findViewById(R.id.buttonPridėti);
        dalykuSarasas = v.findViewById(R.id.pasirinktiDalykai);
        spinnerDalykai = v.findViewById(R.id.spinnerDalykai);
        bio = v.findViewById(R.id.bioProfilis);
        dalykuSarasas.setNestedScrollingEnabled(true);
        bio.setNestedScrollingEnabled(true);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listItems);
        dalykuSarasas.setAdapter(adapter);

        pridetiDalyka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listItems.contains(spinnerDalykai.getSelectedItem().toString())) {
                    listItems.add(spinnerDalykai.getSelectedItem().toString());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        dalykuSarasas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Laikykite, kad pašalinti.", Toast.LENGTH_SHORT).show();
            }
        });

        dalykuSarasas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                listItems.remove(adapterView.getItemAtPosition(i));
                adapter.notifyDataSetChanged();

                return true;
            }
        });

        return v;
    }
}