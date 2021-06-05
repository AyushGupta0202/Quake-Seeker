package com.eggdevs.thequakeseeker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eggdevs.thequakeseeker.R;
import com.eggdevs.thequakeseeker.adapters.EarthquakeDosAdapter;
import com.eggdevs.thequakeseeker.application.ApplicationClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class WhatFragment extends Fragment {

    private View view;

    public WhatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_what, container, false);
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView dosEarthquakeRecycler = view.findViewById(R.id.dos_recycler_earthquake);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        dosEarthquakeRecycler.setLayoutManager(layoutManager);
        dosEarthquakeRecycler.setHasFixedSize(true);

        EarthquakeDosAdapter earthquakeDosAdapter = new EarthquakeDosAdapter(ApplicationClass.dosEarthquakeList);
        dosEarthquakeRecycler.setAdapter(earthquakeDosAdapter);
    }
}