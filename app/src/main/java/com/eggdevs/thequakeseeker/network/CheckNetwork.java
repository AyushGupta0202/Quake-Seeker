package com.eggdevs.thequakeseeker.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;


/**
 * Dedicated class to check network connectivity
 */
public class CheckNetwork {

    private Context context;

    public CheckNetwork(Context context) {
        this.context = context;
    }

    public void registerNetworkCallback() {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {

                    @Override
                    public void onAvailable(@NonNull Network network) {
                        super.onAvailable(network);
                        GlobalVariables.isNetworkConnected = true;
                    }

                    @Override
                    public void onLost(@NonNull Network network) {
                        super.onLost(network);
                        GlobalVariables.isNetworkConnected = false;
                    }
                });
            }
        }
        catch (Exception e) {
            GlobalVariables.isNetworkConnected = false;
        }
    }
}
