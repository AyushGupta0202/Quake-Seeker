package com.eggdevs.thequakeseeker.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eggdevs.thequakeseeker.R;
import com.eggdevs.thequakeseeker.WebPageActivity;
import com.eggdevs.thequakeseeker.adapters.CityDetailsAdapter;
import com.eggdevs.thequakeseeker.data.CityDetails;
import com.eggdevs.thequakeseeker.network.CheckNetwork;
import com.eggdevs.thequakeseeker.network.GlobalVariables;
import com.eggdevs.thequakeseeker.query.QueryUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class RecentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String STATE_ITEMS = "items";
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&starttime=2019-01-01&minlatitude=8.067&maxlatitude=37.1&minlongitude=68.1167&maxlongitude=97.4167";
    private static Bundle mBundleRecyclerViewState;
    final String INTERSTITIAL_AD_ID = "ca-app-pub-5774660261058771/3150972095";
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private CityDetailsAdapter earthquakeAdapter;
    private RecyclerView earthquakeRecycler;
    private List<CityDetails> finalResult;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View view;
    private View loadingScreen, noNetworkScreen;
    private InterstitialAd interstitialAd;

    // First fragment lifecycle event.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Checking network connectivity on create fragment.
        checkNetworkConnection();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        checkNetworkConnection();
        finalResult = new ArrayList<>();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recents, container, false);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // SwipeRefreshLayout.
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_blue_dark));

        if (savedInstanceState == null) {

            // Showing Swipe Refresh animation on activity create
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (GlobalVariables.isNetworkConnected) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        new EarthquakeAsyncTask().execute(USGS_REQUEST_URL);
                    } else {
                        loadingScreen.setVisibility(View.GONE);
                    }
                }
            });
        }
        return view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadingScreen = view.findViewById(R.id.loading_screen);
        noNetworkScreen = view.findViewById(R.id.network_screen);

        earthquakeRecycler = view.findViewById(R.id.recent_earthquakes_recycler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        earthquakeRecycler.setLayoutManager(layoutManager);
        earthquakeRecycler.setHasFixedSize(true);

        //Using saved instance state to check if there is previous data present.
        if (savedInstanceState == null) {

            finalResult.clear();
            // Load new data if there is no previous data.
            loadDataIfNetworkAvailable();
        } else {

            // Reload old data if previous data is present.
            finalResult = (List<CityDetails>) savedInstanceState.getSerializable(STATE_ITEMS);
        }

        // Create a new {@link ArrayAdapter} of earthquakes
        earthquakeAdapter = new CityDetailsAdapter(this.getActivity(), finalResult);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeRecycler.setAdapter(earthquakeAdapter);

        earthquakeAdapter.setListener(new CityDetailsAdapter.Listener() {
            @Override
            public void onClick(int position) {
                // Find the current earthquake that was clicked on
                CityDetails currentEarthquake = finalResult.get(position);

                // Intent to web view activity.
                Intent earthquakeIntent = new Intent(getActivity(), WebPageActivity.class);
                earthquakeIntent.putExtra("url", currentEarthquake.getUrl());
                earthquakeIntent.putExtra("ad_id", getString(R.string.interstitial_ad_2));

                // Send the intent to launch a new activity
                startActivity(earthquakeIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            earthquakeRecycler.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Save the RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = earthquakeRecycler.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    // Storing previous data into Bundle.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (!finalResult.isEmpty()) {
            outState.putSerializable(STATE_ITEMS, (Serializable) finalResult);
        }
    }

    public void notifyDataChanged() {
        earthquakeAdapter.notifyDataSetChanged();
    }

    // This method is called when swipe refresh is pulled down.
    @Override
    public void onRefresh() {

        earthquakeRecycler.setVisibility(View.GONE);
        loadDataIfNetworkAvailable();

        if (!GlobalVariables.isNetworkConnected) {
            noNetworkScreen.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadDataIfNetworkAvailable() {

        if (GlobalVariables.isNetworkConnected) {
            noNetworkScreen.setVisibility(View.GONE);
            loadingScreen.setVisibility(View.VISIBLE);
            new EarthquakeAsyncTask().execute(USGS_REQUEST_URL);
        } else {
            noNetworkScreen.setVisibility(View.VISIBLE);
            loadingScreen.setVisibility(View.GONE);
        }
    }

    // Dedicated method to check network connectivity and set isNetworkConnected
    // Global variable.
    private void checkNetworkConnection() {
        CheckNetwork network = new CheckNetwork(this.getActivity());
        network.registerNetworkCallback();
    }

    private void loadInterstitialAd() {
        interstitialAd = new InterstitialAd(getContext());
        interstitialAd.setAdUnitId(INTERSTITIAL_AD_ID);
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    // AsyncTask class to load earthquake data in the background.
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<CityDetails>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Showing progress bar in onPreExecute before data loads.
            loadingScreen.setVisibility(View.VISIBLE);

            // Showing refreshing animation before data loads.
            mSwipeRefreshLayout.setRefreshing(true);

            // loading ad in pre execute.
            loadInterstitialAd();
        }

        @Override
        protected List<CityDetails> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;

            }
            List<CityDetails> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<CityDetails> earthquakes) {
            loadingScreen.setVisibility(View.GONE);

            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            }

            // Clear the adapter of previous earthquake data
            finalResult.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the RecyclerView to update.
            if (earthquakes != null && !earthquakes.isEmpty()) {
                finalResult.addAll(earthquakes);

                notifyDataChanged();

                // Stopping swipe refresh.
                mSwipeRefreshLayout.setRefreshing(false);

                // Making recyclerView visible again.
                earthquakeRecycler.setVisibility(View.VISIBLE);
            }

            if (!GlobalVariables.isNetworkConnected) {
                noNetworkScreen.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}