package com.example.bakalauras;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link kategorijos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class kategorijos extends Fragment {

    public kategorijos() {
        // Required empty public constructor
    }

    public static kategorijos newInstance() {
        kategorijos fragment = new kategorijos();
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
        return inflater.inflate(R.layout.fragment_kategorijos, container, false);
    }
}