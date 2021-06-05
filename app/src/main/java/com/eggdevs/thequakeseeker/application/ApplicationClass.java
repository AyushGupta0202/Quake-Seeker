package com.eggdevs.thequakeseeker.application;

import android.app.Application;

import com.eggdevs.thequakeseeker.R;
import com.eggdevs.thequakeseeker.data.EarthquakeDos;

import java.util.ArrayList;
import java.util.List;

public class ApplicationClass extends Application {

    public static List<EarthquakeDos> dosEarthquakeList;

    @Override
    public void onCreate() {
        super.onCreate();

        dosEarthquakeList = new ArrayList<>();
        dosEarthquakeList.add(new EarthquakeDos(R.drawable.eq_warning_gas_correct, getString(R.string.tip_gas)));
        dosEarthquakeList.add(new EarthquakeDos(R.drawable.eq_warning_table_correct, getString(R.string.tip_table)));
        dosEarthquakeList.add(new EarthquakeDos(R.drawable.eq_warning_evacuate_correct, getString(R.string.tip_evacuate)));
        dosEarthquakeList.add(new EarthquakeDos(R.drawable.eq_warning_window_correct, getString(R.string.tip_window)));
        dosEarthquakeList.add(new EarthquakeDos(R.drawable.eq_warning_head_protec_correct, getString(R.string.tip_head_protec)));
        dosEarthquakeList.add(new EarthquakeDos(R.drawable.eq_warning_stayaway_correct, getString(R.string.tip_stayaway)));
        dosEarthquakeList.add(new EarthquakeDos(R.drawable.eq_warning_hazards_correct, getString(R.string.tip_hazards)));
        dosEarthquakeList.add(new EarthquakeDos(R.drawable.eq_warning_turnoffgas_correct, getString(R.string.tip_turnoffgas)));
        dosEarthquakeList.add(new EarthquakeDos(R.drawable.eq_warning_streetlight_correct, getString(R.string.tip_streetlight)));
    }
}
